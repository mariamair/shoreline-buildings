/**
 * Defines the main router.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import express from 'express'
import { router as accountRouter } from './accountRouter.js'

export const router = express.Router()

router.get('/', (req, res) => res.status(200).json({ message: 'Authentication API is running!' }))

router.use('/', accountRouter)
