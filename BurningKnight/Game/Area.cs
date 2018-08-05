using System.Collections.Generic;
using BurningKnight.Entities;

namespace BurningKnight.Game
{
	public class Area
	{
		private List<Entity> entities = new List<Entity>();
		public List<Entity> Entities => entities;

		public void Add(Entity entity)
		{
			if (entity == null || entity.done)
			{
				return;
			}
			
			entities.Add(entity);
			entity.Init();
		}

		public void Update(float dt)
		{
			for (int i = entities.Count - 1; i >= 0; i--)
			{
				Entity entity = entities[i];

				entity.CheckIfOnScreen();
				
				if (entity.AlwaysUpdate || entity.OnScreen)
				{
					entity.Update(dt);
				}

				if (entity.done)
				{
					entity.Destroy();
					entities.Remove(entity);
				}
			}
		}

		public void Draw()
		{
			entities.Sort((x, y) => x.Depth.CompareTo(y.Depth));
			
			foreach (var e in entities)
			{
				e.Draw();
			}
		}

		public void Destroy()
		{
			Clear();
		}

		public void Clear()
		{
			foreach (var e in entities)
			{
				e.done = true;
				e.Destroy();
			}
			
			entities.Clear();
		}
	}
}