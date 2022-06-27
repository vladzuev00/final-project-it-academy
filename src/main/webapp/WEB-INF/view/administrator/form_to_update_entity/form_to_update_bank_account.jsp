<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.BankAccountOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Update bank account</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/card_number_of_payment_card_validator.js"></script>
        <script src="${pageContext.request.contextPath}/resources/script/date_of_payment_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Update bank account</h2>
            </div>
        </div>

        <div id="container">
            <spring-form:form acceptCharset="UTF-8" action="<%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                              + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_BANK_ACCOUNT %>"
                              modelAttribute="updated_bank_account"
                              method="POST">
                <spring-form:hidden path="id" value="${updated_bank_account.id}" />
                <table>
                    <tbody>
                        <tr>
                            <td><spring-form:label path="money">New money:</spring-form:label></td>
                            <td>
                                <spring-form:input path="money" />
                                <spring-form:errors path="money" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="blocked">New blocked:</spring-form:label></td>
                            <td>
                                <spring-form:radiobutton path="blocked" value="true" />
                                <spring-form:radiobutton path="blocked" value="false" hidden="hidden" checked="checked" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="number">New number:</spring-form:label></td>
                            <td>
                                <spring-form:input path="number" />
                                <spring-form:errors path="number" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="deleted">New deleted:</spring-form:label></td>
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
            </spring-form:form>
        </div>

        <br />

        <p>
            <a href="${pageContext.request.contextPath}
                            <%= BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS %>">
                Cancel
            </a>
        </p>

    </body>

</html>