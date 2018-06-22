#!/usr/bin/env bash

java -jar packr.jar \
     --platform linux64 \
     --jdk jdk-lin.zip \
     --executable burningknight \
     --classpath burningknight.jar \
     --removelibs burningknight.jar \
     --mainclass "org.rexcellentgames.burningknight.desktop.DesktopLauncher" \
     --minimizejre soft \
     --output out/linux64