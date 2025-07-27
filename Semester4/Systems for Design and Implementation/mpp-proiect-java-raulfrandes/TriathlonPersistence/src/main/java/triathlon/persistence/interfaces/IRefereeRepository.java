package triathlon.persistence.interfaces;

import triathlon.model.Referee;
import triathlon.persistence.IRepository;

public interface IRefereeRepository extends IRepository<Referee, Long> {
    Referee findByUsername(String username);
}
