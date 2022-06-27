<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.PaymentCardOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>

    <head>
        <title>Update payment card</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/expiration_date_of_payment_card_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Update payment card</h2>
            </div>
        </div>

        <div id="container">

            <spring-form:form name="form_to_update_payment_card" acceptCharset="UTF-8" modelAttribute="updated_payment_card" method="POST"
                              action="<%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT_CARD %>"
                              onsubmit="return checkValidOfExpirationDateAndAlertIfNotValid(document.form_to_update_payment_card
                                .description_of_expiration_date_of_updated_payment_card.value)
                                && checkValidOfEmailOfUserAndAlertIfNotValid(document.form_to_update_payment_card
                                    .email_of_client_of_updated_payment_card.value);">

                <spring-form:hidden path="id" value="${updated_payment_card.id}" hidden="hidden" />
                <table>
                    <tbody>
                        <tr>
                            <td><spring-form:label path="cardNumber">New card's number:</spring-form:label></td>
                            <td>
                                <spring-form:input path="cardNumber" />
                                <spring-form:errors path="cardNumber" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><label for="input_for_new_expiration_date">New expiration date:</label></td>
                            <td>
                                <input id="input_for_new_expiration_date" type="text" required="required"
                                       name="description_of_expiration_date_of_updated_payment_card"
                                       value="${updated_payment_card.expirationDate.month}/${updated_payment_card.expirationDate.year}" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="paymentSystem">New payment system:</spring-form:label></td>
                            <td>
                                <spring-form:input path="paymentSystem" />
                                <spring-form:errors path="paymentSystem" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="cvc">New cvc:</spring-form:label></td>
                            <td>
                                <spring-form:input path="cvc" />
                                <spring-form:errors path="cvc" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><label for="input_for_email_of_new_client">New client's email:</label></td>
                            <td>
                                <input id="input_for_email_of_new_client" type="email" required="required"
                                       name="email_of_client_of_updated_payment_card" value="${updated_payment_card.client.email}" />
                                <spring-form:errors path="client" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="nameOfBank">New name of bank:</spring-form:label></td>
                            <td>
                                <spring-form:input path="nameOfBank" />
                                <spring-form:errors path="nameOfBank" cssClass="error" />
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
                            <%= PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS %>">
                Cancel
            </a>
        </p>

    </body>

</html>