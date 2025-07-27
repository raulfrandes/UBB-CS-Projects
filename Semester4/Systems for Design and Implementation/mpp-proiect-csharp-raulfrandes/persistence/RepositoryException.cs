using System;

namespace persistence;

public class RepositoryException(string mess):ApplicationException(mess);