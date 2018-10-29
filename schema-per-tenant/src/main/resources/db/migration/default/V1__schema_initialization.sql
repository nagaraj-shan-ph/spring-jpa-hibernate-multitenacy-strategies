-- EXTENSIONS
CREATE EXTENSION IF NOT EXISTS "plpgsql";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- SCHEMA
CREATE SCHEMA IF NOT EXISTS lms;

-- TABLES
CREATE TABLE lms.tenants (
  id                   BIGSERIAL,
  created_at           TIMESTAMP,
  created_by           VARCHAR(50),
  tenant_name          VARCHAR(50),
  schema_name          VARCHAR(50),
  updated_at           TIMESTAMP,
  updated_by           VARCHAR(50),
  url                  VARCHAR(255),
  PRIMARY KEY (id)
);

-- CONSTRAINTS
