using System;

namespace model.DTOs;

[Serializable]
public class TrialDTO(long id, string name)
{
    public long Id { get; set; } = id;
    public string Name { get; set; } = name;

    public override string ToString()
    {
        return Name;
    }
}