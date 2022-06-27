<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>
<%@ page import="static by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorControllerProperty.DELIMITER_OF_OPERATIONS" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorOperationController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.administrator.AdministratorSortingKey" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Administrators</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/user_email_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Administrators</h2>
            </div>
        </div>

        <div id="container">
            <div id="listed_entities">

                <input type="button" value="Add new" onclick="window.location.href='<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                      + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_ADMINISTRATOR %>'"
                       class="add-button" />

                <form name="form_to_find_administrator_by_email" method="GET"
                      action="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_ADMINISTRATORS_BY_EMAIL %>"
                      onsubmit="return checkValidOfEmailOfUserAndAlertIfNotValid(
                          document.form_to_find_administrator_by_email.email_of_found_administrators.value);">
                    <label for="input_to_find_administrator_by_email">
                        Find administrator by email:
                    </label>
                    <input id="input_to_find_administrator_by_email" type="text" name="email_of_found_administrators" />
                    <input type="submit" value="Find" />
                </form>

                <table>

                    <jstl-core:url var="link_to_sort_by_email" value="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                        + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                        <jstl-core:param name="<%= AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= AdministratorSortingKey.EMAIL.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_password" value="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                           + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                        <jstl-core:param name="<%= AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= AdministratorSortingKey.PASSWORD.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_level" value="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                        + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                        <jstl-core:param name="<%= AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= AdministratorSortingKey.LEVEL.name() %>" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_sort_by_deleted" value="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                          + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                        <jstl-core:param name="<%= AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY %>"
                                         value="<%= AdministratorSortingKey.DELETED.name() %>" />
                    </jstl-core:url>

                    <tr>
                        <th><a href="${link_to_sort_by_email}">Email</a></th>
                        <th><a href="${link_to_sort_by_password}">Password</a></th>
                        <th><a href="${link_to_sort_by_level}">Level</a></th>
                        <th><a href="${link_to_sort_by_deleted}">Deleted</a></th>
                        <th>Operation</th>
                    </tr>

                    <jsp:useBean id="listed_administrators" scope="request" type="java.util.Collection" />
                    <jstl-core:forEach var="listed_administrator" items="${listed_administrators}">

                        <jstl-core:url var="link_to_update_administrator" value="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                   + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_ADMINISTRATOR %>">
                            <jstl-core:param name="<%= AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_ADMINISTRATOR %>"
                                             value="${listed_administrator.id}" />
                        </jstl-core:url>

                        <jstl-core:url var="link_to_delete_administrator" value="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                   + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_ADMINISTRATOR %>">
                            <jstl-core:param name="<%= AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_ADMINISTRATOR %>"
                                             value="${listed_administrator.id}" />
                        </jstl-core:url>

                        <tr>
                            <td>${listed_administrator.email}</td>
                            <td>${listed_administrator.password}</td>
                            <td>${listed_administrator.level}</td>
                            <td>${listed_administrator.deleted}</td>

                            <td>
                                <a href="${link_to_update_administrator}">Update</a>

                                <%= DELIMITER_OF_OPERATIONS.getValue() %>

                                <a href="${link_to_delete_administrator}"
                                   onclick="return (confirm('Are you sure you want to delete this administrator?'))">
                                    Delete
                                </a>
                            </td>

                        </tr>

                    </jstl-core:forEach>

                </table>

            </div>
        </div>

        <br />

        <i>Hint: to sort administrators click on header of table</i>

        <br />

        <div class="error">
            <%= request.getAttribute(AdministratorOperationController.NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR) != null
                    ? request.getAttribute(AdministratorOperationController.NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR) : "" %>
        </div>

    </body>

</html>