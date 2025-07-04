@echo off
setlocal enabledelayedexpansion

REM Leggi ogni riga da un blocco echo con "call :build" per ogni coppia
(
    echo user-management-service=Backend/user-management-service
    echo events-management-service=Backend/events-management-service
    echo eventsetup-service=Backend/eventsetup-service
    echo board-service=Backend/board-service
    echo guest-game-service=Backend/guest-game-service
    echo gallery-service=Backend/gallery-service
    echo frontend=Frontend
) > services.txt

for /f "tokens=1,2 delims==" %%A in (services.txt) do (
    set "SERVICE=%%A"
    set "DIR=%%B"

    echo.
    echo ===== Building !SERVICE! - directory: !DIR! =====
    docker build -t magicevents/!SERVICE!:latest "!DIR!"
    if errorlevel 1 (
        echo Build failed for !SERVICE!
        del services.txt
        exit /b 1
    ) else (
        echo Build completed for !SERVICE!
    )
)

del services.txt

echo.
echo All images built successfully!
