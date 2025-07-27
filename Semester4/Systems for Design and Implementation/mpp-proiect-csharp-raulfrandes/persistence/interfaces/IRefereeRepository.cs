using model;

namespace persistence.interfaces;

public interface IRefereeRepository: IRepository<Referee, long>
{
    Referee? FindByUsername(string username);
}