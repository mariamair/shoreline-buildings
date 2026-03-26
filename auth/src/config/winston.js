/**
 * Defines the Winston logger.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import { createLogger, format, transports } from 'winston'
import DailyRotateFile from 'winston-daily-rotate-file'

const { combine, timestamp, json, colorize, printf } = format

const consoleFormat = combine(
  colorize(),
  timestamp({ format: 'HH:mm:ss' }),
  printf(({ level, message, timestamp }) => `${timestamp} ${level}: ${message}`)
)

const fileFormat = combine(
  timestamp(),
  json()
)

// Available log levels
const levels = {
  error: 'error',
  warn: 'warn',
  info: 'info',
  http: 'http',
  debug: 'debug',
  silly: 'silly'
}

export const logger = createLogger({
  level: process.env.AUTH_LOG_LEVEL || levels.info,
  transports: [
    new transports.Console({ 
      level: levels.debug,
      format: consoleFormat 
    }),
    new DailyRotateFile({
      filename: './logs/%DATE%-app.log',
      datePattern: 'YYYY-MM-DD',
      maxFiles: '7d',         // keep last 7 days
      maxSize: '20m',         // also rotate if file exceeds 20mb
      zippedArchive: true,    // gzip old logs
      level: process.env.AUTH_LOG_LEVEL || levels.info,
      format: fileFormat
    })
  ],
  exceptionHandlers: [
    new DailyRotateFile({
      filename: './logs/%DATE%-exceptions.log',
      datePattern: 'YYYY-MM-DD',
      maxFiles: '7d',         // keep last 7 days
      maxSize: '20m',         // also rotate if file exceeds 20mb
      zippedArchive: true,    // gzip old logs
      format: fileFormat
    })
  ]
})
