<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.ClientOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Update client</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/bank_account_number_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Update client</h2>
            </div>
        </div>

        <div id="container">
            <spring-form:form acceptCharset="UTF-8" modelAttribute="updated_client" method="POST"
                              action="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                    + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_CLIENT %>"
                              name="form_to_update_client"
                              onsubmit="return checkValidOfNumberOfBankAccountAndAlertIfNotValid(
                                document.form_to_update_client.number_of_new_bank_account_of_updated_client.value);">

                <spring-form:hidden path="id" value="${updated_client.id}" hidden="hidden" />
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
                            <td><spring-form:label path="deleted">New deleted:</spring-form:label></td>
                            <td>
                                <spring-form:radiobutton path="deleted" value="true" />
                                <spring-form:radiobutton path="deleted" value="false" hidden="hidden" checked="checked" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="name">New name:</spring-form:label></td>
                            <td>
                                <spring-form:input path="name" />
                                <spring-form:errors path="name" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="surname">New surname:</spring-form:label></td>
                            <td>
                                <spring-form:input path="surname" />
                                <spring-form:errors path="surname" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="patronymic">New patronymic:</spring-form:label></td>
                            <td>
                                <spring-form:input path="patronymic" />
                                <spring-form:errors path="patronymic" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="phoneNumber">New phone number:</spring-form:label></td>
                            <td>
                                <spring-form:input path="phoneNumber" />
                                <spring-form:errors path="phoneNumber" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="input_for_number_of_new_bank_account">Number of new bank account</label>
                            </td>
                            <td>
                                <input id="input_for_number_of_new_bank_account" type="text" required="required"
                                       value="${updated_client.bankAccount.number}"
                                       name="number_of_new_bank_account_of_updated_client" />
                                <spring-form:errors path="bankAccount" cssClass="error" />
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
                            <%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                Cancel
            </a>
        </p>

    </body>

</html>