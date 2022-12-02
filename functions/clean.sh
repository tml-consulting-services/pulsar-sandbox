#!/bin/bash
set -x
PWD=`pwd`

CLI_COMMAND="${PWD}/../environment/download/pulsar/bin/pulsar-admin"

$CLI_COMMAND functions delete --tenant public --namespace default --name SampleWindowFunction
