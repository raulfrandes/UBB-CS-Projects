package triathlon.model.DTOs;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private Long id;
    private String name;
    private String code;
    private Integer points;

    public ParticipantDTO(Long id, String name, String code, Integer points) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
