<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientOperationController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientProfileController" %>
<!DOCTYPE html>
<html>

    <head>
        <title>Client's main page</title>
    </head>

    <body>

        <ul>
            <li>
                <a href="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + ClientOperationController.PATH_OF_REQUEST_PARAM_TO_LIST_PAYMENTS_OF_LOGGED_ON_CLIENT %>">
                    History of all payments
                </a>
            </li>
            <li>
                <a href="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                            + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS %>">
                    History of all sent payments
                </a>
            </li>
            <li>
                <a href="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_RECEIVED_BY_LOGGED_ON_CLIENT_PAYMENTS %>">
                    History of all received payments
                </a>
            </li>
            <li>
                <a href="<%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT %>">
                    My profile
                </a>
            </li>
        </ul>

    </body>

</html>