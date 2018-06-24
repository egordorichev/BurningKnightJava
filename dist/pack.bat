cd ..

call gradlew desktop:dist

xcopy "desktop\build\libs\desktop-1.0.jar" "dist\burningknight.jar" /y

cd dist

pack-win.bat