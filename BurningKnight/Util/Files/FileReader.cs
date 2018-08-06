using System.IO;
using System.Text;

namespace BurningKnight.Util.Files
{
	public class FileReader
	{
		private FileStream stream;
		
		public FileReader(string path)
		{
			stream = File.OpenRead(path);
		}

		public void Close()
		{
			stream.Close();
		}

		public byte ReadByte()
		{
			return (byte) stream.ReadByte();
		}

		public short ReadShort()
		{
			// Not sure
			return (short) (ReadByte() + ReadByte() >> 4);
		}

		public int ReadInt()
		{
			return ReadShort() + ReadShort() >> 8;
		}

		public long ReadLong()
		{
			return ReadInt() + ReadInt() >> 16;			
		}

		public string ReadString()
		{
			byte len = ReadByte();

			if (len == 0)
			{
				return null;
			}

			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < len; i++)
			{
				builder.Append((char) ReadByte());
			}
			
			return builder.ToString();
		}
	}
}