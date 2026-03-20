/**
 * Defines the account model.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import mongoose from 'mongoose'
import bcrypt from 'bcryptjs'
import { BASE_SCHEMA } from './baseSchema.js'
import { UnauthorizedError } from '../utils/ErrorHandler.js'

// Create a schema.
const schema = new mongoose.Schema({
  username: {
    type: String,
    required: [true, 'Username is required'],
    unique: true,
    minLength: [8, 'Username must be at least 8 characters.'],
    maxLength: [256, 'Username cannot be longer than 256 characters.'],
    match: [/^[A-Za-z][A-Za-z0-9_-]{7,255}$/, 'Please provide a valid username.']
  },
  password: {
    type: String,
    required: [true, 'Password is required'],
    minLength: [10, 'The password must be at least 10 characters.'],
    maxLength: [256, 'The password cannot be longer than 256 characters.']
  }
})

schema.add(BASE_SCHEMA)

schema.pre('save', async function () {
  if (this.isModified('password')) {
    this.password = await bcrypt.hash(this.password, 10)
  }
})

// Authenticate a user.
schema.statics.authenticate = async function (username, password) {
  const accountDocument = await this.findOne({ username })

  // If user found, compare the supplied password with the stored password hash.
  // Otherwise, use a dummy hash to prevent timing attacks.
  const dummyPasswordHash = await bcrypt.hash(password, 10)
  const suppliedPasswordHash = accountDocument ? accountDocument.password : dummyPasswordHash
  const passwordMatch = await bcrypt.compare(password, suppliedPasswordHash)

  if (!accountDocument || !passwordMatch) {
    throw new UnauthorizedError('Username or password incorrect.')
  }

  return accountDocument
}

// Create a model using the schema.
export const AccountModel = mongoose.model('Account', schema)
