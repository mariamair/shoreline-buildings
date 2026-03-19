/**
 * The starting point of the authentication server.
 * 
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import express from 'express'
import { logger } from './config/winston.js'
import { connectToDatabase } from './config/mongoose.js'
import { router } from './routes/router.js'
import { ErrorHandler } from './utils/ErrorHandler.js'

try {
  await connectToDatabase(process.env.AUTH_DB_URI)
  
  const app = express()
  
  // Parse incoming requests of content type 'application/x-www-form-urlencoded'.
  app.use(express.urlencoded({ extended: false }))
  
  // Parse incoming requests of content type 'application/json'.
  app.use(express.json())
  
  app.use('/', router)

  app.use(ErrorHandler.handle)

  const PORT = process.env.AUTH_SERVER_PORT || 3000
  
  app.listen(PORT, () => {
    logger.debug(`Server running at http://localhost:${PORT}`)
    logger.debug('Press Ctrl + C to terminate application')
  })
  
} catch (error) {
  logger.error(error)
  process.exitCode = 1
}
