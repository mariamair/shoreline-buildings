# Use Node.js 24 LTS on Alpine for a lightweight image
FROM node:24-alpine
 
# Set working directory
WORKDIR /app
 
# Copy package files first for better layer caching
COPY package*.json ./
 
# Install dependencies
RUN npm ci --ignore-scripts --omit=dev

# Copy the rest of the application source
COPY . .

# Create a log directory
RUN true \
  && mkdir -p /app/logs \
  && chown -R node:node /app/logs
 
# Use a non-root user for security reasons
USER node
 
# Start the application
CMD ["node", "src/server.js"]