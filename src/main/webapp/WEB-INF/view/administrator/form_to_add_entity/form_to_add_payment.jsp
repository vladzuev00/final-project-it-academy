<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.PaymentOperationController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Add new payment</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
        <script src="${pageContext.request.contextPath}/resources/script/card_number_of_payment_card_validator.js"></script>
        <script src="${pageContext.request.contextPath}/resources/script/date_of_payment_validator.js"></script>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Add new payment</h2>
            </div>
        </div>

        <div id="container">
            <spring-form:form action="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT %>"
                              modelAttribute="<%= PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT %>"
                              method="POST" acceptCharset="UTF-8" name="form_to_add_payment"
                              onsubmit="return checkValidOfCardNumberOfPaymentCardAndAlertIfNotValid(
                                                    document.form_to_add_payment.card_number_of_sender.value)
                              && checkValidOfCardNumberOfPaymentCardAndAlertIfNotValid(
                                    document.form_to_add_payment.card_number_of_receiver.value)
                              && checkValidOfDescriptionOfDateOfPaymentAndAlertIfNotValid(
                                    document.form_to_add_payment.date_of_added_payment.value);">

                <table>
                    <tbody>
                        <tr>
                            <td>
                                <label for="input_of_card_number_of_sender">Card's number of sender:</label>
                            </td>
                            <td>
                                <input id="input_of_card_number_of_sender" type="text" name="card_number_of_sender"
                                       required="required" />
                                <spring-form:errors path="cardOfSender" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="input_of_card_number_of_receiver">Card's number of receiver:</label>
                            </td>
                            <td>
                                <input id="input_of_card_number_of_receiver" type="text" name="card_number_of_receiver"
                                       required="required" />
                                <spring-form:errors path="cardOfReceiver" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td><spring-form:label path="money">Money:</spring-form:label></td>
                            <td>
                                <spring-form:input path="money" />
                                <spring-form:errors path="money" cssClass="error" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="input_of_date_of_added_payment">Date:</label>
                            </td>
                            <td>
                                <input id="input_of_date_of_added_payment" type="text" name="date_of_added_payment"
                                       required="required" />
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
                            <%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                    Cancel
                </a>
            </p>

        </div>

    </body>

</html>