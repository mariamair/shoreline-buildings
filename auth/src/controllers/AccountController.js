/**
 * Defines the AccountController class.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import { logger } from '../config/winston.js'
import { TokenHandler } from '../utils/TokenHandler.js'
import { NotFoundError, UnauthorizedError } from '../utils/ErrorHandler.js'
import { AccountModel } from '../models/AccountModel.js'

export class AccountController {
  #tokenhandler = new TokenHandler()

  // If :id is present, load the item and provide req.doc to the route
  // eslint-disable-next-line max-params
  async loadAccount (req, res, next, id) {
    try {
      logger.silly(`Loading account document: ${id}`)

      const accountDocument = await AccountModel.findById(id)

      if (!accountDocument) {
        throw new NotFoundError('User not found')
      }

      req.doc = accountDocument

      logger.silly(`Loaded acccount document: ${id}`)

      next()
    } catch (error) {
      next(error)
    }
  }

  async createAccount (req, res, next) {
    try {
      logger.silly('Creating new account document')

      const { username, password } = req.body

      const accountDocument = await AccountModel.create({
        username,
        password
      })

      logger.silly(`Created new account document: ${accountDocument.id}`)

      res
        .status(201)
        .json({ 
          status: 'success',
          id: accountDocument.id 
        })
    } catch (error) {
      next(error)
    }
  }

  async login (req, res, next) {
    try {
      logger.silly('Authenticating user')

      const { username, password } = req.body
      const accountDocument = await AccountModel.authenticate(username, password)
      const account = accountDocument.toObject()
      logger.info(account)

      const { accessToken, refreshToken } = await this.#tokenhandler.createTokens(account)

      logger.silly(`Authenticated user: ${accountDocument.id}`)

      res
        .status(200)
        .json({
          status: 'success',
          accessToken,
          refreshToken
        })
    } catch (error) {
      next(error)
    }
  }

  async refreshToken (req, res, next) {
    try {
      const [authenticationScheme, token] = req.headers.authorization?.split(' ') ?? []

      if (authenticationScheme !== 'Bearer' || !token) {
        throw new UnauthorizedError('Invalid or missing authorization token.')
      }
      logger.silly('Refreshing token')

      const { accessToken, refreshToken } = await this.#tokenhandler.verifyRefreshToken(token)

      logger.silly('Refreshed token')

      res
        .status(201)
        .json({
          status: 'success',
          accessToken,
          refreshToken
        })
    } catch (error) {
      next(error)
    }
  }
}
