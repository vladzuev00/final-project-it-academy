<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.logon.LogOnController" %>

<!DOCTYPE html>
<html>
<head>
    <title>Log on page</title>
    <meta name="keywords" content="log on page card payment"/>
    <meta name="description" content="log on page"/>
</head>
<body>


    <form accept-charset="UTF-8" action="<%= LogOnController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                           + LogOnController.PATH_OF_REQUEST_MAPPING_TO_LOG_ON %>" method="GET">
        Email: <input type="email" name="<%= LogOnController.NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_LOGGED_ON_USER %>" />
        Password: <input type="password" name="<%= LogOnController.NAME_OF_REQUEST_PARAM_OF_PASSWORD_OF_LOGGED_ON_USER %>" />
        <input type="submit" value="log on" />
    </form>

</body>

</html>