<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.BankAccountOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Add new bank account</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Add new bank account</h2>
            </div>
        </div>

        <div id="container">
            <spring-form:form action="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_BANK_ACCOUNT %>"
                              modelAttribute="<%= BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_BANK_ACCOUNT %>"
                              method="POST" acceptCharset="UTF-8">

                <table>
                    <tbody>
                        <tr>
                            <td><spring-form:label path="money">Money:</spring-form:label></td>
                            <td>
                                <spring-form:input path="money" />
                                <spring-form:errors path="money" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="blocked">Blocked:</spring-form:label></td>
                            <td>
                                <spring-form:radiobutton path="blocked" value="true" />
                                <spring-form:radiobutton path="blocked" value="false" hidden="hidden" checked="checked" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="number">Number:</spring-form:label></td>
                            <td>
                                <spring-form:input path="number" />
                                <spring-form:errors path="number" cssClass="error" />
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
                            <%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                    Cancel
                </a>
            </p>

        </div>

    </body>

</html>