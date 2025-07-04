#!/bin/bash

# Define services and their build contexts in SERVICE=CONTEXT format
SERVICE_MAPPINGS=(
    "user-management-service=Backend/user-management-service"
    "events-management-service=Backend/events-management-service"
    "eventsetup-service=Backend/eventsetup-service"
    "board-service=Backend/board-service"
    "guest-game-service=Backend/guest-game-service"
    "gallery-service=Backend/gallery-service"
    "frontend=Frontend"
)

# Loop through each service mapping
for entry in "${SERVICE_MAPPINGS[@]}"; do
    SERVICE="${entry%%=*}"
    CONTEXT="${entry#*=}"

    echo
    echo "===== Building $SERVICE - context: $CONTEXT ====="
    docker build -t "magicevents/$SERVICE:latest" "$CONTEXT"
    if [ $? -ne 0 ]; then
        echo "❌ Build failed for $SERVICE"
        exit 1
    else
        echo "✅ Build completed for $SERVICE"
    fi
done

echo
echo "✅ All images built successfully!"
