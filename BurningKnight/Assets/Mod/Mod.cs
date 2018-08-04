using BurningKnight.Util.Files;
using MoonSharp.Interpreter;

namespace BurningKnight.assets
{
  public class Mod
  {
    public string Description { get; private set; }
    public string Id { get; private set; }
    public string Name { get; private set; }
    private readonly DynValue drawCallback;
    private readonly Script script;

    private readonly DynValue updateCallback;

    public Mod(string name, string description, string id)
    {
      Name = name;
      Description = description;
      Id = id;

      script = new Script();
      Api.Add(script);

      script.DoString(@"
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

      DynValue update = script.Globals.Get("update");

      if (update.Function != null) updateCallback = update;

      DynValue draw = script.Globals.Get("draw");

      if (draw.Function != null) drawCallback = draw;
    }

    public void Init()
    {
      DynValue init = script.Globals.Get("init");

      if (init.Function != null)
        try
        {
          init.Function.Call();
        }
        catch (ScriptRuntimeException e)
        {
          HandleError("init()", e);
        }
    }

    public void Update(float dt)
    {
      if (updateCallback != null)
        try
        {
          updateCallback.Function.Call();
        }
        catch (ScriptRuntimeException e)
        {
          HandleError("update()", e);
        }
    }

    public void Draw()
    {
      if (drawCallback != null)
        try
        {
          drawCallback.Function.Call();
        }
        catch (ScriptRuntimeException e)
        {
          HandleError("draw()", e);
        }
    }

    public void Destroy()
    {
      DynValue destroy = script.Globals.Get("destroy");

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
      Log.Error(Id + " " + where + ": " + e.Message);
    }
  }
}