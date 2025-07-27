package triathlon.persistence.interfaces;

import triathlon.model.Result;
import triathlon.persistence.IRepository;

import java.util.List;

public interface IResultRepository extends IRepository<Result, Long> {
    List<Result> findByParticipant(Long idParticipant);
    List<Result> findByTrial(Long idTrial);
}
