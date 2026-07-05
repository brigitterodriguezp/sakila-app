#!/bin/bash
# ============================================================
# Sakila Rental - DEBIAN
# ============================================================
clear
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "[INFO] Starting Sakila Rental..."
echo "[INFO] Using: $(java --version 2>&1 | head -1)"

if [ -f ".env" ]; then
  echo "[INFO] Loading environment from .env"
  set -a
  # shellcheck disable=SC1091
  source ".env"
  set +a
fi

./mvnw spring-boot:run
