@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b biome_castle.ase --split-layers --trim --save-as %OUT%biome-0.png

%ASEPRITE% -b item.ase --split-layers --trim --save-as %OUT%item.png

%ASEPRITE% -b actor_towelknight.ase --sheet --list-tags %OUT%actor-towelknight.png --data %OUT%actor-towelknight-sheet.json
%ASEPRITE% -b actor_towelknight.ase --split-tags --list-tags --save-as %OUT%actor-towelknight-{tag}-{tagframe00}.png --data %OUT%actor-towelknight.json