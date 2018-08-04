using System.IO;
using BurningKnight.Assets;

namespace BurningKnight.Util.Files
{
	public class FileHandle
	{
		private string path;
		public string FullPath => Path.GetFullPath(path);
		public string NameWithoutExtension => Path.GetFileNameWithoutExtension(path);
		public string Name => Path.GetFileName(path);
		public string Extension => Path.GetExtension(path);

		public FileHandle(string path)
		{
			this.path = path;
		}

		public static FileHandle FromRoot(string path)
		{
			return new FileHandle(AssetsHelper.content.RootDirectory + path);
		}

		public void MakeDirectory()
		{
			Directory.CreateDirectory(path);
		}

		public void MakeFile()
		{
			File.Create(path);
		}

		public string ReadAll()
		{
			return File.ReadAllText(path);
		}

		public void Delete()
		{
			if (IsDirectory())
			{
				Directory.Delete(path);				
			}
			else
			{
				File.Delete(path);				
			}
		}

		public FileHandle FindFile(string name)
		{
			name = Path.GetFileName(name);
			
			string[] names = ListFiles();

			foreach (var id in names)
			{
				if (Path.GetFileName(id) == name)
				{
					return new FileHandle(id);
				}
			}

			return null;
		}

		public FileHandle FindDirectory(string name)
		{
			name = Path.GetFileName(name);
			
			string[] names = ListDirectories();

			foreach (var id in names)
			{
				if (Path.GetFileName(id) == name)
				{
					return new FileHandle(id);
				}
			}

			return null;
		}
		
		public string[] ListFiles()
		{
			return Directory.GetFiles(path);
		}

		public string[] ListDirectories()
		{
			return Directory.GetDirectories(path);
		}

		public FileHandle[] ListFileHandles()
		{
			return List(ListFiles());
		}

		public FileHandle[] ListDirectoryHandles()
		{
			return List(ListDirectories());
		}

		protected FileHandle[] List(string[] names)
		{
			FileHandle[] handles = new FileHandle[names.Length];

			for (int i = 0; i < names.Length; i++)
			{
				handles[i] = new FileHandle(names[i]);
			}
			
			return handles;
		}

		public bool Exists()
		{
			return IsDirectory() ? Directory.Exists(path) : File.Exists(path);
		}

		public bool IsDirectory()
		{
			return File.GetAttributes(path).HasFlag(FileAttributes.Directory);
		}
	}
}