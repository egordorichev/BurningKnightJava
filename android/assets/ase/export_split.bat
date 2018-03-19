@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"

%ASEPRITE% -b biome_castle.ase --split-layers --list-layers --trim --save-as %OUT%biome-0.png --data %OUT%biome-0.json  

%ASEPRITE% -b item.ase --split-layers --list-layers --trim --save-as %OUT%item.png --data %OUT%item.json
%ASEPRITE% -b item.ase --split-layers --trim --sheet-pack --sheet %OUT%item.png --data %OUT%item-sheet.json

%ASEPRITE% -b actor_towelknight.ase --sheet %OUT%actor-towelknight.png --data %OUT%actor-towelknight-sheet.json
%ASEPRITE% -b actor_towelknight.ase --split-tags --save-as %OUT%actor-towelknight-{tag}-{tagframe00}.png --data %OUT%actor-towelknight.json