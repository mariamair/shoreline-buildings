/**
 * Defines the Winston logger.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import winston from 'winston'

export const logger = winston.createLogger({
  level: process.env.LOG_LEVEL || 'info',
  format: winston.format.combine(
    winston.format.timestamp(),
    winston.format.json()
  ),
  transports: [
    new winston.transports.Console()
  ]
})
