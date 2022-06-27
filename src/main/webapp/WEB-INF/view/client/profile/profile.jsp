<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientProfileController" %>
<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientOperationController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Profile</title>
    </head>

    <body>

        <jsp:useBean id="logged_on_user" type="by.itacademy.zuevvlad.cardpaymentproject.entity.Client" scope="session" />

        <table>
            <thead>Your personal data</thead>
            <tbody>
                <tr>
                    <td>Your email:</td>
                    <td>${logged_on_user.email}</td>
                </tr>
                <tr>
                    <td>Your name:</td>
                    <td>${logged_on_user.name}</td>
                </tr>
                <tr>
                    <td>Your surname:</td>
                    <td>${logged_on_user.surname}</td>
                </tr>
                <tr>
                    <td>Your patronymic:</td>
                    <td>${logged_on_user.patronymic}</td>
                </tr>
                <tr>
                    <td>Your phone number:</td>
                    <td>${logged_on_user.phoneNumber}</td>
                </tr>
                <tr>
                    <td>
                        <jstl-core:url var="link_to_update_personal_data" value="<%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                                                   + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PERSONAL_DATA_OF_CLIENT %>" />
                        <a href="${link_to_update_personal_data}">
                            Update
                        </a>
                    </td>
                    <td>
                        <jstl-core:url var="link_to_change_password"
                                       value="<%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                                + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_CHANGE_PASSWORD %>" />
                        <a href="${link_to_change_password}">
                            Change password
                        </a>
                    </td>
                </tr>
            </tbody>
        </table>

        <br />

        <table>
            <thead>Data of your bank account</thead>
            <tbody>
                <tr>
                    <td>Money:</td>
                    <td>${logged_on_user.bankAccount.money}</td>
                </tr>
                <tr>
                    <td>Status of blocking:</td>
                    <td>${logged_on_user.bankAccount.blocked}</td>
                </tr>
                <tr>
                    <td>Number:</td>
                    <td>${logged_on_user.bankAccount.number}</td>
                </tr>
            </tbody>
        </table>

        <br />

        <jsp:useBean id="not_deleted_cards_of_logged_on_client" type="java.util.Collection" scope="request" />
        <ol>
            <jstl-core:forEach var="listed_card" items="${not_deleted_cards_of_logged_on_client}">
                <li>
                    <table>
                        <tr>
                            <td>Card number:</td>
                            <td>${listed_card.cardNumber}</td>
                        </tr>
                        <tr>
                            <td>Expiration date:</td>
                            <td>${listed_card.expirationDate.month}/${listed_card.expirationDate.year}</td>
                        </tr>
                        <tr>
                            <td>Payment system:</td>
                            <td>${listed_card.paymentSystem}</td>
                        </tr>
                        <tr>
                            <td>Name of bank:</td>
                            <td>${listed_card.nameOfBank}</td>
                        </tr>
                    </table>
                </li>
            </jstl-core:forEach>
        </ol>

        <br />
        <p>
            <a href="${pageContext.request.contextPath}
                    <%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                      + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_STATISTICS_OF_LOGGED_ON_CLIENT %>">
                Your statistics
            </a>
        </p>

        <br />

        <p>
            <a href="${pageContext.request.contextPath}
                     <%= ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                       + ClientOperationController.PATH_OF_REQUEST_PARAM_OF_MAIN_CLIENT_PAGE %>">
                main page
            </a>
        </p>

    </body>

</html>