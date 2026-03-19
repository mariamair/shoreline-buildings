/**
 * The starting point of the authentication server.
 * 
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import express from 'express'
import dotenv from 'dotenv'
import path from 'node:path'
import { fileURLToPath } from 'url';
import { connectToDatabase } from './config/mongoose.js'
import { router } from './routes/router.js'
import { ErrorHandler } from './utils/ErrorHandler.js'

try {
  const __filename = fileURLToPath(import.meta.url);
  const __dirname = path.dirname(__filename);

  const envPath = path.join(__dirname, '../../.env');
  dotenv.config({ path: envPath });

  await connectToDatabase(process.env.AUTH_DB_CONNECTION_STRING)
  
  const app = express()
  
  // Parse incoming requests of content type 'application/x-www-form-urlencoded'.
  app.use(express.urlencoded({ extended: false }))
  
  // Parse incoming requests of content type 'application/json'.
  app.use(express.json())
  
  app.use('/', router)

  app.use(ErrorHandler.handle)

  const PORT = process.env.AUTH_SERVER_PORT || 3000
  
  const server = app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`)
  console.log('Press Ctrl + C to terminate application')
  })
  
} catch (error) {
  console.log(error)
  process.exitCode = 1
}