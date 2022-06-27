<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.PaymentCardOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Add new payment card</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/expiration_date_of_payment_card_validator.js"></script>
        <script src="${pageContext.request.contextPath}/resources/script/user_email_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Add new payment card</h2>
            </div>
        </div>

        <div id="container">

            <spring-form:form name="form_to_add_payment_card" acceptCharset="UTF-8" method="POST"
                              onsubmit="return checkValidOfExpirationDateAndAlertIfNotValid(
                                    document.form_to_add_payment_card.description_of_expiration_date_of_added_payment_card.value)
                                    && checkValidOfEmailOfUserAndAlertIfNotValid(
                                    document.form_to_add_payment_card.email_of_client_of_added_payment_card.value);"
                              modelAttribute="<%= PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT_CARD %>"
                              action="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT_CARD %>">

                <table>
                    <tbody>
                        <tr>
                            <td><spring-form:label path="cardNumber">Card's number:</spring-form:label></td>
                            <td>
                                <spring-form:input path="cardNumber" />
                                <spring-form:errors path="cardNumber" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><label for="input_of_expiration_date_of_payment_card">Expiration date:</label></td>
                            <td>
                                <input id="input_of_expiration_date_of_payment_card" type="text" required="required"
                                       name="description_of_expiration_date_of_added_payment_card" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="paymentSystem">Payment system:</spring-form:label></td>
                            <td>
                                <spring-form:input path="paymentSystem" />
                                <spring-form:errors path="paymentSystem" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="cvc">Cvc:</spring-form:label></td>
                            <td>
                                <spring-form:input path="cvc" />
                                <spring-form:errors path="cvc" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><label for="input_of_email_of_client_of_payment_card">Client's email:</label></td>
                            <td>
                                <input id="input_of_email_of_client_of_payment_card" type="email" required="required"
                                       name="email_of_client_of_added_payment_card" />
                                <spring-form:errors path="client" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="nameOfBank">Name of bank:</spring-form:label></td>
                            <td>
                                <spring-form:input path="nameOfBank" />
                                <spring-form:errors path="nameOfBank" cssClass="error" />
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
                            <%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                    Cancel
                </a>
            </p>

        </div>

    </body>

</html>