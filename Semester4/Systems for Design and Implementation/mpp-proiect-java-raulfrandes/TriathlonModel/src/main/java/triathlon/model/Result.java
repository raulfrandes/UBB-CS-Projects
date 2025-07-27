package triathlon.model;

import jakarta.persistence.*;

@jakarta.persistence.Entity
public class Result implements Entity<Long>{
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Participant participant;
    @ManyToOne
    private Trial trial;
    @Column
    private Integer points;

    public Result() {
    }

    public Result(Participant participant, Trial trial, Integer points) {
        this.participant = participant;
        this.trial = trial;
        this.points = points;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Trial getTrial() {
        return trial;
    }

    public void setTrial(Trial trial) {
        this.trial = trial;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Result{" +
                "participant=" + participant +
                ", trial=" + trial +
                ", points=" + points +
                ", id=" + id +
                '}';
    }
}
