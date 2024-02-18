#!/bin/bash

set -e

exec initialize_db.sh & exec docker-entrypoint.sh $@

