<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.BankAccountOperationController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>
<%@ page import="static by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorControllerProperty.DELIMITER_OF_OPERATIONS" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.bankaccount.BankAccountSortingKey" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring-form"%>

<!DOCTYPE html>
<html>

    <head>
        <title>Bank accounts</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/bank_account_number_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Bank accounts</h2>
            </div>
        </div>

        <div id="container">
            <div id="listed_entities">

                <input type="button" value="Add new"
                       onclick="window.location.href='<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                        + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_BANK_ACCOUNT %>'"
                       class="add-button" />

                <form name="form_to_find_bank_accounts_by_number" method="GET"
                      action="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_BANK_ACCOUNTS_BY_NUMBER %>"
                      onsubmit="return checkValidOfNumberOfBankAccountAndAlertIfNotValid(
                          document.form_to_find_bank_accounts_by_number.number_of_found_bank_accounts.value);">
                    <label for="input_to_find_bank_accounts_by_number">
                        Find bank accounts by number:
                    </label>
                    <input id="input_to_find_bank_accounts_by_number" type="text" name="number_of_found_bank_accounts" />
                    <input type="submit" value="Find" />
                </form>

                <table>

                    <jstl-core:url var="link_to_sort_by_money" value="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                        + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                        <jstl-core:param name="<%= BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= BankAccountSortingKey.MONEY.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_blocked" value="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                          + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                        <jstl-core:param name="<%= BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= BankAccountSortingKey.BLOCKED.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_number" value="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                         + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                        <jstl-core:param name="<%= BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= BankAccountSortingKey.NUMBER.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_deleted" value="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                          + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                        <jstl-core:param name="<%= BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= BankAccountSortingKey.DELETED.name() %>" />
                    </jstl-core:url>

                    <tr>
                        <th><a href="${link_to_sort_by_money}">Money</a></th>
                        <th><a href="${link_to_sort_by_blocked}">Blocked</a></th>
                        <th><a href="${link_to_sort_by_number}">Number</a></th>
                        <th><a href="${link_to_sort_by_deleted}">Deleted</a></th>
                        <th>Operation</th>
                    </tr>

                    <jsp:useBean id="listed_bank_accounts" scope="request" type="java.util.Collection" />
                    <jstl-core:forEach var="listed_bank_account" items="${listed_bank_accounts}">

                        <jstl-core:url var="link_to_update_bank_account" value="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                  + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_BANK_ACCOUNT %>">
                            <jstl-core:param name="<%= BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_BANK_ACCOUNT %>"
                                             value="${listed_bank_account.id}" />
                        </jstl-core:url>

                        <jstl-core:url var="link_to_delete_bank_account" value="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                  + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_BANK_ACCOUNT %>">
                            <jstl-core:param name="<%= BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_BANK_ACCOUNT %>"
                                             value="${listed_bank_account.id}" />
                        </jstl-core:url>

                        <tr>
                            <td>${listed_bank_account.money}</td>
                            <td>${listed_bank_account.blocked}</td>
                            <td>${listed_bank_account.number}</td>
                            <td>${listed_bank_account.deleted}</td>
                            <td>
                                <a href="${link_to_update_bank_account}">Update</a>

                                <%= DELIMITER_OF_OPERATIONS.getValue() %>

                                <a href="${link_to_delete_bank_account}"
                                   onclick="return (confirm('Are you sure you want to delete this bank '
                                            + 'account?(You will also delete associated clients, payment cards and '
                                            + 'payments)'))">
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