#!/bin/sh

# launch.sh
# launcher for machine learning tests

if [ ! -d ./logs ]; then
	mkdir logs
fi
if [ -d target/build]; then
	java -cp target/build:lib/ogapi-core.jar ori.ml.Main
else
	echo 'run ant compile first'
fi
