<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.*" %>
<!DOCTYPE html>
<html>

    <head>
        <title>Administrator main page</title>
        <meta name="keywords" content="administrator main page card payment"/>
        <meta name="description" content="administrator main page"/>
    </head>

    <body>
        <ul>
            <li>
                <a href="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                    All bank accounts
                </a>
            </li>
            <li>
                <a href="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                    All administrators
                </a>
            </li>
            <li>
                <a href="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                    All clients
                </a>
            </li>
            <li>
                <a href="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                    All payment cards
                </a>
            </li>
            <li>
                <a href="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                           + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                    All payments
                </a>
            </li>
        </ul>
    </body>

</html>