#!/bin/bash
# ============================================================
# Sakila Rental - Database Setup Script
# Usage: sudo bash db/setup.sh
# ============================================================

set -euo pipefail

DB_NAME="sakila"
DB_USER="brigitte"
DB_PASS="b74g1tt3"
PG_USER="postgres"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "============================================"
echo " Sakila Rental - Database Setup"
echo "============================================"

# Step 1: Create database
echo "[1/6] Creating database '${DB_NAME}'..."
sudo -u ${PG_USER} psql -c "CREATE DATABASE ${DB_NAME};" 2>/dev/null || echo "  Database already exists, skipping."

# Step 2: Run credentials script
echo "[2/6] Creating user '${DB_USER}'..."
sudo -u ${PG_USER} psql -d ${DB_NAME} -f "${SCRIPT_DIR}/credentials.sql"

# Step 3: Load schema
echo "[3/6] Loading schema..."
sudo -u ${PG_USER} psql -d ${DB_NAME} -f "${SCRIPT_DIR}/schema.sql"

# Step 4: Load seed data
echo "[4/6] Loading seed data..."
if [ -f "${SCRIPT_DIR}/seed.sql" ]; then
  sudo -u ${PG_USER} psql -d ${DB_NAME} -f "${SCRIPT_DIR}/seed.sql"
else
  echo "  seed.sql not found, skipping."
fi

# Step 5: Create indexes
echo "[5/6] Creating indexes..."
sudo -u ${PG_USER} psql -d ${DB_NAME} -f "${SCRIPT_DIR}/index.sql"

# Step 6: Verify and configure
echo "[6/6] Verifying setup..."
sudo -u ${PG_USER} psql -d ${DB_NAME} -c "
  SELECT 'Tables:' AS info;
  SELECT table_name FROM information_schema.tables
    WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
    ORDER BY table_name;
"

# Configure resource limits
sudo -u ${PG_USER} psql -c "
  ALTER SYSTEM SET max_connections = 50;
  ALTER SYSTEM SET shared_buffers = '256MB';
  ALTER SYSTEM SET effective_cache_size = '768MB';
  ALTER SYSTEM SET work_mem = '8MB';
  ALTER SYSTEM SET maintenance_work_mem = '64MB';
  ALTER SYSTEM SET random_page_cost = 1.1;
  ALTER SYSTEM SET effective_io_concurrency = 200;
  ALTER SYSTEM SET wal_buffers = '8MB';
  ALTER SYSTEM SET min_wal_size = '1GB';
  ALTER SYSTEM SET max_wal_size = '4GB';
" 2>/dev/null || echo "  Note: Some PG config changes require restart."

echo ""
echo "============================================"
echo " Setup complete!"
echo " Database: ${DB_NAME}"
echo " User:     ${DB_USER}"
echo "============================================"
echo ""
echo "Start the application:"
echo "  ./mvnw spring-boot:run"
echo ""
