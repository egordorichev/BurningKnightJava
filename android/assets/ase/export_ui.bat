@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b ui_bkbar.ase --trim --save-as %OUT%ui-bkbar-{layer}.png
%ASEPRITE% -b ui_bkbar_flame.ase --trim --save-as %OUT%ui-bkbar-flame-{frame00}.png