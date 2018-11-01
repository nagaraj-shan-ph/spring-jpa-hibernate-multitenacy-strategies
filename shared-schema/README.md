# Shared Schema Implementation

Sample Spring Boot REST API with shared-schema based Multi-tenancy and Flyway migrations.

## Database Setup

Create **shared-schema** database in postgres. 

When the application start-up [flyway](https://flywaydb.org/getstarted/) migration scripts will create the necessary tables.

![Shared Schema](../images/shared-schema-ERD.png "Shared Schema")
