using System;
using System.Collections.Generic;
using System.IO;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.Assets.Graphics
{
	public class TextureAtlas
	{
		private Dictionary<string, TextureRegion> regions = new Dictionary<string, TextureRegion>();
		public Texture2D texture;
		
		public void Load(FileHandle handle)
		{
			string name = null;
			int w = 0;
			int h = 0;
			int x = 0;
			int y = 0;
			bool first = true;
			TextureRegion region = null;
			
			foreach (var line in handle.ReadAll().Split("\r\n".ToCharArray(), StringSplitOptions.RemoveEmptyEntries))
			{
				if (line.StartsWith("  "))
				{
					if (line.Contains("xy"))
					{
						var l = line.Replace("  xy: ", "");
						string[] values = l.Split(", ".ToCharArray());
						
						region = new TextureRegion();
						region.texture = texture;
						region.source.X = Int32.Parse(values[0]);
						region.source.Y = Int32.Parse(values[2]);
					} 
					else if (line.Contains("size"))
					{
						var l = line.Replace("  size: ", "");
						string[] values = l.Split(", ".ToCharArray());
						
						region.source.Width = Int32.Parse(values[0]);
						region.source.Height = Int32.Parse(values[2]);

						regions[name] = region;
						region = null;
					}
				} 
				else if (first)
				{
					first = false;
					FileStream setStream = File.Open(AssetsHelper.content.RootDirectory + "Atlas/" + line, FileMode.Open);
					texture = Texture2D.FromStream(Graphics.batch.GraphicsDevice, setStream);
					setStream.Dispose();
				}
				else if (!line.Contains(":"))
				{
					name = line;
				}
			}				
		}

		public TextureRegion Get(string id)
		{
			if (regions.ContainsKey(id))
			{
				return regions[id];
			}

			Log.Error("Texture '" + id + "' is missing!");
			
			return null; // todo: return tmp texture
		}

		public void Destroy()
		{
			texture.Dispose();
		}
	}
}