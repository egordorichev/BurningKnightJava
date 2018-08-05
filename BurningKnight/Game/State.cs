﻿namespace BurningKnight.Game
{
	public class State
	{
		public bool paused;
		public Area area;
		public Area ui;
		public static InGameState InGame = new InGameState();
		
		public virtual void Init()
		{
			area = new Area();
			ui = new Area();
		}

		public virtual void Destroy()
		{
			area.Destroy();
			ui.Destroy();
		}

		public virtual void Update(float dt)
		{
			
		}

		public virtual void Draw()
		{
			
		}

		public virtual void DrawUi()
		{
			
		}
	}
}