using System.Collections.Generic;
using BurningKnight.Assets.Graphics;
using BurningKnight.Util.Files;
using Newtonsoft.Json;

namespace BurningKnight.Util.Animations
{
	public class Animation
	{
		private Dictionary<string, List<Frame>> frames = new Dictionary<string, List<Frame>>();

		public Animation(string name, string add = "")
		{
			FileHandle handle = FileHandle.FromRoot("Animations/" + name + ".json");

			if (!handle.Exists())
			{
				Log.Error("Animation " + name + " does not exist!");
				return;
			}

			AnimationHelper root = JsonConvert.DeserializeObject<AnimationHelper>(handle.ReadAll());

			foreach (var tag in root.Meta.FrameTags)
			{
				int from = (int) tag.From;
				int to = (int) tag.To;
				string state = tag.Name;

				for (int i = from; i <= to; i++)
				{
					// todo
					// FrameValue frame = root.Frames.
				}
			}
		}

		public AnimationData Get(string id)
		{
			if (frames.ContainsKey(id))
			{
				return new AnimationData(frames[id]);
			}

			return null;
		}
		
		public class Frame
		{
			public TextureRegion frame;
			public float delay;
			public float initial;

			public Frame(TextureRegion frame, float delay) {
				this.frame = frame;
				this.delay = delay;
				initial = delay;
			}
		}
	}
}