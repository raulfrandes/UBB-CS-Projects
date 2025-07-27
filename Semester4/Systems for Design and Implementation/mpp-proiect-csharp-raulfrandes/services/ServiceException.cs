using System;

namespace services;

public class ServiceException(string mess):ApplicationException(mess)
{
    
}