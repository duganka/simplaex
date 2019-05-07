# Instructions

## Compile
sbt compile

## Test
sbt test

## Run
Supported command line arguments:<br />
  -g, --group-size  <arg><br />
  -p, --port  <arg><br />
  -h, --help                Show help message<br />

### run with defaults:
sbt run

### run with custom arguments:
sbt "run -p 9001 -g 42"