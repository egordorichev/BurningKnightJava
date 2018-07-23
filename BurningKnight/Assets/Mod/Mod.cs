using BurningKnight.util.files;
using MoonSharp.Interpreter;

namespace BurningKnight.assets
{
	public class Mod
	{
		public readonly string Name;
		public readonly string Decription;
		public readonly string Id;

		private DynValue _updateCallback;
		private DynValue _drawCallback;
		private Script _script;

		public Mod(string name, string decription, string id)
		{
			Name = name;
			Decription = decription;
			Id = id;
			
			_script = new Script();
			Api.Add(_script);
			
			_script.DoString(@"
function init()
	print('Hallo!')
end		

function destroy()
	print('Bye!')
end

function update(dt)

end

function draw()

end
");

			DynValue update = _script.Globals.Get("update");

			if (update.Function != null)
			{
				_updateCallback = update;
			}
			
			DynValue draw = _script.Globals.Get("draw");

			if (draw.Function != null)
			{
				_drawCallback = draw;
			}
		}

		public void Init()
		{
			DynValue init = _script.Globals.Get("init");

			if (init.Function != null)
			{
				try
				{
					init.Function.Call();
				}
				catch (ScriptRuntimeException e)
				{
					HandleError("init()", e);
				}
 			}
		}

		public void Update(float dt)
		{
			if (_updateCallback != null)
			{
				try
				{
					_updateCallback.Function.Call();
				}
				catch (ScriptRuntimeException e)
				{
					HandleError("update()", e);
				}
			}	
		}

		public void Draw()
		{
			if (_drawCallback != null)
			{
				try
				{
					_drawCallback.Function.Call();
				}
				catch (ScriptRuntimeException e)
				{
					HandleError("draw()", e);
				}
			}
		}

		public void Destroy()
		{
			DynValue destroy = _script.Globals.Get("destroy");

			if (destroy.Function != null)
			{
				try
				{
					destroy.Function.Call();
				}
				catch (ScriptRuntimeException e)
				{
					HandleError("destroy()", e);
				}
			}
		}

		private void HandleError(string where, ScriptRuntimeException e)
		{
			Log.error(Id + " " + where + ": " + e.Message);
		}
	}
}