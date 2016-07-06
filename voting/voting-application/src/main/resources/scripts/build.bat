echo "Executing build.bat script..."
cd ..\..\dev-infrastructure && vagrant up && cd ..\..\pac-front-end && npm install && npm run clean && npm run build