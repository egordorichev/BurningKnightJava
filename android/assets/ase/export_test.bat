@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT=""
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b veggie.ase  --crop 16,16,16,16 --split-layers --split-tags --list-tags --save-as %OUT%veggie-{layer}-{tag}-{tagframe00}.png --data %OUT%veggie.json