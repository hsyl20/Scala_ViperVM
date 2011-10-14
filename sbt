#!/bin/bash

LD_PRELOAD=$JAVA_HOME/jre/lib/amd64/libjsig.so:$JAVA_HOME/jre/lib/amd64/server/libjsig.so sbt
