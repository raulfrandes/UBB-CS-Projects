using System;
using System.Runtime.ConstrainedExecution;
using Bills.Domain;
using Bills.Repository;

namespace Bills
{
    internal class Program
    {
        static void PrintMenu()
        {
            Console.WriteLine("Menu: ");
            Console.WriteLine("1.Sa se afișeze toate documentele (nume, dataEmitere) emise in anul 2023");
            Console.WriteLine("2.Sa se afișeze toate facturile (nume, dataScadenta) scadente in luna curenta.");
            Console.WriteLine("3.Sa se afiseze toate facturile (nume, nrProduse) cu cel putin 3 produse achizitionate.");
            Console.WriteLine("4.Sa se afișeze toate achizitiile (produs, numeFactura) din categoria Utilities.");
            Console.WriteLine("5.Sa se afișeze categoria de facturi care a înregistrat cele mai multe cheltuieli.");
            Console.WriteLine("0.Exit");
        }
        
        public static void Main(string[] args)
        {
            DocumentsFileRepository documentsFileRepository = 
                new DocumentsFileRepository("D:\\Anul2\\Semestrul1\\Metode avansate de programare\\Laboratoare\\Lab11\\Bills\\Bills\\Data\\documents.txt");
            BillsFileRepository billsFileRepository = new BillsFileRepository("D:\\Anul2\\Semestrul1\\Metode avansate de programare\\Laboratoare\\Lab11\\Bills\\Bills\\Data\\bills.txt");
            AcquisitionsFileRepository acquisitionsFileRepository =
                new AcquisitionsFileRepository("D:\\Anul2\\Semestrul1\\Metode avansate de programare\\Laboratoare\\Lab11\\Bills\\Bills\\Data\\acquisitions.txt");

            Service.Service service = new Service.Service(documentsFileRepository, billsFileRepository, 
                acquisitionsFileRepository);

            while (true)
            {
                PrintMenu();
                string option = Console.ReadLine();
                switch (option)
                {
                    case "1":
                    {
                        service.GetDocumentsByYear(2023).ForEach(Console.WriteLine);
                        break;
                    }
                    case "2":
                    {
                        service.GetBillsDueCurrentDay().ForEach(Console.WriteLine);
                        break;
                    }
                    case "3":
                    {
                        service.GetBillsAtLeast3Acquisitions().ForEach(Console.WriteLine);
                        break;
                    }
                    case "4":
                    {
                        service.GetAcquisitionsFromUtilities().ForEach(Console.WriteLine);
                        break;
                    }
                    case "5":
                    {
                        Console.WriteLine(service.GetCategoryWithMostSpendings());
                        break;
                    }
                    case "0":
                    {
                        return;
                    }
                    default:
                    {
                        Console.WriteLine("Invalid option!");
                        break;
                    }
                }
            }
        }
    }
}