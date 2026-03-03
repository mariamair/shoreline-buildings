import { gql } from 'graphql-tag'

export const typeDefs = gql`
  type Query {
    buildingCounts: [ShorelineBuildingCount!]!
  }

  type ShorelineBuildingCount {
    id: ID!
    region: Region
    buildingType: BuildingType
    shorelineType: ShorelineType
    areaType: AreaType
    year: Int!
    buildingCount: Int
  }

  type Region {
    id: ID!
    name: String!
    regionType: RegionType
  }

  type RegionType {
    id: ID!
    name: String!
  }

  type BuildingType {
    id: ID!
    name: String!
  }

  type ShorelineType {
    id: ID!
    name: String!
  }

  type AreaType {
    id: ID!
    name: String!
  }
`
