#!/usr/bin/env bash

exec ./create_quality_gate_sonar.bash &
exec ./docker/entrypoint.sh