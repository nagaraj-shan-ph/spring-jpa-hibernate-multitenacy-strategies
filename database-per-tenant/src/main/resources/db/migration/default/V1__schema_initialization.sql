-- EXTENSIONS
CREATE EXTENSION IF NOT EXISTS "plpgsql";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- SCHEMA
CREATE SCHEMA IF NOT EXISTS lms;

-- TABLES
CREATE TABLE lms.data_source_configs (
  id         BIGSERIAL,
  created_at TIMESTAMP,
  created_by VARCHAR(255),
  initialize BOOLEAN,
  name       VARCHAR(255) NOT NULL UNIQUE,
  updated_at TIMESTAMP,
  updated_by VARCHAR(255),
  database_url        VARCHAR(255) NOT NULL UNIQUE,
  username   VARCHAR(255),
  password   VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE lms.tenants (
  id                   BIGSERIAL,
  created_at           TIMESTAMP,
  created_by           VARCHAR(255),
  tenant_name          VARCHAR(255),
  updated_at           TIMESTAMP,
  updated_by           VARCHAR(255),
  databaseUrl                  VARCHAR(255),
  datasource_config_id BIGINT,
  PRIMARY KEY (id)
);



-- CONSTRAINTS

ALTER TABLE lms.data_source_configs
  ADD CONSTRAINT "data_source_configs_name_unique_key" UNIQUE ("name");

ALTER TABLE lms.tenants
  ADD CONSTRAINT "tenants_name_unique_key" UNIQUE ("tenant_name");

ALTER TABLE lms.tenants
  ADD CONSTRAINT FK_tenants_datasource_config_id FOREIGN KEY (datasource_config_id) REFERENCES lms.data_source_configs (id);