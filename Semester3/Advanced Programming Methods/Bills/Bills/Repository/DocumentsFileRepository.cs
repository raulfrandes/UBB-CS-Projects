#nullable enable
using System;
using Bills.Domain;

namespace Bills.Repository;

public class DocumentsFileRepository : InFileRepository<string, Document>
{
    public DocumentsFileRepository(string filename) : base(filename)
    {
    }

    protected override Document EntityFromString(string? data)
    {
        string[] attributes = data.Split(',');

        string id = attributes[0];
        string name = attributes[1];
        DateTime issueDate = DateTime.Parse(attributes[2]);

        Document document = new Document(id, name, issueDate);
        return document;
    }
}