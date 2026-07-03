#!/bin/bash
# ============================================================
# Sakila Rental - Run Script (Linux/macOS)
# ============================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "[INFO] Starting Sakila Rental..."
echo "[INFO] Using: $(java --version 2>&1 | head -1)"

./mvnw spring-boot:run
