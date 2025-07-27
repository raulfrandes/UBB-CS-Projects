namespace model;

public class Participant(string name, string code): Entity<long>
{
    public string Name { get; set; } = name;
    public string Code { get; set; } = code;

    public override string ToString()
    {
        return base.ToString() + $", Name: {Name}, Code: {Code}";
    }
}