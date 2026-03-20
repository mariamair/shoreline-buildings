/**
 * Defines the model for storing refresh tokens.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import mongoose from 'mongoose'
import { BASE_SCHEMA } from './baseSchema.js'

// Create a schema.
const schema = new mongoose.Schema({
  userId: {
    type: String,
    required: true
  },
  refreshToken: {
    type: String,
    required: true
  },
  usedToken: {
    type: Boolean,
    required: true
  },
  expiresAt: {
    type: Date,
    required: true,
    // TTL (Time-To-Live) index
    index: { expires: 0 }
  }
})

schema.add(BASE_SCHEMA)

// Create a model using the schema.
export const RefreshTokenModel = mongoose.model('RefreshToken', schema)
