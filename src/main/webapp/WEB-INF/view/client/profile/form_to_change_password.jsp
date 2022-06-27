<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientProfileController" %>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>

    <head>
        <title>Change password</title>
        <script src="${pageContext.request.contextPath}/resources/script/user_password_validator.js"></script>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/general_style.css" />
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Change password</h2>
            </div>
        </div>

        <form name="form_to_change_password" accept-charset="UTF-8" method="POST"
              action="<%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                        + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_CHANGE_PASSWORD %>"
              onsubmit="return checkValidDataInFormToChangePasswordAndAlertIfNot(
                  document.form_to_change_password.old_password.value,
                  document.form_to_change_password.new_password.value,
                  document.form_to_change_password.repeated_new_password.value);">
            <table>
                <tbody>
                    <tr>
                        <td><label for="input_for_old_password">Your old password:</label></td>
                        <td>
                            <input id="input_for_old_password" type="password" name="old_password" required="required" />
                        </td>
                    </tr>
                    <tr>
                        <td><label for="input_for_new_password">Your new password:</label></td>
                        <td>
                            <input id="input_for_new_password" type="password" name="new_password" required="required" />
                        </td>
                    </tr>
                    <tr>
                        <td><label for="input_for_repeated_new_password">Repeat your new password</label></td>
                        <td>
                            <input id="input_for_repeated_new_password" type="password" name="repeated_new_password"
                                   required="required" />
                        </td>
                    </tr>
                    <tr>
                        <td><label></label></td>
                        <td><input type="submit" value="Change" /></td>
                    </tr>
                </tbody>
            </table>
        </form>

        <br />

        <p>
            <a href="${pageContext.request.contextPath}
                            <%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                              + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT %>">
                Cancel
            </a>
        </p>

        <br />

        <%
            final String messageOfError = (String)request.getAttribute(ClientProfileController.NAME_OF_HTTP_REQUEST_ATTRIBUTE_OF_MESSAGE_OF_ERROR);
        %>

        <p class="error">
            <%= messageOfError != null ? messageOfError : "" %>
        </p>

    </body>

</html>