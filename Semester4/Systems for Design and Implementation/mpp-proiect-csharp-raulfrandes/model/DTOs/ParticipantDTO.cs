using System;

namespace model.DTOs
{
    [Serializable]
    public class ParticipantDTO(long id, string name, string code, int points)
    {
        public long Id { get; set; } = id;
        public string Name { get; set; } = name;
        public string Code { get; set; } = code;
        public int Points { get; set; } = points;
    }
}
