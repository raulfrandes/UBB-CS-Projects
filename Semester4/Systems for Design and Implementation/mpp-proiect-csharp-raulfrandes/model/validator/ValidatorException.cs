using System;

namespace model.validator;

public class ValidatorException(string mess) : ApplicationException(mess);