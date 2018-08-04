using System.Collections.Generic;
using BurningKnight.Assets.Graphics;
using Microsoft.Xna.Framework;

namespace BurningKnight.Util.Animations
{
	public class AnimationData
	{
		public List<Animation.Frame> frames;
		public float t;
		public int index;
		public bool pause;
		public bool back;
		public Animation.Frame current;
		public bool auto;
		public Listener listener;

		public AnimationData(List<Animation.Frame> frames)
		{
			this.frames = frames;
		}
		
		public bool Update(float dt) {
			bool val = false;

			if (!pause) {
				t += dt;

				if (t >= current.delay) {
					index += (back ? -1 : 1);
					t = 0;

					if ((!back && index >= frames.Count) || (back && index < 0)) {
						listener?.onEnd();

						index = (back ? frames.Count - 1 : 0);
						val = true;

						if (auto) {
							index = (back ? 0 : frames.Count - 1);
							pause = true;
						}
					}

					listener?.onFrame(index);
					current = frames[index];
				}
			}

			return val;
		}

		public void Draw(Vector2 pos) {
			Graphics.Draw(current.frame, pos);
		}
		
		public class Listener
		{
			public virtual void onFrame(int frame) {

			}

			public virtual void onEnd() {

			}
		}
	}
}