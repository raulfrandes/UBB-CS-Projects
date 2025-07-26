namespace Bills.Domain
{
    public class Acquisition : Entity<string>
    {
        public string Product { get; set; }
        public int Amount { get; set; }
        public double ProductPrice { get; set; }
        public Bill? Bill { get; set; }
        public string IdBill { get; set; }

        public Acquisition(string id, string product, int amount, double productPrice, string idBill, Bill bill)
        {
            Id = id;
            Product = product;
            Amount = amount;
            ProductPrice = productPrice;
            IdBill = idBill;
            Bill = bill;
        }

        public Acquisition()
        {
        }

        public override string ToString()
        {
            return $"Id: {Id} | Product: {Product} | Amount: {Amount} | ProductPrice: {ProductPrice} | Bill: {Bill} | " +
                   $"IdBill: {IdBill}";
        }
    }
}