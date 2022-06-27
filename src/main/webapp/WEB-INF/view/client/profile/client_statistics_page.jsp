<%@ page import="by.itacademy.zuevvlad.cardpaymentproject.controller.client.ClientProfileController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Statistics</title>
    </head>

    <body>

        <div id="header_wrapper">
            <div id="header">
                <h2>Your statistics</h2>
            </div>
        </div>

        <jsp:useBean id="client_statistics_holder" scope="request"
                     type="by.itacademy.zuevvlad.cardpaymentproject.service.statisticsholder.ClientStatisticsHolder" />
        <ol>
            <li>
                <table>
                    <thead>General statistics</thead>
                    <tbody>
                        <tr>
                            <td>Total of received money:</td>
                            <td>${client_statistics_holder.totalReceivedMoney}</td>
                        </tr>
                        <tr>
                            <td>Total of sent money:</td>
                            <td>${client_statistics_holder.totalSentMoney}</td>
                        </tr>
                        <tr>
                            <td>Average of received money:</td>
                            <td>${client_statistics_holder.averageReceivedMoney}</td>
                        </tr>
                        <tr>
                            <td>Average of sent money:</td>
                            <td>${client_statistics_holder.averageSentMoney}</td>
                        </tr>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of received money by each card</thead>
                    <tbody>
                        <tr>
                            <th>Card number</th>
                            <th>Expiration date</th>
                            <th>Payment system</th>
                            <th>Name of bank</th>
                            <th>Amount of received money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.paymentCardOfClientToTotalReceivedMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().cardNumber}</td>
                                <td>${entry.getKey().expirationDate.month}/${entry.getKey().expirationDate.year}</td>
                                <td>${entry.getKey().paymentSystem}</td>
                                <td>${entry.getKey().nameOfBank}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of sent money by each card</thead>
                    <tbody>
                        <tr>
                            <th>Card number</th>
                            <th>Expiration date</th>
                            <th>Payment system</th>
                            <th>Name of bank</th>
                            <th>Amount of sent money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.paymentCardOfClientToTotalSentMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().cardNumber}</td>
                                <td>${entry.getKey().expirationDate.month}/${entry.getKey().expirationDate.year}</td>
                                <td>${entry.getKey().paymentSystem}</td>
                                <td>${entry.getKey().nameOfBank}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of average received money by each card</thead>
                    <tbody>
                        <tr>
                            <th>Card number</th>
                            <th>Expiration date</th>
                            <th>Payment system</th>
                            <th>Name of bank</th>
                            <th>Amount of average received money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.paymentCardOfClientToAverageReceivedMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().cardNumber}</td>
                                <td>${entry.getKey().expirationDate.month}/${entry.getKey().expirationDate.year}</td>
                                <td>${entry.getKey().paymentSystem}</td>
                                <td>${entry.getKey().nameOfBank}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of average sent money by each card</thead>
                    <tbody>
                        <tr>
                            <th>Card number</th>
                            <th>Expiration date</th>
                            <th>Payment system</th>
                            <th>Name of bank</th>
                            <th>Amount of average sent money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.paymentCardOfClientToAverageSentMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().cardNumber}</td>
                                <td>${entry.getKey().expirationDate.month}/${entry.getKey().expirationDate.year}</td>
                                <td>${entry.getKey().paymentSystem}</td>
                                <td>${entry.getKey().nameOfBank}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of total received money from each other clients</thead>
                    <tbody>
                        <tr>
                            <th>Sender's email</th>
                            <th>Sender's name</th>
                            <th>Sender's surname</th>
                            <th>Sender's patronymic</th>
                            <th>Sender's phone number</th>
                            <th>Amount of total received money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.senderToTotalReceivedMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().email}</td>
                                <td>${entry.getKey().name}</td>
                                <td>${entry.getKey().surname}</td>
                                <td>${entry.getKey().patronymic}</td>
                                <td>${entry.getKey().phoneNumber}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of total sent money to each other clients</thead>
                    <tbody>
                        <tr>
                            <th>Receiver's email</th>
                            <th>Receiver's name</th>
                            <th>Receiver's surname</th>
                            <th>Receiver's patronymic</th>
                            <th>Receiver's phone number</th>
                            <th>Amount of total sent money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.receiverToTotalSentMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().email}</td>
                                <td>${entry.getKey().name}</td>
                                <td>${entry.getKey().surname}</td>
                                <td>${entry.getKey().patronymic}</td>
                                <td>${entry.getKey().phoneNumber}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of average received money from each other clients</thead>
                    <tbody>
                        <tr>
                            <th>Sender's email</th>
                            <th>Sender's name</th>
                            <th>Sender's surname</th>
                            <th>Sender's patronymic</th>
                            <th>Sender's phone number</th>
                            <th>Amount of average received money</th>
                        </tr>
                        <jstl-core:forEach var="entry" items="${client_statistics_holder.senderToAverageReceivedMoney.entrySet()}">
                            <tr>
                                <td>${entry.getKey().email}</td>
                                <td>${entry.getKey().name}</td>
                                <td>${entry.getKey().surname}</td>
                                <td>${entry.getKey().patronymic}</td>
                                <td>${entry.getKey().phoneNumber}</td>
                                <td>${entry.getValue()}</td>
                            </tr>
                        </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
            <li>
                <table>
                    <thead>Amount of average sent money to each other clients</thead>
                    <tbody>
                    <tr>
                        <th>Receiver's email</th>
                        <th>Receiver's name</th>
                        <th>Receiver's surname</th>
                        <th>Receiver's patronymic</th>
                        <th>Receiver's phone number</th>
                        <th>Amount of average sent money</th>
                    </tr>
                    <jstl-core:forEach var="entry" items="${client_statistics_holder.receiverToAverageSentMoney.entrySet()}">
                        <tr>
                            <td>${entry.getKey().email}</td>
                            <td>${entry.getKey().name}</td>
                            <td>${entry.getKey().surname}</td>
                            <td>${entry.getKey().patronymic}</td>
                            <td>${entry.getKey().phoneNumber}</td>
                            <td>${entry.getValue()}</td>
                        </tr>
                    </jstl-core:forEach>
                    </tbody>
                </table>
            </li>
        </ol>

        <p>
            <a href="${pageContext.request.contextPath}
                     <%= ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                       + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT %>">
                back to profile
            </a>
        </p>

    </body>

</html>