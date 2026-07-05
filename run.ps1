<# ============================================================
   Sakila Rental - WINDOWS PWSHELL
   ============================================================ #>
Write-Host "[INFO] Starting Sakila Rental..." -ForegroundColor Green

$javaVersion = java -version 2>&1
Write-Host "[INFO] Using: $javaVersion" -ForegroundColor Green

if (Test-Path ".env") {
    Write-Host "[INFO] Loading environment from .env" -ForegroundColor Green
    Get-Content ".env" | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#")) {
            $parts = $line -split "=", 2
            if ($parts.Count -eq 2) {
                [Environment]::SetEnvironmentVariable($parts[0].Trim(), $parts[1].Trim(), "Process")
            }
        }
    }
}

.\mvnw.cmd spring-boot:run
