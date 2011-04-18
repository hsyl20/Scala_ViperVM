#!/bin/bash

export LD_PRELOAD=/opt/java/jre/lib/amd64/libjsig.so:/opt/java/jre/lib/amd64/server/libjsig.so
sbt
