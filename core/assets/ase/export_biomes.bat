@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"

%ASEPRITE% -b biome_gen.ase --save-as %OUT%biome-gen-{slice}.png
%ASEPRITE% -b biome_0.ase --save-as %OUT%biome-0-{slice}.png
%ASEPRITE% -b biome_1.ase --save-as %OUT%biome-1-{slice}.png
%ASEPRITE% -b biome_2.ase --save-as %OUT%biome-2-{slice}.png
%ASEPRITE% -b biome_3.ase --save-as %OUT%biome-3-{slice}.png
%ASEPRITE% -b biome_4.ase --save-as %OUT%biome-4-{slice}.png
%ASEPRITE% -b biome_5.ase --save-as %OUT%biome-5-{slice}.png
%ASEPRITE% -b biome_6.ase --save-as %OUT%biome-6-{slice}.png