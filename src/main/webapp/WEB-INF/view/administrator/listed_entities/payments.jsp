<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.payment.PaymentSortingKey" %>
<%@ page
        import="static by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorControllerProperty.DELIMITER_OF_OPERATIONS" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.PaymentOperationController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Payments</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/date_of_payment_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Payments</h2>
            </div>
        </div>

        <div id="container">
            <div id="listed_entities">

                <input type="button" value="Add new" class="add-button"
                       onclick="window.location.href='<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                        + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT %>'" />

                <form name="form_to_find_payments_by_date" method="GET"
                      action="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_PAYMENTS_BY_RANGE_OF_DATES %>"
                      onsubmit="return checkValidOfDescriptionOfDateOfPaymentAndAlertIfNotValid(
                                            document.form_to_find_payments_by_date.minimum_date_to_find_payments_by_date.value)
                                    && checkValidOfDescriptionOfDateOfPaymentAndAlertIfNotValid(
                                            document.form_to_find_payments_by_date.maximum_date_to_find_payments_by_date.value);">

                    <legend>Find payments by date</legend>

                    <label for="input_of_minimum_date_to_find_payments_by_date">From:</label>
                    <input id="input_of_minimum_date_to_find_payments_by_date" type="text"
                           name="minimum_date_to_find_payments_by_date" required="required" />

                    <label for="input_of_maximum_date_to_find_payments_by_date">To:</label>
                    <input id="input_of_maximum_date_to_find_payments_by_date" type="text"
                           name="maximum_date_to_find_payments_by_date" required="required" />

                    <input type="submit" value="Find" />

                </form>

                <table>

                    <jstl-core:url var="link_to_sort_by_card_number_of_sender"
                                   value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                            + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                        <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.CARD_NUMBER_OF_SENDER.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_card_number_of_receiver"
                                   value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                            + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                        <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.CARD_NUMBER_OF_RECEIVER.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_money"
                                   value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                            + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                        <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.MONEY.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_date"
                                   value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                            + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                        <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.DATE.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_deleted"
                                   value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                            + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                        <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentSortingKey.DELETED.name() %>" />
                    </jstl-core:url>

                    <tr>
                        <th><a href="${link_to_sort_by_card_number_of_sender}">Card's number of sender</a></th>
                        <th><a href="${link_to_sort_by_card_number_of_receiver}">Card's number of receiver</a></th>
                        <th><a href="${link_to_sort_by_money}">Money</a></th>
                        <th><a href="${link_to_sort_by_date}">Date</a></th>
                        <th><a href="${link_to_sort_by_deleted}">Deleted</a></th>
                        <th>Operation</th>
                    </tr>

                    <jsp:useBean id="listed_payments" scope="request" type="java.util.Collection" />
                    <jsp:useBean id="map_of_payment_to_its_description_of_date" scope="request" type="java.util.Map" />
                    <jstl-core:forEach var="listed_payment" items="${listed_payments}">

                        <jstl-core:url var="link_to_update_payment"
                                       value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT %>">
                            <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_PAYMENT %>"
                                             value="${listed_payment.id}" />
                        </jstl-core:url>

                        <jstl-core:url var="link_to_delete_payment"
                                       value="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_PAYMENT %>">
                            <jstl-core:param name="<%= PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_PAYMENT %>"
                                             value="${listed_payment.id}" />
                        </jstl-core:url>

                        <tr>
                            <td>${listed_payment.cardOfSender.cardNumber}</td>
                            <td>${listed_payment.cardOfReceiver.cardNumber}</td>
                            <td>${listed_payment.money}</td>
                            <td>${map_of_payment_to_its_description_of_date.get(listed_payment)}</td>
                            <td>${listed_payment.deleted}</td>
                            <td>
                                <a href="${link_to_update_payment}">Update</a>

                                <%= DELIMITER_OF_OPERATIONS.getValue() %>

                                <a href="${link_to_delete_payment}"
                                   onclick="return (confirm('Are you sure you want delete this payment?'))">
                                    Delete
                                </a>
                            </td>
                        </tr>

                    </jstl-core:forEach>

                </table>

                <br />
                <i>Hint: to sort payments click on header of table</i>

            </div>
        </div>

    </body>

</html>