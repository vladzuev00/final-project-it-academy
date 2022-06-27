<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.AdministratorOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>

    <head>
        <title>Add new administrator</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Add new administrator</h2>
            </div>
        </div>

        <div id="container">

            <spring-form:form action="<%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_ADMINISTRATOR %>"
                              modelAttribute="<%= AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_ADMINISTRATOR %>"
                              method="post" acceptCharset="UTF-8">
                
                <table>
                    <tbody>
                        <tr>
                            <td><spring-form:label path="email">Email:</spring-form:label></td>
                            <td>
                                <spring-form:input path="email" />
                                <spring-form:errors path="email" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="password">Password:</spring-form:label></td>
                            <td>
                                <spring-form:input path="password" />
                                <spring-form:errors path="password" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="level">Level:</spring-form:label></td>
                            <td>
                                <spring-form:select path="level">
                                    <spring-form:options items="${administrator_levels_and_their_descriptions}" />
                                </spring-form:select>
                                <spring-form:errors path="level" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="deleted">Deleted:</spring-form:label></td>
                            <td>
                                <spring-form:radiobutton path="deleted" value="true" />
                                <spring-form:radiobutton path="deleted" value="false" hidden="hidden" checked="checked" />
                            </td>
                        </tr>
                        <tr>
                            <td><label></label></td>
                            <td><input type="submit" value="Add" /></td>
                        </tr>
                    </tbody>
                </table>
                
            </spring-form:form>

            <br />
            <p>
                <a href="${pageContext.request.contextPath}
                            <%= AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS %>">
                    Cancel
                </a>
            </p>

        </div>

    </body>

</html>