<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.ClientOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Add new administrator</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/bank_account_number_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Add new client</h2>
            </div>
        </div>

        <spring-form:form acceptCharset="UTF-8" modelAttribute="<%= ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT %>"
                          method="post" action="<%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                  + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_CLIENT %>"
                          name = "form_to_add_client"
                          onsubmit="return checkValidOfNumberOfBankAccountAndAlertIfNotValid(
                            document.form_to_add_client.number_of_bank_account_of_added_client.value);">

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
                        <td><spring-form:label path="name">Name:</spring-form:label></td>
                        <td>
                            <spring-form:input path="name" />
                            <spring-form:errors path="name" cssClass="error" />
                        </td>
                    </tr>
                    <tr>
                        <td><spring-form:label path="surname">Surname:</spring-form:label></td>
                        <td>
                            <spring-form:input path="surname" />
                            <spring-form:errors path="surname" cssClass="error" />
                        </td>
                    </tr>
                    <tr>
                        <td><spring-form:label path="patronymic">Patronymic:</spring-form:label></td>
                        <td>
                            <spring-form:input path="patronymic" />
                            <spring-form:errors path="patronymic" cssClass="error" />
                        </td>
                    </tr>
                    <tr>
                        <td><spring-form:label path="phoneNumber">Phone number:</spring-form:label></td>
                        <td>
                            <spring-form:input path="phoneNumber" />
                            <spring-form:errors path="phoneNumber" cssClass="error" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="input_for_number_of_bank_account_of_added_client">Bank account's number:</label>
                        </td>
                        <td>
                            <input id="input_for_number_of_bank_account_of_added_client" type="text" required="required"
                                   name="number_of_bank_account_of_added_client" />
                            <spring-form:errors path="bankAccount" cssClass="error" />
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
                            <%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS %>">
                Cancel
            </a>
        </p>

    </body>

</html>