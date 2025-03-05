#!/bin/bash

# Log file
LOGFILE="run.log"

# Function to log and execute a command
log_and_run() {
    echo "Running: $1" | tee -a "$LOGFILE"
    eval "$1" | tee -a "$LOGFILE"
    if [ $? -ne 0 ]; then
        echo "Error: Command failed - $1" | tee -a "$LOGFILE"
        exit 1
    fi
}

# Clear the log file
> "$LOGFILE"

# Commands
log_and_run "mvn clean install"
log_and_run "docker build -t springbatchdemo ."
log_and_run "docker-compose up -d"
log_and_run "docker-compose logs batch_app"

echo "All commands executed successfully!" | tee -a "$LOGFILE"
