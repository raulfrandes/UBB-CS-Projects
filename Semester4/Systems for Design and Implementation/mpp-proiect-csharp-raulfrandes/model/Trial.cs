using Newtonsoft.Json;

namespace model;

public class Trial(string name, string description, Referee referee) : Entity<long>
{
    [JsonProperty("name")]
    public string Name { get; set; } = name;
    [JsonProperty("description")]
    public string Description { get; set; } = description;
    [JsonProperty("referee")]
    public Referee Referee { get; set; } = referee;

    public override string ToString()
    {
        return base.ToString() + $", Name: {Name}, Description: {Description}, Referee: {Referee}";
    }
}