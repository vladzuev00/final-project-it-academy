<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Update administrator</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Update administrator</h2>
            </div>
        </div>

        <div id="container">

            <spring-form:form acceptCharset="UTF-8" action="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                          + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_ADMINISTRATOR %>"
                              modelAttribute="updated_administrator"
                              method="POST">
                <spring-form:hidden path="id" value="${updated_administrator.id}" />
                <table>
                    <tbody>
                        <tr>
                            <td><spring-form:label path="email">New email:</spring-form:label></td>
                            <td>
                                <spring-form:input path="email" />
                                <spring-form:errors path="email" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="password">New password:</spring-form:label></td>
                            <td>
                                <spring-form:input path="password" />
                                <spring-form:errors path="password" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="level">New level:</spring-form:label></td>
                            <td>
                                <spring-form:select path="level">
                                    <spring-form:options items="${administrator_levels_and_their_descriptions}" />
                                </spring-form:select>
                                <spring-form:errors path="level" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td>New deleted:</td>
                            <td>
                                <spring-form:radiobutton path="deleted" value="true" />
                                <spring-form:radiobutton path="deleted" value="false" hidden="hidden" checked="checked" />
                            </td>
                        </tr>
                        <tr>
                            <td><label></label></td>
                            <td><input type="submit" value="Update" /></td>
                        </tr>
                    </tbody>
                </table>

                <div class="error">
                    <%= request.getAttribute(AdministratorOperationController.NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR) != null
                            ? request.getAttribute(AdministratorOperationController.NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR) : "" %>
                </div>

            </spring-form:form>

        </div>

        <br />

        <p>
            <a href="${pageContext.request.contextPath}
                            <%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                Cancel
            </a>
        </p>

    </body>

</html>