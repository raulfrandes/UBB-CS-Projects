using System.Collections.Generic;
using model;

namespace persistence.interfaces;

public interface IResultRepository: IRepository<Result, long>
{
    IList<Result> FindByParticipant(long idParticipant);
    IList<Result> FindByTrial(long idTrial);
}