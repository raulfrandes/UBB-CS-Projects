using System.Collections.Generic;
using System.Linq;
using Bills.Domain;

namespace Bills.Repository
{
    public class InMemoryRepository<TId, TE> : IRepository<TId, TE> where TE : Entity<TId>
    {
        protected IDictionary<TId, TE> Entities = new Dictionary<TId, TE>(); 
        
        public InMemoryRepository()
        {}
        
        public IEnumerable<TE> FindAll()
        {
            return Entities.Values.ToList<TE>();
        }

        public TE Save(TE entity)
        {
            if (Entities.ContainsKey(entity.Id))
                throw new RepositoryException("Entity already exists!");
            Entities.Add(entity.Id, entity);
            return entity;
        }
    }
}