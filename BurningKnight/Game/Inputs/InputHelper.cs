using System.Collections.Generic;
using J = Newtonsoft.Json.JsonPropertyAttribute;

namespace BurningKnight.Game.Inputs
{
	public class InputHelper : Dictionary<string, InputHelperValue>
	{
		
	}

	public class InputHelperValue : List<string>
	{
		// public List<string> Values { get; set; }
	}
}