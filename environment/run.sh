#!/bin/bash
set -x

cd download/pulsar

# clean
rm -rf data logs

bin/pulsar standalone
