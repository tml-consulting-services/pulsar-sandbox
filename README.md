# Pulsar Sandbox

Vertsion: `2.10.2`
## Environment

Note: Please use `JDK 11 amd64`.

1. Run `setup.sh` from `environment` to download and configure Pulsar .
2. Run `run.sh` from `environment` to start Pulsar .

## Deployment

1. Run `deploy.sh` from `functions`.
2. Check `SampleWindowFunction` function logs with `tail -f logs/functions/public/default/SampleWindowFunction/SampleWindowFunction-0.log` from `environment/download/pulsar`.