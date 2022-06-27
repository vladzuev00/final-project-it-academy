<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.logon.LogOnController" %>

<!DOCTYPE html>
<html>

<head>

    <title>Welcome</title>
    <meta name="keywords" content="welcome card payment"/>
    <meta name="description" content="welcome page"/>

</head>

<body>

    <ul>
        <li><a href="#">Registration</a></li>
        <li><a href="<%= LogOnController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + LogOnController.PATH_OF_REQUEST_MAPPING_OF_LOG_ON_PAGE %>">Logging on</a></li>
    </ul>

</body>

</html>