using System;
using Bills.Domain;

namespace Bills.Repository;

public class BillsFileRepository : InFileRepository<string, Bill>
{
    public BillsFileRepository(string filename) : base(filename)
    {
    }

    protected override Bill EntityFromString(string? data)
    {
        string[] attributes = data.Split(',');

        string id = attributes[0];
        DateTime dueDate = DateTime.Parse(attributes[1]);
        Category category = (Category)Enum.Parse(typeof(Category), attributes[2]);
        Bill bill = new Bill(id, null, DateTime.Now, dueDate, null, category);
        return bill;
    }
}