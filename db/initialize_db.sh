#!/bin/bash

set -e

try_to_connect_to_mongo_db() {
    mongosh --eval "db.adminCommand('ping')" >/dev/null 2>&1
}

try_to_check_replica_status() {
    mongosh --eval "rs.status()" >/dev/null 2>&1
}

RETRY_COUNT=10
INTERVAL=1

counter=1
while [ $counter -le $RETRY_COUNT ]; do
    echo "Wait for MongoDB $counter/$RETRY_COUNT"
    if try_to_connect_to_mongo_db; then
        echo "MongoDB is ready"
        if try_to_check_replica_status; then
            echo "Replica set is already initialized"
        else
            echo "Initializing replica set and data"
            mongosh /after-run-scripts/replica-init.js
            mongosh /after-run-scripts/mongo-init.js
        fi
        exit 0
    fi
    sleep $INTERVAL
    counter=$((counter + 1))
done
echo "MongoDB was not available after $((RETRY_COUNT * INTERVAL)) seconds"
exit 1;
