#!/bin/bash
set -x

PULSAR_VERSION=2.10.2
DOWNLOADS_DIR=download

mkdir -p ${DOWNLOADS_DIR}
rm -rf ${DOWNLOADS_DIR}/*

# Download Pulsar
curl -L "https://archive.apache.org/dist/pulsar/pulsar-${PULSAR_VERSION}/apache-pulsar-${PULSAR_VERSION}-bin.tar.gz" --output ${DOWNLOADS_DIR}/pulsar.tar.gz
tar -xvf ${DOWNLOADS_DIR}/pulsar.tar.gz -C ${DOWNLOADS_DIR}
mkdir -p ${DOWNLOADS_DIR}/pulsar
cp -a ${DOWNLOADS_DIR}/apache-pulsar-${PULSAR_VERSION}/* ${DOWNLOADS_DIR}/pulsar
rm -rf ${DOWNLOADS_DIR}/apache-pulsar-${PULSAR_VERSION}
