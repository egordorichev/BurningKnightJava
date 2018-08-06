using System;
using System.IO;

namespace BurningKnight.Util.Files
{
	public class FileWriter
	{
		private FileStream stream;
		
		public FileWriter(string path)
		{
			stream = File.Open(path, FileMode.Truncate);

			if (stream == null)
			{
				throw new Exception("File not found");
			}
		}
		
		public FileWriter(FileHandle file)
		{
			stream = File.Open(file.FullPath, FileMode.Truncate);

			if (stream == null)
			{
				throw new Exception("File not found");
			}
		}

		public void Close()
		{
			stream.Close();
		}

		public void WriteString(string val)
		{
			WriteByte((byte) val.Length);

			for (int i = 0; i < val.Length; i++)
			{
				WriteByte((byte) val[i]);
			}
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