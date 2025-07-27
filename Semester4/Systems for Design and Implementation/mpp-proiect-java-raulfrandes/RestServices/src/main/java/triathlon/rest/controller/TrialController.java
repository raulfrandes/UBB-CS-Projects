package triathlon.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triathlon.model.Trial;
import triathlon.persistence.RepositoryException;
import triathlon.persistence.interfaces.ITrialRepository;

import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/triathlon/trials")
public class TrialController {
    private final ITrialRepository trialRepository;

    @Autowired
    public TrialController(ITrialRepository trialRepository) {
        this.trialRepository = trialRepository;
    }

    @GetMapping
    public Trial[] findAll() {
        System.out.println("Find all trials ...");
        return StreamSupport.stream(trialRepository.findAll().spliterator(), false).toArray(Trial[]::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        System.out.println("Find by id " + id);
        Trial trial = trialRepository.findById(id);
        if (trial == null) {
            return new ResponseEntity<String>("Trial not found", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<Trial>(trial, HttpStatus.OK);
        }
    }

    @PostMapping
    public Trial save(@RequestBody Trial trial) {
        trialRepository.save(trial);
        return trial;
    }

    @PutMapping
    public Trial update(@RequestBody Trial trial) {
        System.out.println("Updating trial ...");
        trialRepository.update(trial);
        return trial;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        System.out.println("Deleting trial ... " + id);
        try {
            trialRepository.deleteById(id);
            return new ResponseEntity<Trial>(HttpStatus.OK);
        } catch (RepositoryException ex) {
            System.out.println("Delete trial exception");
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
