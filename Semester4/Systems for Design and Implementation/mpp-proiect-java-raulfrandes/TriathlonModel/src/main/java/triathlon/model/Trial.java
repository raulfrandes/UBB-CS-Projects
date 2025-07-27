package triathlon.model;

import jakarta.persistence.*;
import org.hibernate.sql.results.graph.collection.internal.EagerCollectionFetch;

@jakarta.persistence.Entity
public class Trial implements Entity<Long>{
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @Column(name = "nameTrial")
    private String name;
    @Column
    private String description;
    @ManyToOne()
    private Referee referee;

    public Trial() {
    }

    public Trial(String name, String description, Referee referee) {
        this.name = name;
        this.description = description;
        this.referee = referee;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    @Override
    public String toString() {
        return "Trial{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", referee=" + referee +
                ", id=" + id +
                '}';
    }
}
