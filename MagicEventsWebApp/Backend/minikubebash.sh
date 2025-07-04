#!/bin/bash

# Define each service and its context directory
SERVICES="user-management-service events-management-service eventsetup-service board-service guest-game-service gallery-service frontend"

for SERVICE in $SERVICES; do
    echo
    echo "===== Building $SERVICE ====="
    docker build -t magicevents/$SERVICE:latest ./$SERVICE
    if [ $? -ne 0 ]; then
        echo "❌ Build failed for $SERVICE"
        exit 1
    else
        echo "✅ Build completed for $SERVICE"
    fi
done

echo
echo "✅ All images built successfully!"