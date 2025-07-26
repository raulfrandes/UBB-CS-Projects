namespace Bills.Domain.DTO;

public class BillDTO2
{
    public string Name { get; set; }
    public int Amount { get; set; }

    public BillDTO2(string name, int amount)
    {
        Name = name;
        Amount = amount;
    }

    public BillDTO2()
    {
    }

    public override string ToString()
    {
        return $"Name: {Name} | NumberOfProducts: {Amount}";
    }
}