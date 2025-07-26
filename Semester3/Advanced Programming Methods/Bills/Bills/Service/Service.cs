using System;
using System.Collections.Generic;
using System.Linq;
using Bills.Domain;
using Bills.Domain.DTO;
using Bills.Repository;

namespace Bills.Service;

public class Service
{
    private IRepository<string, Document> DocumentsRepository;
    private IRepository<string, Bill> BillsRepository;
    private IRepository<string, Acquisition> AcquisitionsRepository;

    public Service(DocumentsFileRepository documentsFileRepository, BillsFileRepository billsFileRepository,
        AcquisitionsFileRepository acquisitionsFileRepository)
    {
        DocumentsRepository = documentsFileRepository;
        BillsRepository = billsFileRepository;
        AcquisitionsRepository = acquisitionsFileRepository;
    }

    public List<Document> GetDocumentsByYear(int year)
    {
        var documents = DocumentsRepository.FindAll();
        return (from d in documents
            where d.IssueDate.Year == year
            select d).ToList();
    }

    public List<Bill> GetInitializedBills()
    {
        var documents = DocumentsRepository.FindAll();
        var bills = BillsRepository.FindAll();
        return (from bill in bills
            join document in documents on bill.Id equals document.Id
            select new Bill
            {
                Id = bill.Id,
                Name = document.Name,
                IssueDate = bill.IssueDate,
                DueDate = bill.DueDate,
                Acquisitions = bill.Acquisitions,
                Category = bill.Category
            }).ToList();
    }

    public List<BillDTO> GetBillsDueCurrentDay()
    {
        int currentMonth = DateTime.Now.Month;
        int currentYear = DateTime.Now.Year;
        var bills = GetInitializedBills();
        return (from bill in bills
            where bill.DueDate.Month == currentMonth && bill.DueDate.Year == currentYear
            select new BillDTO
            {
                DueDate = bill.DueDate,
                Name = bill.Name
            }).ToList();
    }

    public List<Bill> GetInitializedBillsWithAcquisitions()
    {
        var bills = GetInitializedBills();
        var acquisitions = AcquisitionsRepository.FindAll();

        return bills.GroupJoin(
            acquisitions,
            bill => bill.Id,
            acquisition => acquisition.IdBill,
            (bill, acquisitionsGroup) => new Bill
            {
                Id = bill.Id,
                Name = bill.Name,
                Acquisitions = acquisitionsGroup.ToList(),
                Category = bill.Category,
                DueDate = bill.DueDate,
                IssueDate = bill.IssueDate
            }).ToList();
    }

    public List<BillDTO2> GetBillsAtLeast3Acquisitions()
    {
        var bills = GetInitializedBillsWithAcquisitions();
        return (from bill in bills
            where bill.Acquisitions.Sum(a => a.Amount) >= 3
            select new BillDTO2
            {
                Name = bill.Name,
                Amount = bill.Acquisitions.Sum(a => a.Amount)
            }).ToList();
    }

    public List<Acquisition> GetInitializedAcquisitions()
    {
        var acquisitions = AcquisitionsRepository.FindAll();
        var bills = GetInitializedBills();
        return (from acquisition in acquisitions
            join bill in bills on acquisition.IdBill equals bill.Id
            select new Acquisition
            {
                Amount = acquisition.Amount,
                Bill = bill,
                Id = acquisition.Id,
                IdBill = bill.Id,
                Product = acquisition.Product,
                ProductPrice = acquisition.ProductPrice
            }).ToList();
    }

    public List<AcquisitionDTO> GetAcquisitionsFromUtilities()
    {
        var acquisitions = GetInitializedAcquisitions();
        return (from acquisition in acquisitions
            where acquisition.Bill.Category == Category.Utilities
            select new AcquisitionDTO
            {
                BillName = acquisition.Bill.Name,
                Product = acquisition.Product
            }).ToList();
    }

    public Category GetCategoryWithMostSpendings()
    {
        var bills = GetInitializedBillsWithAcquisitions();
        return bills
            .Where(bill => bill.Acquisitions != null)
            .GroupBy(bill => bill.Category)
            .Select(group => new
            {
                Category = group.Key,
                TotalAmount = group.Sum(bill => bill.Acquisitions.Sum(acquisition => acquisition.ProductPrice))
            })
            .OrderByDescending(item => item.TotalAmount)
            .FirstOrDefault()?.Category ?? Category.Utilities;
    }
}