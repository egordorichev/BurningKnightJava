using System;
using BurningKnight.Entities.Creatures;
using BurningKnight.Entities.Creatures.Enemies;
using BurningKnight.Items;
using BurningKnight.Util.Animations;
using BurningKnight.Util.Files;
using MoonSharp.Interpreter;

namespace BurningKnight.Assets.Mods
{
	public static class Api
	{
		static Api()
		{
			UserData.RegisterAssembly();
		}
		
		public static void Define(Script script)
		{			
			script.Globals["os"] = DynValue.Nil;
			script.Globals["print"] = (Func<object, int>) Print;
			script.Globals["defineItem"] = (Func<Table, string, int>) DefineItem;
			script.Globals["defineCreature"] = (Func<Table, string, Table, int>) DefineCreature;
			script.Globals["defineState"] = (Func<Table, string, Table, int>) DefineState;
			script.Globals["loadAnimation"] = (Func<string, string, Animation>) LoadAnimation;

			script.Globals["item"] = new Item();
			script.Globals["enemy"] = new Enemy();
			script.Globals["creature"] = new Creature();

			script.DoString(@"
object={}
function object:extend(kob)
	kob=kob or {}
	kob.extends=self
	return setmetatable(kob,{
		__index=self,
		__call=function(self,ob)
		ob=setmetatable(ob or {},{__index=kob})
		local ko,init_fn=kob
		while ko do
			if ko.init and ko.init~=init_fn then
			init_fn=ko.init
		init_fn(ob)
		end
			ko=ko.extends
		end
		return ob
		end
	})
end

function extend(o)
  local myobj = { _wrapped = o };
  setmetatable(myobj, {
    __index = function(t, name)
      local obj = rawget(t, '_wrapped');
      if obj then
        return obj[name];
      end
    end
  });

  return myobj
end
");
		}

		private static int Print(object o)
		{
			Log.Lua(o);
			return 0;
		}

		private static int DefineItem(Table t, string id)
		{
			if (id == null || t == null)
			{
				return 0;
			}

			ScriptedItem item = new ScriptedItem(t);
			item.LoadNames(Mod.currentId + ":" + id);
			
			ItemRegistry.Register(Mod.currentId + ":" + id, item);
			
			return 0;
		}

		private static int DefineCreature(Table t, string id, Table dt)
		{
			if (id == null || t == null)
			{
				return 0;
			}

			t["id"] = id;
			
			CreatureData data = new CreatureData();
			data.table = t;
			data.data = dt;

			CreatureRegistry.Define(Mod.currentId + ":" + id, data);
			
			return 0;
		}

		private static int DefineState(Table t, string name, Table state)
		{
			if (state == null || t == null)
			{
				return 0;
			}

			DynValue enter = state.Get("onEnter");
			DynValue exit = state.Get("onExit");
			DynValue update = state.Get("onUpdate");
			
			DynValue[] states = new DynValue[3];

			if (enter?.Function != null)
			{
				states[0] = enter;
			}
			
			if (exit?.Function != null)
			{
				states[1] = exit;
			}
			
			if (update?.Function != null)
			{
				states[2] = update;
			}
			
			CreatureRegistry.AddState(Mod.currentId + ":" + t.Get("id").String, name, states);
			
			return 0;
		}

		private static Animation LoadAnimation(string name, string add)
		{
			return new Animation( name, add);
		}
	}
}