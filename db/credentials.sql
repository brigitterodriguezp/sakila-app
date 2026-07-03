-- ============================================================
-- Sakila Rental - Database Credentials
-- Creates role 'brigitte' with hashed password (max 8 chars)
-- Grants access to sakila database
-- ============================================================

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'brigitte') THEN
    CREATE ROLE brigitte LOGIN PASSWORD 'b74g1tt3';
  ELSE
    ALTER ROLE brigitte WITH PASSWORD 'b74g1tt3';
  END IF;
END
$$;

GRANT CONNECT ON DATABASE sakila TO brigitte;
GRANT USAGE ON SCHEMA public TO brigitte;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO brigitte;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO brigitte;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO brigitte;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO brigitte;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO brigitte;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO brigitte;

-- Resource limits
ALTER ROLE brigitte WITH CONNECTION LIMIT 10;
