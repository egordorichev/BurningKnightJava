#!/usr/bin/env bash

java -jar packr.jar \
     --platform linux64 \
     --jdk jdk-lin64.zip \
     --executable burningknight \
     --classpath burningknight.jar \
     --removelibs burningknight.jar \
     --mainclass "org.rexcellentgames.burningknight.desktop.DesktopLauncher" \
     --minimizejre soft \
     --output out/linux64

java -jar packr.jar \
     --platform linux32 \
     --jdk jdk-lin32.zip \
     --executable burningknight \
     --classpath burningknight.jar \
     --removelibs burningknight.jar \
     --mainclass "org.rexcellentgames.burningknight.desktop.DesktopLauncher" \
     --minimizejre soft \
     --output out/linux32