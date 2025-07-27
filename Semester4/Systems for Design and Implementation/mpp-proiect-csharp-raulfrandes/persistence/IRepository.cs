using System.Collections.Generic;

namespace persistence;

public interface IRepository<TE, T>
{
    IEnumerable<TE> FindAll();
    TE? FindById(T entityId);
    TE? Save(TE entity);
    TE DeleteById(T entityId);
    TE Update(TE entity);
}
