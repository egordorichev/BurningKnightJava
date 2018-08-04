using System;
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
			script.Globals["print"] = (Func<object, int>) Print;
			script.Globals["define_item"] = (Func<string, Table, int>) DefineItem;
			
			script.Globals["item"] = new Item();

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

		private static int DefineItem(string id, Table t)
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
	}
}