@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT=""
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b bk_bars.ase --trim --save-as %OUT%bk-{layer}.png