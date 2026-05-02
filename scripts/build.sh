#!/usr/bin/env bash
set -euo pipefail

mvn clean verify javadoc:javadoc
