@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b biome_castle.ase --split-layers --trim --save-as %OUT%biome-0.png

%ASEPRITE% -b item.ase --trim --save-as %OUT%item.png
%ASEPRITE% -b veggie.ase --crop 16,16,16,16 --split-layers --split-tags --list-tags --save-as %OUT%veggie-{layer}-{tag}-{tagframe00}.png --data %OUT%veggie.json

%ASEPRITE% -b actor_burning_knight.ase --trim --list-tags --save-as %OUT%actor-burning-knight-{tag}-{tagframe00}.png --data %OUT%actor-burning-knight.json
%ASEPRITE% -b actor_towelknight.ase --trim --list-tags --save-as %OUT%actor-towelknight-{tag}-{tagframe00}.png --data %OUT%actor-towelknight.json
%ASEPRITE% -b actor_towelking.ase --trim --list-tags --save-as %OUT%actor-towelking-{tag}-{tagframe00}.png --data %OUT%actor-towelking.json
%ASEPRITE% -b actor_clown.ase --trim --list-tags --save-as %OUT%actor-clown-{tag}-{tagframe00}.png --data %OUT%actor-clown.json

%ASEPRITE% -b actor_door_horizontal.ase --trim --list-tags --save-as %OUT%actor_door_horizontal-{tag}-{tagframe00}.png --data %OUT%actor_door_horizontal.json
%ASEPRITE% -b actor_door_vertical.ase --trim --list-tags --save-as %OUT%actor_door_vertical-{tag}-{tagframe00}.png --data %OUT%actor_door_vertical.json

%ASEPRITE% -b prop_throne.ase --trim --list-tags --save-as %OUT%prop-throne-{layer}-{tag}-{tagframe00}.png --data %OUT%prop-throne.json

%ASEPRITE% -b fx_plant.ase --trim --list-tags --save-as %OUT%fx-plant-{tag}-{tagframe00}.png --data %OUT%fx-plant.json
%ASEPRITE% -b fx_run.ase --trim --list-tags --save-as %OUT%fx-run-{tag}-{tagframe00}.png --data %OUT%fx-run.json
%ASEPRITE% -b fx_sword.ase --trim --list-tags --save-as %OUT%fx-sword-{tag}-{tagframe00}.png --data %OUT%fx-sword.json
%ASEPRITE% -b fx_dither.ase --trim --list-tags --save-as %OUT%fx-dither-{tag}-{tagframe00}.png --data %OUT%fx-dither.json
%ASEPRITE% -b fx_fireball.ase --trim --list-tags --save-as %OUT%fx-fireball-{tag}-{tagframe00}.png --data %OUT%fx-fireball.json

%ASEPRITE% -b bk_bars.ase --trim --save-as %OUT%bk_{layer}.png
