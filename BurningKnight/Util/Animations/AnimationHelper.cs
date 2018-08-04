using System.Collections.Generic;
using J = Newtonsoft.Json.JsonPropertyAttribute;
using R = Newtonsoft.Json.Required;
using N = Newtonsoft.Json.NullValueHandling;

namespace BurningKnight.Util.Animations
{
	public class AnimationHelper
	{
		[J("frames")] public Dictionary<string, FrameValue> Frames { get; set; }
		[J("meta")]   public Meta Meta { get; set; }                            
	}

	public class FrameValue
	{
		[J("frame")]            public SpriteSourceSizeClass Frame { get; set; }           
		[J("trimmed")]          public bool Trimmed { get; set; }                          
		[J("spriteSourceSize")] public SpriteSourceSizeClass SpriteSourceSize { get; set; }
		[J("sourceSize")]       public Size SourceSize { get; set; }                       
		[J("duration")]         public long Duration { get; set; }                         
	}

	public class SpriteSourceSizeClass
	{
		[J("x")] public long X { get; set; }
		[J("y")] public long Y { get; set; }
		[J("w")] public long W { get; set; }
		[J("h")] public long H { get; set; }
	}

	public class Size
	{
		[J("w")] public long W { get; set; }
		[J("h")] public long H { get; set; }
	}

	public class Meta
	{
		[J("frameTags")]                                          public List<FrameTag> FrameTags { get; set; }
	}

	public class FrameTag
	{
		[J("name")]      public string Name { get; set; }     
		[J("from")]      public long From { get; set; }       
		[J("to")]        public long To { get; set; }         
	}
}