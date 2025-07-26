#nullable enable
using System.IO;
using Bills.Domain;

namespace Bills.Repository
{
    public abstract class InFileRepository<TId, TE> : InMemoryRepository<TId, TE> where TE : Entity<TId>
    {
        private string _filename;

        public InFileRepository(string filename)
        {
            _filename = filename;
            ReadFromFile();
        }

        protected abstract TE EntityFromString(string? data);

        protected void ReadFromFile()
        {
            if (_filename == null)
            {
                throw new RepositoryException("Invalid parameter!");
            }
            StreamReader streamReader = new StreamReader(_filename);
            string? data;
            while (true)
            {
                data = streamReader.ReadLine();
                if (data == null)
                    break;
                Save(EntityFromString(data));
            }
        }
    }
}