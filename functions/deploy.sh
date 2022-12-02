#!/bin/bash
set -x
PWD=`pwd`

CLI_COMMAND="${PWD}/../environment/download/pulsar/bin/pulsar-admin"
FUNCTIONS_PATH="file:${PWD}"
CONFIG_PATH="${PWD}"

cd watermark-window-pulsar-function
mvn clean package

$CLI_COMMAND functions create \
  --jar ${FUNCTIONS_PATH}/watermark-window-pulsar-function/target/watermark-window-pulsar-function-1.0-SNAPSHOT.jar \
  --function-config-file ${CONFIG_PATH}/watermark-window-pulsar-function/target/classes/sample-window-function-config.yaml
