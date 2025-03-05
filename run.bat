@echo off
echo Running Maven clean install...
mvn clean install
if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed. Exiting.
    exit /b %ERRORLEVEL%
)

echo Building Docker image...
docker build -t springbatchdemo .
if %ERRORLEVEL% NEQ 0 (
    echo Docker build failed. Exiting.
    exit /b %ERRORLEVEL%
)

echo Starting Docker Compose services...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo Docker Compose failed to start services. Exiting.
    exit /b %ERRORLEVEL%
)

echo Fetching logs for the batch_app service...
docker-compose logs batch_app
if %ERRORLEVEL% NEQ 0 (
    echo Failed to fetch logs. Exiting.
    exit /b %ERRORLEVEL%
)

echo Script completed successfully.
pause
