using System.IO;

namespace BurningKnight.Util.Files
{
	public class FileWriter
	{
		private FileStream stream;
		
		public FileWriter(string path)
		{
			stream = File.Open(path, FileMode.Truncate);
		}

		public void Close()
		{
			stream.Close();
		}

		public void WriteByte(byte val)
		{
			stream.WriteByte(val);
		}

		public void WriteShort(short val)
		{
			
		}

		public void WriteInt(int val)
		{
			
		}

		public void WriteLong(int val)
		{
			
		}
	}
}