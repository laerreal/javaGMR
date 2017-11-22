#!/bin/bash

SCRIPTPATH=$( cd "$(dirname "$0")" ; pwd -P )

cd "$SCRIPTPATH/CWD"

java -jar ../gmr.jar &>> gmr.log

