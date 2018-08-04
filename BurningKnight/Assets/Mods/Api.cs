using System;
using BurningKnight.Entities.Creatures.Enemies;
using BurningKnight.Items;
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
			script.Globals["define_item"] = (Func<Table, string, int>) DefineItem;
			script.Globals["define_enemy"] = (Func<Table, string, int>) DefineEnemy;
			script.Globals["define_state"] = (Func<Table, Table, int>) DefineState;
			
			script.Globals["item"] = new Item();
			script.Globals["enemy"] = new Enemy();

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
			item.LoadNames(id);
			
			ItemRegistry.Register(id, item);
			
			return 0;
		}

		private static int DefineEnemy(Table t, string id)
		{
			if (id == null || t == null)
			{
				return 0;
			}

			
			return 0;
		}

		private static int DefineState(Table t, Table state)
		{
			if (state == null || t == null)
			{
				return 0;
			}

			return 0;
		}
	}
}