using System.Collections.Generic;
using Bills.Domain;

namespace Bills.Repository
{
    public interface IRepository<TId, TE> where TE : Entity<TId>
    {
        IEnumerable<TE> FindAll();
    }
}