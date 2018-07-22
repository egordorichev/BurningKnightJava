#!/usr/bin/env bash

java -jar packr.jar \
     --platform windows64 \
     --jdk jdk-win64.zip \
     --executable burningknight \
     --classpath burningknight.jar \
     --mainclass "org.rexcellentgames.burningknight.desktop.DesktopLauncher" \
     --minimizejre soft \
     --removelibs burningknight.jar \
     --output out/windows64

java -jar packr.jar \
     --platform windows32 \
     --jdk jdk-win32.zip \
     --executable burningknight \
     --classpath burningknight.jar \
     --mainclass "org.rexcellentgames.burningknight.desktop.DesktopLauncher" \
     --removelibs burningknight.jar \
     --minimizejre soft \
     --output out/windows32
