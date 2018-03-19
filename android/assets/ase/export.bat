@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites/"

@set ENTITYFORMAT="{path}/{title} #{tag} {frame00}.png"

%ASEPRITE% -b biome_castle.ase --save-as %OUT%tiles-0.png 
%ASEPRITE% -b item.ase --save-as %OUT%item.png 

%ASEPRITE% -b actor_towelknight.ase --sheet %OUT%actor_towelknight.png 
%ASEPRITE% -b actor_towelknight.ase --sheet-pack --sheet %OUT%actor_towelknight_packed.png 