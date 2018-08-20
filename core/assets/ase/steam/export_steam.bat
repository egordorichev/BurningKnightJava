@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"

%ASEPRITE% -b avatar.ase --save-as export/avatar_{frame000}.png

%ASEPRITE% -b background.ase --scale 2 --save-as export/background.png
%ASEPRITE% -b capsule_lg.ase --save-as export/capsule_lg.png
%ASEPRITE% -b capsule_main.ase --save-as export/capsule_main.png
%ASEPRITE% -b capsule_sm.ase --save-as export/capsule_sm.png
%ASEPRITE% -b header.ase --save-as export/header.png
%ASEPRITE% -b header_community.ase --save-as export/header_community.png

%ASEPRITE% -b icon.ase --scale 1 --save-as export/icon_96.png
%ASEPRITE% -b icon.ase --scale 0.75 --save-as export/icon_64.png
%ASEPRITE% -b icon.ase --scale 0.5 --save-as export/icon_48.png
%ASEPRITE% -b icon.ase --scale 0.333 --save-as export/icon_32.png
%ASEPRITE% -b icon.ase --scale 0.25 --save-as export/icon_24.png
%ASEPRITE% -b icon.ase --scale 0.0625 --save-as export/icon_16.png