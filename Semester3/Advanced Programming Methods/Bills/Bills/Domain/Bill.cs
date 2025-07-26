using System;
using System.Collections.Generic;

namespace Bills.Domain
{
    public enum Category
    {
        Utilities, Groceries, Fashion, Entertainment, Electronics
    }
    public class Bill : Document
    {
        public DateTime DueDate { get; set; }
        public List<Acquisition> Acquisitions { get; set; }
        public Category Category { get; set; }

        public Bill(string id, string name, DateTime issueDate, DateTime dueDate, List<Acquisition> acquisitions,
            Category category)
        {
            Id = id;
            Name = name;
            IssueDate = issueDate;
            DueDate = dueDate;
            Acquisitions = acquisitions;
            Category = category;
        }

        public Bill()
        {
        }

        public override string ToString()
        {
            return $"Id: {Id} | Name: {Name} | IssueDate: {IssueDate} | DueDate: {DueDate} | " +
                   $"Acquisitions: {Acquisitions} | Category: {Category}";
        }
    }
}