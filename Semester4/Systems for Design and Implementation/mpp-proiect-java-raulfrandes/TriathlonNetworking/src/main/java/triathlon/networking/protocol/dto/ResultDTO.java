package triathlon.networking.protocol.dto;

import java.io.Serializable;

public class ResultDTO implements Serializable {
    private Long idParticipant;
    private Long idTrial;
    private Integer points;

    public ResultDTO(Long idParticipant, Long idTrial, Integer points) {
        this.idParticipant = idParticipant;
        this.idTrial = idTrial;
        this.points = points;
    }

    public Long getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(Long idParticipant) {
        this.idParticipant = idParticipant;
    }

    public Long getIdTrial() {
        return idTrial;
    }

    public void setIdTrial(Long idTrial) {
        this.idTrial = idTrial;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
