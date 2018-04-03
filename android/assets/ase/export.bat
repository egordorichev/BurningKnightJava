@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b biome_castle.ase --split-layers --trim --save-as %OUT%biome-0.png

%ASEPRITE% -b item.ase --split-layers --trim --save-as %OUT%item.png
%ASEPRITE% -b veggie.ase  --crop 16,16,16,16 --split-layers --split-tags --list-tags --save-as %OUT%veggie-{layer}-{tag}-{tagframe00}.png --data %OUT%veggie.json

%ASEPRITE% -b actor_gobbo.ase --split-tags --list-tags --save-as %OUT%actor-gobbo-{tag}-{tagframe00}.png --data %OUT%actor-gobbo.json
%ASEPRITE% -b actor_burning_knight.ase --split-tags --list-tags --save-as %OUT%actor_burning_knight-{tag}-{tagframe00}.png --data %OUT%actor_burning_knight.json
%ASEPRITE% -b actor_towelknight.ase --split-tags --list-tags --save-as %OUT%actor-towelknight-{tag}-{tagframe00}.png --data %OUT%actor-towelknight.json
%ASEPRITE% -b actor_towelking.ase --split-tags --list-tags --save-as %OUT%actor-towelking-{tag}-{tagframe00}.png --data %OUT%actor-towelking.json
%ASEPRITE% -b actor_clown.ase --split-tags --list-tags --save-as %OUT%actor-clown-{tag}-{tagframe00}.png --data %OUT%actor-clown.json

%ASEPRITE% -b prop_throne.ase --trim --list-tags --save-as %OUT%prop-throne-{layer}-{tag}-{tagframe00}.png --data %OUT%prop-throne.json

%ASEPRITE% -b fx_plant.ase --list-tags --save-as %OUT%fx-plant-{tag}-{tagframe00}.png --data %OUT%fx-plant.json
%ASEPRITE% -b fx_run.ase --list-tags --save-as %OUT%fx-run-{tag}-{tagframe00}.png --data %OUT%fx-run.json
%ASEPRITE% -b fx_sword.ase --list-tags --save-as %OUT%fx-sword-{tag}-{tagframe00}.png --data %OUT%fx-sword.json
%ASEPRITE% -b fx_dither.ase --list-tags --save-as %OUT%fx-dither-{tag}-{tagframe00}.png --data %OUT%fx-dither.json