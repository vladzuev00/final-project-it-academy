<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientProfileController" %>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Update personal data</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Update personal data</h2>
            </div>
        </div>

        <div id="container">
            <spring-form:form acceptCharset="UTF-8" modelAttribute="personal_data_dto" method="POST"
                              action="<%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                                       +  ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PERSONAL_DATA_OF_CLIENT %>">
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
                            <%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT %>">
                Cancel
            </a>
        </p>

    </body>

</html>