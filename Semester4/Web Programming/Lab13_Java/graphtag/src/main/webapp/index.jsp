<%@ taglib prefix="gt" uri="http://example.com/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Custom Graph Tag</title>
</head>
<body>
    <h1>Custom Graph Tag</h1>
    <gt:graph valuesOx="${requestScope.valuesOx}"
              valuesOy="${requestScope.valuesOy}"
              labelOx="Time"
              labelOy="Quantity"
              minOx="0"
              maxOx="6"
              minOy="0"
              maxOy="40"
              color="#FF0000"/>
</body>
</html>