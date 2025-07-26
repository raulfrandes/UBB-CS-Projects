using System;

namespace Bills.Domain.DTO;

public class BillDTO
{
    public string Name { get; set; }
    public DateTime DueDate { get; set; }

    public BillDTO(string name, DateTime dueDate)
    {
        Name = name;
        DueDate = dueDate;
    }

    public BillDTO()
    {
    }

    public override string ToString()
    {
        return $"Name: {Name} | DueDate: {DueDate}";
    }
}