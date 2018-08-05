﻿using BurningKnight.Util.Animations;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.Assets.Graphics
{
	public class Graphics : AssetManager
	{
		public static SpriteBatch batch;
		public static TextureAtlas atlas;

		private static Color color = Color.White;

		public static Color Color
		{
			get => color;
			set => color = value;
		}

		/*
		 * Asset loading
		 */
		
		public override void LoadAssets()
		{
			base.LoadAssets();
			
			atlas = new TextureAtlas();
			atlas.Load(FileHandle.FromRoot("Atlas/atlas.atlas"));

			Animation animation = new Animation("actor-mummy", "-gray");
			anim = animation.Get("idle");
		}

		public static TextureRegion GetTexture(string id)
		{
			return atlas.Get(id);
		}
		
		private static AnimationData anim;
		
		/*
		 * Static methods
		 */

		public static void Clear(Color color)
		{
			batch.GraphicsDevice.Clear(color);
			batch.Begin();
			anim.Update(0.01f);
			anim.Draw(Vector2.Zero);
			batch.End();
		}

		public static void Draw(TextureRegion region, Vector2 position)
		{
			batch.Draw(region.texture, position, region.source, color);
		}
	}
}