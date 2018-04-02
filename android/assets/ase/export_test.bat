@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT=""
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b prop_throne.ase --trim --list-tags --save-as %OUT%prop-throne-{layer}-{tag}-{tagframe00}.png --data %OUT%prop-throne.json