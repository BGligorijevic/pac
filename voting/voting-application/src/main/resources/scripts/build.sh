#!/bin/bash
echo "Executing build.sh script..."
cd ../../dev-infrastructure && vagrant up && cd ../../pac-front-end && npm install && npm run clean && npm run build