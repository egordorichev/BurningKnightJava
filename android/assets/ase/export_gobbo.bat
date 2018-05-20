@set ASEPRITE="D:\GAMES\STEAM\steamapps\common\Aseprite\aseprite.exe"
@set OUT="../sprites_split/"
@set BFORMAT="{path}/{title} {layer}.{extension}"

%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/gobbo" --trim --list-tags --save-as %OUT%actor-gobbo-{tag}-{tagframe00}.png --data %OUT%actor-gobbo.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/shroom" --trim --list-tags --save-as %OUT%actor-gobbo-shroom-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-shroom.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/towelknight" --trim --list-tags --save-as %OUT%actor-gobbo-towelknight-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-towelknight.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/fungi" --trim --list-tags --save-as %OUT%actor-gobbo-fungi-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-fungi.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/ushanka" --trim --list-tags --save-as %OUT%actor-gobbo-ushanka-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-ushanka.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/rave" --trim --list-tags --save-as %OUT%actor-gobbo-rave-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-rave.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/dunce" --trim --list-tags --save-as %OUT%actor-gobbo-dunce-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-dunce.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/coboi" --trim --list-tags --save-as %OUT%actor-gobbo-coboi-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-coboi.json

%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/skull" --trim --list-tags --save-as %OUT%actor-gobbo-skull-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-skull.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/valkyre" --trim --list-tags --save-as %OUT%actor-gobbo-valkyre-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-valkyre.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/gold" --trim --list-tags --save-as %OUT%actor-gobbo-gold-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-gold.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/ruby" --trim --list-tags --save-as %OUT%actor-gobbo-ruby-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-ruby.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/viking" --trim --list-tags --save-as %OUT%actor-gobbo-viking-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-viking.json
%ASEPRITE% -b actor_gobbo.ase --layer "body" --layer "hat/moai" --trim --list-tags --save-as %OUT%actor-gobbo-moai-{tag}-{tagframe00}.png --data %OUT%actor-gobbo-moai.json