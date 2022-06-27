<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>
<%@ page import="static by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorControllerProperty.DELIMITER_OF_OPERATIONS" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.PaymentCardOperationController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.paymentcard.PaymentCardSortingKey" %>

<!DOCTYPE html>
<html>

    <head>
        <title>All payment cards</title>
        <script src="${pageContext.request.contextPath}/resources/script/card_number_of_payment_card_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Payment cards</h2>
            </div>
        </div>

        <div id="container">
            <div id="listed_entities">

                <input type="button" value="Add new" class="add-button"
                       onclick="window.location.href='<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                        + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT_CARD %>'" />

                <form name="form_to_find_payment_cards_by_card_number" method="GET"
                      action="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER %>"
                      onsubmit="return checkValidOfCardNumberOfPaymentCardAndAlertIfNotValid(
                          document.form_to_find_payment_cards_by_card_number.card_number_of_found_payment_cards.value);">
                    <label for="input_to_find_payment_cards_by_card_number">
                        Find payment cards by card number:
                    </label>
                    <input id="input_to_find_payment_cards_by_card_number" type="text"
                           name="card_number_of_found_payment_cards" />
                    <input type="submit" value="Find" />
                </form>

                <table>

                    <jstl-core:url var="link_to_sort_by_card_number" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                              + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.CARD_NUMBER.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_expiration_date" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                  + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.EXPIRATION_DATE.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_payment_system" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                 + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.PAYMENT_SYSTEM.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_cvc" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                      + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.CVC.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_email_of_client" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                  + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.EMAIL_OF_CLIENT.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_phone_number_of_client" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                         + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.PHONE_NUMBER_OF_CLIENT.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_name_of_bank" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                               + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.NAME_OF_BANK.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_password" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                           + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.PASSWORD.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_deleted" value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                          + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                        <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= PaymentCardSortingKey.DELETED.name() %>" />
                    </jstl-core:url>

                    <tr>
                        <th><a href="${link_to_sort_by_card_number}">Card number</a></th>
                        <th><a href="${link_to_sort_by_expiration_date}">Expiration date</a></th>
                        <th><a href="${link_to_sort_by_payment_system}">Payment system</a></th>
                        <th><a href="${link_to_sort_by_cvc}">Cvc</a></th>
                        <th><a href="${link_to_sort_by_email_of_client}">Client's email</a></th>
                        <th><a href="${link_to_sort_by_phone_number_of_client}">Client's phone number</a></th>
                        <th><a href="${link_to_sort_by_name_of_bank}">Name of bank</a></th>
                        <th><a href="${link_to_sort_by_password}">Password</a></th>
                        <th><a href="${link_to_sort_by_deleted}">Deleted</a></th>
                        <th>Operation</th>
                    </tr>

                    <jsp:useBean id="listed_payment_cards" scope="request" type="java.util.Collection" />
                    <jstl-core:forEach var="listed_payment_card" items="${listed_payment_cards}">

                        <jstl-core:url var="link_to_update_payment_card"
                                       value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT_CARD %>">
                            <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_PAYMENT_CARD %>"
                                             value="${listed_payment_card.id}" />
                        </jstl-core:url>

                        <jstl-core:url var="link_to_delete_payment_card"
                                       value="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_PAYMENT_CARD %>">
                            <jstl-core:param name="<%= PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_PAYMENT_CARD %>"
                                             value="${listed_payment_card.id}" />
                        </jstl-core:url>

                        <tr>
                            <td>${listed_payment_card.cardNumber}</td>
                            <td>${listed_payment_card.expirationDate.month}/${listed_payment_card.expirationDate.year}</td>
                            <td>${listed_payment_card.paymentSystem}</td>
                            <td>${listed_payment_card.cvc}</td>
                            <td>${listed_payment_card.client.email}</td>
                            <td>${listed_payment_card.client.phoneNumber}</td>
                            <td>${listed_payment_card.nameOfBank}</td>
                            <td>${listed_payment_card.password}</td>
                            <td>${listed_payment_card.deleted}</td>
                            <td>

                                <a href="${link_to_update_payment_card}">Update</a>

                                <%= DELIMITER_OF_OPERATIONS.getValue() %>

                                <a href="${link_to_delete_payment_card}"
                                   onclick="return (confirm('Are you sure you want to delete this payment card?(You will also delete '
                                                  + 'associated payments)'))">
                                    Delete
                                </a>

                            </td>
                        </tr>

                    </jstl-core:forEach>

                </table>

                <br />
                <i>Hint: to sort bank accounts click on header of table</i>

            </div>
        </div>

    </body>

</html>