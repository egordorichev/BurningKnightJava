#!/usr/bin/env bash

cd ..
./gradlew desktop:dist
mv ./desktop/build/libs/desktop-1.0.jar ./dist/burningknight.jar
cd dist

sh ./pack-win.sh
sh ./pack-lin.sh
sh ./pack-mac.sh