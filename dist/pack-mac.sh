#!/usr/bin/env bash

java -jar packr.jar \
     --platform mac \
     --jdk jdk-mac.zip \
     --executable burningknight \
     --classpath burningknight.jar \
     --removelibs burningknight.jar \
     --mainclass "org.rexcellentgames.burningknight.desktop.DesktopLauncher" \
     --minimizejre soft \
     --output out/mac