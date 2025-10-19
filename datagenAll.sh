#!/bin/bash
datagenVersions=(
  1.20.1-fabric
  1.21.1-fabric
  1.21.8-fabric
)

checkStatus() {
  if [ "$1" != 0 ]; then
    exit "$1"
  fi
}

reset() {
  ./gradlew "Reset active"
}
trap reset EXIT

for o in "${datagenVersions[@]}"; do
  echo "Running datagen on $o"
  ./gradlew "Set active project to $o"; checkStatus $?
  ./gradlew "$o:runDatagen"; checkStatus $?
done
