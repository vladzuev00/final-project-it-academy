<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.administrator.PaymentOperationController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.CalendarHandler" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Update payment</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Update payment</h2>
            </div>
        </div>

        <div id="container">
            <spring-form:form acceptCharset="UTF-8" modelAttribute="updated_payment" method="POST"
                              name="form_to_update_payment"
                              action="<%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                        + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT %>"
                              onsubmit="return checkValidOfCardNumberOfPaymentCardAndAlertIfNotValid(
                                                    document.form_to_update_payment.card_number_of_sender.value)
                                            && checkValidOfCardNumberOfPaymentCardAndAlertIfNotValid(
                                                    document.form_to_update_payment.card_number_of_receiver.value)
                                            && checkValidOfExpirationDateAndAlertIfNotValid(
                                                    document.form_to_update_payment.date_of_updated_payment.value);">
                <jsp:useBean id="updated_payment" scope="request" type="by.itacademy.zuevvlad.cardpaymentproject.entity.Payment"/>
                <spring-form:hidden path="id" value="${updated_payment.id}" />
                    <table>
                        <tbody>
                            <tr>
                                <td>
                                    <label for="input_of_card_number_of_sender">Card's number of new sender:</label>
                                </td>
                                <td>
                                    <input id="input_of_card_number_of_sender" type="text" name="card_number_of_sender"
                                           required="required" value="${updated_payment.cardOfSender.cardNumber}" />
                                    <spring-form:errors path="cardOfSender" cssClass="error" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="input_of_card_number_of_receiver">Card's number of new receiver:</label>
                                </td>
                                <td>
                                    <input id="input_of_card_number_of_receiver" type="text" name="card_number_of_receiver"
                                           required="required" value="${updated_payment.cardOfReceiver.cardNumber}" />
                                    <spring-form:errors path="cardOfReceiver" cssClass="error" />
                                </td>
                            </tr>
                            <tr>
                                <td><spring-form:label path="money">New money:</spring-form:label></td>
                                <td>
                                    <spring-form:input path="money" />
                                    <spring-form:errors path="money" cssClass="error" />
                                </td>
                            </tr>
                            <tr>
                                <td><label for="input_of_date_of_updated_payment">New date:</label></td>
                                <td>
                                    <% CalendarHandler calendarHandler = CalendarHandler.createCalendarHandler(); %>
                                    <input id="input_of_date_of_updated_payment" type="text" name="date_of_updated_payment"
                                           required="required" value="<%= calendarHandler.findDescriptionOfCalendar(updated_payment.getDate()) %>" />
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
                            <%= PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS %>">
                Cancel
            </a>
        </p>

    </body>

</html>