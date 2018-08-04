using System;
using BurningKnight.Util.Files;
using MoonSharp.Interpreter;

namespace BurningKnight.Assets.Mods
{
	public class Mod
	{
		private string id;
		private Script script;
		public string Id => id;

		public Mod(FileHandle dir)
		{
			id = dir.Name;
			
			script = new Script();
			Api.Define(script);
			
			FileHandle main = dir.FindFile("main.lua");

			if (main == null)
			{
				Log.Warn("Did not find main.lua!");
			}
			else
			{
				ParseMain(main);
			}

			ParseDirectory(dir);
		}

		private void ParseDirectory(FileHandle dir)
		{
			foreach (var sub in dir.ListDirectoryHandles())
			{
				ParseDirectory(sub);
			}
			
			foreach (var sub in dir.ListFileHandles())
			{
				ParseFile(sub);
			}
		}

		private void ParseFile(FileHandle file)
		{
			if (file.Extension != ".lua")
			{
				return;
			}

			try
			{
				script.DoString(file.ReadAll());
			}
			catch (Exception e)
			{
				LogError(file.Name, e);
			}
		}

		private void LogError(string where, Exception e)
		{
			Log.Error("runtime error" + (where == "" ? "" : " in " + where) + ": " + e.Message);
		}

		private DynValue updateCallback;
		private DynValue drawCallback;
		
		private void ParseMain(FileHandle main)
		{
			script.DoString(main.ReadAll());
			DynValue updateCallback = script.Globals.Get("update");

			if (updateCallback?.Function != null)
			{
				this.updateCallback = updateCallback;
			}
			
			DynValue drawCallback = script.Globals.Get("draw");

			if (drawCallback?.Function != null)
			{
				this.drawCallback = drawCallback;
			}
		}
		
		public void Init()
		{
			DynValue initCallback = script.Globals.Get("init");

			initCallback?.Function?.Call();
		}

		public void Destroy()
		{
			DynValue destroyCallback = script.Globals.Get("destroy");

			destroyCallback?.Function?.Call();
		}
		
		public void Update(float dt)
		{
			updateCallback?.Function.Call(DynValue.NewNumber(dt));
		}

		public void Draw()
		{
			drawCallback?.Function.Call();
		}
	}
}