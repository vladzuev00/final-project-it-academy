<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>
<%@ page import="static by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorControllerProperty.DELIMITER_OF_OPERATIONS" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.ClientOperationController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.client.ClientSortingKey" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Clients</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/user_email_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Clients</h2>
            </div>
        </div>

        <div id="container">
            <div id="listed_entities">

                <input type="button" value="Add new" class="add-button"
                       onclick="window.location.href='<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_CLIENT %>'" />

                <form name="form_to_find_clients_by_email" method="GET"
                      action="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_CLIENTS_BY_EMAIL %>"
                      onsubmit="return checkValidOfEmailOfUserAndAlertIfNotValid(
                          document.form_to_find_clients_by_email.email_of_found_clients.value);">
                    <label for="input_to_find_clients_by_email">
                        Find clients by email:
                    </label>
                    <input id="input_to_find_clients_by_email" type="text" name="email_of_found_clients"  />
                    <input type="submit" value="Find" />
                </form>

                <table>

                    <jstl-core:url var="link_to_sort_by_email" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.EMAIL.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_password" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                           + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.PASSWORD.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_name" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                       + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.NAME.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_surname" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                          + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.SURNAME.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_patronymic" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                             + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.PATRONYMIC.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_phone_number" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                               + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.PHONE_NUMBER.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_number_of_bank_account" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                         + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.NUMBER_OF_BANK_ACCOUNT.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_deleted" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                          + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                        <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= ClientSortingKey.DELETED.name() %>" />
                    </jstl-core:url>

                    <tr>
                        <th><a href="${link_to_sort_by_email}">Email</a></th>
                        <th><a href="${link_to_sort_by_password}">Password</a></th>
                        <th><a href="${link_to_sort_by_name}">Name</a></th>
                        <th><a href="${link_to_sort_by_surname}">Surname</a></th>
                        <th><a href="${link_to_sort_by_patronymic}">Patronymic</a></th>
                        <th><a href="${link_to_sort_by_phone_number}">Phone number</a></th>
                        <th><a href="${link_to_sort_by_number_of_bank_account}">Number of bank account</a></th>
                        <th>Numbers of payment cards</th>
                        <th><a href="${link_to_sort_by_deleted}">Deleted</a></th>
                        <th>Operation</th>
                    </tr>

                    <jsp:useBean id="listed_clients" scope="request" type="java.util.Collection" />
                    <jstl-core:forEach var="listed_client" items="${listed_clients}">

                        <jstl-core:url var="link_to_update_client" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_CLIENT %>">
                            <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_CLIENT %>"
                                             value="${listed_client.id}" />
                        </jstl-core:url>

                        <jstl-core:url var="link_to_delete_client" value="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                        + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_CLIENT %>">
                            <jstl-core:param name="<%= ClientOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_CLIENT %>"
                                             value="${listed_client.id}" />
                        </jstl-core:url>

                        <tr>
                            <td>${listed_client.email}</td>
                            <td>${listed_client.password}</td>
                            <td>${listed_client.name}</td>
                            <td>${listed_client.surname}</td>
                            <td>${listed_client.patronymic}</td>
                            <td>${listed_client.phoneNumber}</td>
                            <td>${listed_client.bankAccount.number}</td>

                            <td>
                                <jstl-core:choose>
                                    <jstl-core:when test="${!listed_client.paymentCards.isEmpty()}">
                                        <jstl-core:forEach var="payment_card" items="${listed_client.paymentCards}">
                                            ${payment_card.cardNumber}
                                            <br />
                                        </jstl-core:forEach>

                                    </jstl-core:when>
                                    <jstl-core:otherwise>
                                        -
                                    </jstl-core:otherwise>

                                </jstl-core:choose>
                            </td>

                            <td>${listed_client.deleted}</td>

                            <td>
                                <a href="${link_to_update_client}">Update</a>

                                <%= DELIMITER_OF_OPERATIONS.getValue() %>

                                <a href="${link_to_delete_client}"
                                   onclick="return (confirm('Are you sure you want to delete this client?(You will also delete ' +
                                            'associated payment cards and payments)'))">
                                    Delete
                                </a>
                            </td>
                        </tr>

                    </jstl-core:forEach>

                </table>

                <br />
                <i>Hint: to sort clients click on header of table</i>

            </div>
        </div>

    </body>

</html>