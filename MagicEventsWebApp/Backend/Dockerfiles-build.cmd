@echo off
setlocal enabledelayedexpansion

REM Define each service and its context directory
set SERVICES=user-management-service events-management-service eventsetup-service board-service guest-game-service gallery-service

for %%S in (%SERVICES%) do (
    echo.
    echo ===== Building %%S =====
    docker build -t magicevents/%%S:latest ./%%S
    IF ERRORLEVEL 1 (
        echo ❌ Build failed for %%S
        exit /b 1
    ) else (
        echo ✅ Build completed for %%S
    )
)

echo.
echo ✅ All images built successfully!
