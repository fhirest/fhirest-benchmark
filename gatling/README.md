
## Test scenario 1
```
1. Search for patient by name
2. Search for practitioner
3. Search for patients medical data (Encounter, Observation, MedicationRequest, Claim, Immunization), total 5 requests
4. Create new Immunization
5. Load created immunization
6. Update immunization
7. Search for all patients Immunizations again
```

## Configure
* gradle.properties
```
timeFactor = 1 ## test duration in minutes
userFactor = 700 ## number of scenarios to run, split evenly during `timeFactor` 
fhirUrl = http://localhost:8181/fhir
```
* src/gatling/resources/data/immunization.json
```
immunization resource template
```
* src/gatling/resources/data/patients.csv
```
scv with patients data on the server. needed to run searches and reference
```

## Running load tests
`./gradlew clean gatlingRun`
