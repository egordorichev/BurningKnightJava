@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"

%ASEPRITE% -b biome_castle.ase --save-as %OUT%biome-0-{slice}.png

%ASEPRITE% -b biome_gen.ase --save-as %OUT%biome-gen-{slice}.png
%ASEPRITE% -b biome_0.ase --save-as %OUT%biome-0-{slice}.png