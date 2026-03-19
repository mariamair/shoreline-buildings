/**
 * Defines the account router.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 * @version 1.0.0
 */

import express from 'express'
import { AccountController } from '../controllers/AccountController.js'

export const router = express.Router()

const controller = new AccountController()

// Provide req.task to the route if :id is present in the route path.
router.param('id', (req, res, next, id) => controller.loadAccount(req, res, next, id))

// Map HTTP verbs and route paths to controller action methods.
router.get('/', (req, res) => res.json({ message: 'Authentication API is running!' }))

router.post('/register', (req, res, next) => controller.createAccount(req, res, next))

router.post('/login', (req, res, next) => controller.login(req, res, next))

router.post('/refresh', (req, res, next) => controller.refreshToken(req, res, next))
