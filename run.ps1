<# ============================================================
   Sakila Rental - Run Script (Windows PowerShell)
   ============================================================ #>
Write-Host "[INFO] Starting Sakila Rental..." -ForegroundColor Green

$javaVersion = java -version 2>&1
Write-Host "[INFO] Using: $javaVersion" -ForegroundColor Green

.\mvnw.cmd spring-boot:run
