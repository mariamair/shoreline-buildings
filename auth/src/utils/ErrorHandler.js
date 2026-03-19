/**
 * This module handles errors on the auth server side and in auth database requests.
 * 
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

export class AppError extends Error {
  constructor(message, statusCode) {
    super(message)
    this.statusCode = statusCode
    this.isOperational = true
    Error.captureStackTrace(this, this.constructor)
  }
}

export class ValidationError extends AppError {
  constructor(message) {
    super(message, 400)
    this.name = 'ValidationError'
  }
}

export class NotFoundError extends AppError {
  constructor(message) {
    super(message, 404)
    this.name = 'NotFoundError'
  }
}

export class DatabaseError extends AppError {
  constructor(message) {
    super(message, 500)
    this.name = 'DatabaseError'
  }
}

export class ConflictError extends AppError {
  constructor(message) {
    super(message, 409)
    this.name = 'ConflictError'
  }
}

// Used for authentication and authorization errors.
export class UnauthorizedError extends AppError {
  constructor(message) {
    super(message, 401)
    this.name = 'Unauthorized'
  }
}

export class ErrorHandler {
  // eslint-disable-next-line max-params, no-unused-vars
  static handle(error, req, res, next) {
    const { statusCode = 500, message } = error

    if (process.env.NODE_ENV === 'development') {
      console.error(error)
    }

    // Operational errors (expected errors)
    if (error.isOperational) {
      return res.status(statusCode).json({
        status: 'error',
        statusCode,
        message
      })
    }

    // Programming or unknown errors
    console.error('Unexpected error:', error)
    res.status(500).json({
      status: 'error',
      statusCode: 500,
      message: 'Internal server error'
    })
  }
}
