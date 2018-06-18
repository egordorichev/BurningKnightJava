@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b fx_plant.ase --trim --list-tags --save-as %OUT%fx-plant-{tag}-{tagframe00}.png --data %OUT%fx-plant.json
%ASEPRITE% -b fx_run.ase --trim --list-tags --save-as %OUT%fx-run-{tag}-{tagframe00}.png --data %OUT%fx-run.json
%ASEPRITE% -b fx_sword.ase --trim --list-tags --save-as %OUT%fx-sword-{tag}-{tagframe00}.png --data %OUT%fx-sword.json
%ASEPRITE% -b fx_dither.ase --trim --list-tags --save-as %OUT%fx-dither-{tag}-{tagframe00}.png --data %OUT%fx-dither.json
%ASEPRITE% -b fx_fireball.ase --trim --list-tags --save-as %OUT%fx-fireball-{tag}-{tagframe00}.png --data %OUT%fx-fireball.json
%ASEPRITE% -b fx_blood.ase --trim --list-tags --save-as %OUT%fx-blood-{tag}-{tagframe00}.png --data %OUT%fx-blood.json

%ASEPRITE% -b fx_bloodsplat.ase --trim --save-as %OUT%fx-bloodsplat-{frame00}.png --data

%ASEPRITE% -b bullet.ase --trim --save-as %OUT%bullet ({layer}).png --data %OUT%bullet.json
