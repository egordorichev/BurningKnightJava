@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT=""
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b fx_fireball.ase --list-tags --save-as %OUT%fx-fireball-{tag}-{tagframe00}.png --data %OUT%fx-fireball.json