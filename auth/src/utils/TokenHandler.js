/**
 * Handles JSON Web Tokens.
 *
 * @author Maria Mair <mm225mz@student.lnu.se>
 */

import { logger } from '../config/winston.js'
import { RefreshTokenModel } from '../models/RefreshTokenModel.js'
import { AccountModel } from '../models/AccountModel.js'
import { UnauthorizedError } from './ErrorHandler.js'
import ms from 'ms'
import jwt from 'jsonwebtoken'

export class TokenHandler {
  #accessTokenLife
  #refreshTokenLife
  #privateKey
  #publicKey

  constructor() {
    this.#accessTokenLife = process.env.ACCESS_TOKEN_LIFE
    this.#refreshTokenLife = process.env.REFRESH_TOKEN_LIFE
    this.#privateKey = Buffer.from(process.env.ACCESS_TOKEN_PRIVATE_KEY, 'base64').toString('utf-8')
    this.#publicKey = Buffer.from(process.env.ACCESS_TOKEN_PUBLIC_KEY, 'base64').toString('utf-8')
  }

  async createTokens (account) {
    const accessToken = await this.createAccessToken(account)
    const refreshToken = await this.createRefreshToken(account)
    return { accessToken, refreshToken }
  }

  async createAccessToken (account) {
    // Add the username to the access token payload
    const payload = {
      sub: account.id,
      username: account.username,
      email: account.email
    }

    // Create the access token and return it
    return new Promise((resolve, reject) => {
      jwt.sign(
        payload,
        this.#privateKey,
        {
          algorithm: 'RS256',
          expiresIn: this.#accessTokenLife
        },
        (error, token) => {
          if (error) {
            reject(error)
            return
          }
          resolve(token)
        }
      )
    })
  }

  async createRefreshToken (account) {
    // Add the username to the refresh token payload
    const payload = {
      sub: account.username,
      type: 'refresh'
    }

    // Create the refresh token
    const refreshToken = await new Promise((resolve, reject) => {
      jwt.sign(
        payload,
        process.env.REFRESH_TOKEN_SECRET,
        {
          algorithm: 'HS256',
          expiresIn: this.#refreshTokenLife
        },
        (error, token) => {
          if (error) {
            reject(error)
            return
          }
          resolve(token)
        }
      )
    })

    const expiresAt = new Date(Date.now() + ms(this.#refreshTokenLife))

    // Save the refresh token to the database
    const refreshTokenDocument = await RefreshTokenModel.create({
      userId: account.id,
      refreshToken,
      usedToken: false,
      expiresAt
    })

    logger.silly(`Created new refresh token document: ${refreshTokenDocument.id}`)

    return refreshToken
  }

  async verifyRefreshToken (accessToken, refreshToken) {
    // Decode token and verify that it is a valid token and has not expired
    const username = await new Promise((resolve, reject) => {
      jwt.verify(
        refreshToken,
        process.env.REFRESH_TOKEN_SECRET,
        {
          algorithm: 'HS256'
        },
        (error, decoded) => {
          if (error) {
            reject(error)
            return
          }
          resolve(decoded.sub)
        })
    })

    const accountDocument = await AccountModel.findOne({ username })
    const tokenDocuments = await RefreshTokenModel.find({ userId: accountDocument.id, refreshToken })

    // If the token is not found in the database or is found multiple times, throw an error
    if (tokenDocuments.length === 0 || tokenDocuments.length > 1) {
      throw new UnauthorizedError('Invalid token.')
    }

    const tokenDocument = tokenDocuments[0]

    const decoded = jwt.verify(accessToken, this.#publicKey, { ignoreExpiration: true })

    // Make sure the refresh token belongs to the same user as the access token
    if (decoded.sub !== tokenDocument.userId.toString()) {
      throw new UnauthorizedError('Token mismatch.')
    }

    // If the token already has been used, throw an error
    if (tokenDocument.usedToken === true) {
      throw new UnauthorizedError('Reused token detected.')
    }

    // Set token as used
    tokenDocument.usedToken = true
    await tokenDocument.save()

    // Create new access and refresh tokens and return them
    return this.createTokens(accountDocument)
  }
}
