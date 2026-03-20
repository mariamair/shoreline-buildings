/**
 * Defines the base schema.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import { logger } from '../config/winston.js'
import mongoose from 'mongoose'

// Options to use converting the document to a plain object and JSON.
// Transform removes properties that should not be sent to the client.
const convertOptions = {
  getters: true, 
  transform: (doc, ret) => {
    logger.silly(`Transforming document: ${doc._id}`)
    delete ret._id
    delete ret.__v
    delete ret.password
    logger.silly(`Transformed document: ${doc._id}`)
    return ret
  }
}

// Create a schema using timestamps and specified options when converting the document to a POJO (or DTO) or JSON.
// POJO = Plain Old JavaScript Object
const baseSchema = new mongoose.Schema({}, {
  timestamps: true,
  toObject: convertOptions,
  toJSON: convertOptions
})

export const BASE_SCHEMA = Object.freeze(baseSchema)
