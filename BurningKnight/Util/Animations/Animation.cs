using System;
using System.Collections.Generic;
using System.Linq;
using BurningKnight.Assets.Graphics;
using BurningKnight.Util.Files;
using MoonSharp.Interpreter;
using Newtonsoft.Json;

namespace BurningKnight.Util.Animations
{
	[MoonSharpUserData]
	public class Animation
	{
		private Dictionary<string, List<Frame>> frames = new Dictionary<string, List<Frame>>();

		public Animation(string aname, string add = "")
		{
			if (add == null)
			{
				add = "";
			}
			
			FileHandle handle = FileHandle.FromRoot("Animations/" + aname + ".json");

			if (!handle.Exists())
			{
				Log.Error("Animation " + aname + " does not exist!");
				return;
			}

			AnimationHelper root = JsonConvert.DeserializeObject<AnimationHelper>(handle.ReadAll());

			foreach (var tag in root.Meta.FrameTags)
			{
				int from = (int) tag.From;
				int to = (int) tag.To;
				string state = tag.Name;
				List<Frame> frameList = new List<Frame>();

				for (int i = from; i <= to; i++)
				{
					var pair = root.Frames.ElementAt(i);
					FrameValue frame = pair.Value;
					
					string name = pair.Key;

					// this: actor_towelknight 0.ase -- and state dead
					// into this: actor-towelknight-dead-00

					name = name.Replace(".aseprite", "");
					name = name.Replace(".ase", "");
					name = name.Replace('_', '-');
					name = name.Replace(' ', '-');

					name = name.Substring(0, name.Length - (Char.IsDigit(name[name.Length - 2]) ? 3 : 2));
					name += add + "-" + state + "-" + (i - from).ToString("D2");
					
					frameList.Add(new Frame(Graphics.GetTexture(name), frame.Duration * 0.001f));
				}

				frames[state] = frameList;
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