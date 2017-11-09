#!/usr/bin/env bash
set -e

FRAMEWORK_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_DIR=$FRAMEWORK_DIR/build/distributions

# grab TEMPLATE_x vars for use in universe template:
export TEMPLATE_ELASTIC_VERSION="5.6.3"
export TEMPLATE_SUPPORT_DIAGNOSTICS_VERSION="6.2"

$FRAMEWORK_DIR/../../tools/build_framework.sh \
    beta-elastic \
    $FRAMEWORK_DIR \
    --artifact "$BUILD_DIR/executor.zip" \
    --artifact "$BUILD_DIR/$(basename $FRAMEWORK_DIR)-scheduler.zip" \
    $@
