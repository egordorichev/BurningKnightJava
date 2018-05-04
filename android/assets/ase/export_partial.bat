@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b actor_towelking.ase --trim --list-tags --save-as %OUT%actor-towelking-{tag}-{tagframe00}.png --data %OUT%actor-towelking.json