to generate test data, one can use synthea, for example
https://github.com/synthetichealth/synthea
```
exporter.fhir.use_us_core_ig false
./run_synthea -s 10 -p 1000
```
see `upload.sh` to push generated data


