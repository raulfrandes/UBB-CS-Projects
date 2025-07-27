<%@ taglib prefix="ct" uri="http://example.com/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Custom Calendar Tag</title>
    <style>
        .mystyle {
            border: 1px solid black;
            border-collapse: collapse;
        }
        .mystyle th, .mystyle td {
            border: 1px solid black;
            padding: 5px;
            text-align: center;
        }
    </style>
</head>
<body>
    <h1>Custom Calendar Tag</h1>
    <ct:calendar an="2024" luna="6" zi="3" culoare="#25f00f" cssClass="mystyle"/>
</body>
</html>