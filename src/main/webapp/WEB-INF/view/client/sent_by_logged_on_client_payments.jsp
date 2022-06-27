<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientOperationController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.payment.PaymentSortingKey" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Sent payments</title>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>All your sent payments</h2>
            </div>
        </div>

        <div id="container">
            <div id="listed_entities">

                <table>

                    <jstl-core:url var="link_to_sort_by_card_number_of_receiver"
                                   value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.CARD_NUMBER_OF_RECEIVER.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_money"
                                   value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.MONEY.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_date"
                                   value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.DATE.name() %>" />
                    </jstl-core:url>

                    <tr>
                        <th><a href="${link_to_sort_by_card_number_of_receiver}">Card's number of receiver</a></th>
                        <th><a href="${link_to_sort_by_money}">Money</a></th>
                        <th><a href="${link_to_sort_by_date}">Date</a></th>
                    </tr>

                    <jsp:useBean id="listed_payments" scope="request" type="java.util.Collection" />
                    <jsp:useBean id="map_of_payment_to_its_description_of_date" scope="request" type="java.util.Map" />
                    <jstl-core:forEach var="listed_payment" items="${listed_payments}">
                        <tr>
                            <td>${listed_payment.cardOfReceiver.cardNumber}</td>
                            <td>${listed_payment.money}</td>
                            <td>${map_of_payment_to_its_description_of_date.get(listed_payment)}</td>
                        </tr>
                    </jstl-core:forEach>

                </table>

                <br />

                <p>
                    <a href="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                               + ClientOperationController.PATH_OF_REQUEST_PARAM_OF_MAIN_CLIENT_PAGE %>">
                        main page
                    </a>
                </p>

            </div>
        </div>

    </body>

</html>