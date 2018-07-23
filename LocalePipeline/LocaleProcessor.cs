using BurningKnight.assets;
using Microsoft.Xna.Framework.Content.Pipeline;

namespace LocalePipeline
{
	[ContentProcessor(DisplayName = "Locale Processor")]
	public class LocaleProcessor : ContentProcessor<Locale, Locale>
	{
		public override Locale Process(Locale input, ContentProcessorContext context)
		{
			return input;
		}
	}
}