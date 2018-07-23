using BurningKnight.assets;
using Microsoft.Xna.Framework.Content.Pipeline;
using Microsoft.Xna.Framework.Content.Pipeline.Serialization.Compiler;

namespace LocalePipeline
{
	[ContentTypeWriter]
	public class LocaleWriter : ContentTypeWriter<Locale>
	{
		public override string GetRuntimeReader(TargetPlatform targetPlatform)
		{
			return "MonoGame.Extended.BitmapFonts.BitmapFontReader, MonoGame.Extended";
		}

		protected override void Write(ContentWriter output, Locale value)
		{
			throw new System.NotImplementedException();
		}
	}
	
	// todo: http://blog.dylanwilson.net/posts/2015/creating-custom-content-importers-for-the-monogame-pipeline/
}