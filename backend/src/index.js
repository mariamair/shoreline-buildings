// The main starting point of the backend application
import { ApolloServer } from '@apollo/server'
import { startStandaloneServer } from '@apollo/server/standalone'
import { addMocksToSchema } from '@graphql-tools/mock'
import { makeExecutableSchema } from '@graphql-tools/schema'
import { typeDefs } from './schema.js'
import dotenv from 'dotenv'
import path from 'path';
import { fileURLToPath } from 'url';

// Configure port via environment variables
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
dotenv.config({ path: path.resolve(__dirname, '../../.env') })
const PORT = process.env.PORT || 4000;

const mocks = {
  Query: () => ({
    buildingCounts: () => [...new Array(4)]
  }),
  ShorelineBuildingCount: () => ({
    region: () => {
      return {
        id: "0099",
        name: "Storkommunen",
        regionType: { 
          name: "Kommun", 
        },
      }
    },
    buildingType: () => "buildingType_1_01",
    shorelineType: () => "shorelineType_2_01",
    areaType: () => "areaType_3_01",
    year: () => 1999,
    buildingCount: () => 12345,
  })
}

async function startApolloServer() {
  const server = new ApolloServer({ 
    schema: addMocksToSchema({
      schema: makeExecutableSchema({ typeDefs }),
      mocks
    })
  })
  const { url } = await startStandaloneServer(server, {
    listen: { port: PORT}
  })
  console.log(`Server running at ${url}`)
}

startApolloServer()