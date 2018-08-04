using System.IO;
using BurningKnight.assets;
using Microsoft.Xna.Framework.Content.Pipeline;
using Newtonsoft.Json;

namespace LocalePipeline
{
	[ContentImporter(".fnt", DefaultProcessor = "LocaleProcessor", 
		DisplayName = "Locale importer")]
	public class LocaleImporter : ContentImporter<Locale>
	{
		public override Locale Import(string filename, ContentImporterContext context)
		{
			context.Logger.LogMessage("Importing JSON file: {0}", filename);

			using (var streamReader = new StreamReader(filename))
			{
				return JsonConvert.DeserializeObject<Locale>(streamReader.ReadToEnd());
			}
		}
	}
}