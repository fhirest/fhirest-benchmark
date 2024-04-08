#!/bin/bash
cd `dirname $0`

fhir=http://localhost:8181
o=`pwd`/output
p=5

function main() {
  rm -rf output && mkdir output
  upload data/meta
  upload data/patients
}

function upload() {
  cd $1
  for f in *; do
    ((i=i%p)); ((i++==0)) && wait
    curl -XPOST $fhir/fhir -H"Content-Type: application/json" -d"@$f" -o"$o/$f.log" -w "$f %{http_code} %{time_total}\n" -s &
  done
  wait
  cd -
}

main
