using System;

namespace networking.dtos;

[Serializable]
public class ResultDTO(long idParticipant, long idTrial, int points)
{
    public long IdParticipant { get; set; } = idParticipant;
    public long IdTrial { get; set; } = idTrial;
    public int Points { get; set; } = points;
}