using System;

namespace Bills.Repository;

public class RepositoryException : ApplicationException
{
    public RepositoryException(string mess) : base(mess)
    {
    }
}