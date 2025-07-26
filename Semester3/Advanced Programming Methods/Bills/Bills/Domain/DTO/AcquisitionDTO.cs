namespace Bills.Domain.DTO;

public class AcquisitionDTO
{
    public string Product { get; set; }
    public string BillName { get; set; }

    public AcquisitionDTO(string product, string billName)
    {
        Product = product;
        BillName = billName;
    }

    public AcquisitionDTO()
    {
    }

    public override string ToString()
    {
        return $"Product: {Product} | Bill Name: {BillName}";
    }
}