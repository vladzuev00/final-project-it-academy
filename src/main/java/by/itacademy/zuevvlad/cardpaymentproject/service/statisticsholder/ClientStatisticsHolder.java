package by.itacademy.zuevvlad.cardpaymentproject.service.statisticsholder;

import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ClientStatisticsHolder implements StatisticsHolder
{
    private double totalReceivedMoney;
    private double totalSentMoney;
    private double averageReceivedMoney;
    private double averageSentMoney;
    private Map<PaymentCard, Double> paymentCardOfClientToTotalReceivedMoney;
    private Map<PaymentCard, Double> paymentCardOfClientToTotalSentMoney;
    private Map<PaymentCard, Double> paymentCardOfClientToAverageReceivedMoney;
    private Map<PaymentCard, Double> paymentCardOfClientToAverageSentMoney;
    private Map<Client, Double> senderToTotalReceivedMoney;
    private Map<Client, Double> receiverToTotalSentMoney;
    private Map<Client, Double> senderToAverageReceivedMoney;
    private Map<Client, Double> receiverToAverageSentMoney;

    public ClientStatisticsHolder()
    {
        super();
        this.totalReceivedMoney = ClientStatisticsHolder.VALUE_OF_NOT_DEFINED_TOTAL_RECEIVED_MONEY;
        this.totalSentMoney = ClientStatisticsHolder.VALUE_OF_NOT_DEFINED_TOTAL_SENT_MONEY;
        this.averageReceivedMoney = ClientStatisticsHolder.VALUE_OF_NOT_DEFINED_AVERAGE_RECEIVED_MONEY;
        this.averageSentMoney = ClientStatisticsHolder.VALUE_OF_NOT_DEFINED_AVERAGE_SENT_MONEY;
        this.paymentCardOfClientToTotalReceivedMoney = new HashMap<PaymentCard, Double>();
        this.paymentCardOfClientToTotalSentMoney = new HashMap<PaymentCard, Double>();
        this.paymentCardOfClientToAverageReceivedMoney = new HashMap<PaymentCard, Double>();
        this.paymentCardOfClientToAverageSentMoney = new HashMap<PaymentCard, Double>();
        this.senderToTotalReceivedMoney = new HashMap<Client, Double>();
        this.receiverToTotalSentMoney = new HashMap<Client, Double>();
        this.senderToAverageReceivedMoney = new HashMap<Client, Double>();
        this.receiverToAverageSentMoney = new HashMap<Client, Double>();
    }

    private static final double VALUE_OF_NOT_DEFINED_TOTAL_RECEIVED_MONEY = 0;
    private static final double VALUE_OF_NOT_DEFINED_TOTAL_SENT_MONEY = 0;
    private static final double VALUE_OF_NOT_DEFINED_AVERAGE_RECEIVED_MONEY = 0;
    private static final double VALUE_OF_NOT_DEFINED_AVERAGE_SENT_MONEY = 0;

    public ClientStatisticsHolder(final double inTotalReceivedMoney, final double inTotalSentMoney,
                                  final double averageReceivedMoney, final double averageSentMoney,
                                  final Map<PaymentCard, Double> paymentCardOfClientToTotalReceivedMoney,
                                  final Map<PaymentCard, Double> paymentCardOfClientToTotalSentMoney,
                                  final Map<PaymentCard, Double> paymentCardOfClientToAverageReceivedMoney,
                                  final Map<PaymentCard, Double> paymentCardOfClientToAverageSentMoney,
                                  final Map<Client, Double> senderToTotalReceivedMoney,
                                  final Map<Client, Double> receiverToTotalSentMoney,
                                  final Map<Client, Double> senderToAverageReceivedMoney,
                                  final Map<Client, Double> receiverToAverageSentMoney)
    {
        super();
        this.totalReceivedMoney = inTotalReceivedMoney;
        this.totalSentMoney = inTotalSentMoney;
        this.averageReceivedMoney = averageReceivedMoney;
        this.averageSentMoney = averageSentMoney;
        this.paymentCardOfClientToTotalReceivedMoney = paymentCardOfClientToTotalReceivedMoney;
        this.paymentCardOfClientToTotalSentMoney = paymentCardOfClientToTotalSentMoney;
        this.paymentCardOfClientToAverageReceivedMoney = paymentCardOfClientToAverageReceivedMoney;
        this.paymentCardOfClientToAverageSentMoney = paymentCardOfClientToAverageSentMoney;
        this.senderToTotalReceivedMoney = senderToTotalReceivedMoney;
        this.receiverToTotalSentMoney = receiverToTotalSentMoney;
        this.senderToAverageReceivedMoney = senderToAverageReceivedMoney;
        this.receiverToAverageSentMoney = receiverToAverageSentMoney;
    }

    public final void setTotalReceivedMoney(final double totalReceivedMoney)
    {
        this.totalReceivedMoney = totalReceivedMoney;
    }

    public final double getTotalReceivedMoney()
    {
        return this.totalReceivedMoney;
    }

    public final void setTotalSentMoney(final double totalSentMoney)
    {
        this.totalSentMoney = totalSentMoney;
    }

    public final double getTotalSentMoney()
    {
        return this.totalSentMoney;
    }

    public final void setAverageReceivedMoney(final double averageReceivedMoney)
    {
        this.averageReceivedMoney = averageReceivedMoney;
    }

    public final double getAverageReceivedMoney()
    {
        return this.averageReceivedMoney;
    }

    public final void setAverageSentMoney(final double averageSentMoney)
    {
        this.averageSentMoney = averageSentMoney;
    }

    public final double getAverageSentMoney()
    {
        return this.averageSentMoney;
    }

    public final void setPaymentCardOfClientToTotalReceivedMoney(
            final Map<PaymentCard, Double> paymentCardOfClientToTotalReceivedMoney)
    {
        this.paymentCardOfClientToTotalReceivedMoney = paymentCardOfClientToTotalReceivedMoney;
    }

    public final Map<PaymentCard, Double> getPaymentCardOfClientToTotalReceivedMoney()
    {
        return this.paymentCardOfClientToTotalReceivedMoney;
    }

    public final void setPaymentCardOfClientToTotalSentMoney(
            final Map<PaymentCard, Double> paymentCardOfClientToTotalSentMoney)
    {
        this.paymentCardOfClientToTotalSentMoney = paymentCardOfClientToTotalSentMoney;
    }

    public final Map<PaymentCard, Double> getPaymentCardOfClientToTotalSentMoney()
    {
        return this.paymentCardOfClientToTotalSentMoney;
    }

    public final void setPaymentCardOfClientToAverageReceivedMoney(
            final Map<PaymentCard, Double> paymentCardOfClientToAverageReceivedMoney)
    {
        this.paymentCardOfClientToAverageReceivedMoney = paymentCardOfClientToAverageReceivedMoney;
    }

    public final Map<PaymentCard, Double> getPaymentCardOfClientToAverageReceivedMoney()
    {
        return this.paymentCardOfClientToAverageReceivedMoney;
    }

    public final void setPaymentCardOfClientToAverageSentMoney(
            final Map<PaymentCard, Double> paymentCardOfClientToAverageSentMoney)
    {
        this.paymentCardOfClientToAverageSentMoney = paymentCardOfClientToAverageSentMoney;
    }

    public final Map<PaymentCard, Double> getPaymentCardOfClientToAverageSentMoney()
    {
        return this.paymentCardOfClientToAverageSentMoney;
    }

    public final void setSenderToTotalReceivedMoney(final Map<Client, Double> senderToTotalReceivedMoney)
    {
        this.senderToTotalReceivedMoney = senderToTotalReceivedMoney;
    }

    public final Map<Client, Double> getSenderToTotalReceivedMoney()
    {
        return this.senderToTotalReceivedMoney;
    }

    public final void setReceiverToTotalSentMoney(final Map<Client, Double> receiverToTotalSentMoney)
    {
        this.receiverToTotalSentMoney = receiverToTotalSentMoney;
    }

    public final Map<Client, Double> getReceiverToTotalSentMoney()
    {
        return this.receiverToTotalSentMoney;
    }

    public final void setSenderToAverageReceivedMoney(final Map<Client, Double> senderToAverageReceivedMoney)
    {
        this.senderToAverageReceivedMoney = senderToAverageReceivedMoney;
    }

    public final Map<Client, Double> getSenderToAverageReceivedMoney()
    {
        return this.senderToAverageReceivedMoney;
    }

    public final void setReceiverToAverageSentMoney(final Map<Client, Double> receiverToAverageSentMoney)
    {
        this.receiverToAverageSentMoney = receiverToAverageSentMoney;
    }

    public final Map<Client, Double> getReceiverToAverageSentMoney()
    {
        return this.receiverToAverageSentMoney;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(this == otherObject)
        {
            return true;
        }
        if(otherObject == null)
        {
            return false;
        }
        if(this.getClass() != otherObject.getClass())
        {
            return false;
        }
        final ClientStatisticsHolder other = (ClientStatisticsHolder)otherObject;
        return     this.totalReceivedMoney == other.totalReceivedMoney && this.totalSentMoney == other.totalSentMoney
                && this.averageReceivedMoney == other.averageReceivedMoney && this.averageSentMoney == other.averageSentMoney
                && Objects.equals(this.paymentCardOfClientToTotalReceivedMoney, other.paymentCardOfClientToTotalReceivedMoney)
                && Objects.equals(this.paymentCardOfClientToTotalSentMoney, other.paymentCardOfClientToTotalSentMoney)
                && Objects.equals(this.paymentCardOfClientToAverageReceivedMoney, other.paymentCardOfClientToAverageReceivedMoney)
                && Objects.equals(this.paymentCardOfClientToAverageSentMoney, other.paymentCardOfClientToAverageSentMoney)
                && Objects.equals(this.senderToTotalReceivedMoney, other.senderToTotalReceivedMoney)
                && Objects.equals(this.receiverToTotalSentMoney, other.receiverToTotalSentMoney)
                && Objects.equals(this.senderToAverageReceivedMoney, other.senderToAverageReceivedMoney)
                && Objects.equals(this.receiverToAverageSentMoney, other.receiverToAverageSentMoney);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.totalReceivedMoney, this.totalSentMoney, this.averageReceivedMoney,
                this.averageSentMoney, this.paymentCardOfClientToTotalReceivedMoney, this.paymentCardOfClientToTotalSentMoney,
                this.paymentCardOfClientToAverageReceivedMoney, this.paymentCardOfClientToAverageSentMoney,
                this.senderToTotalReceivedMoney, this.receiverToTotalSentMoney, this.senderToAverageReceivedMoney,
                this.receiverToAverageSentMoney);
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName() + "["
                + "totalReceivedMoney = " + this.totalReceivedMoney
                + ", totalSentMoney = " + this.totalSentMoney
                + ", averageReceivedMoney = " + this.averageReceivedMoney
                + ", averageSentMoney = " + this.averageSentMoney
                + ", paymentCardOfClientToTotalReceivedMoney = " + this.paymentCardOfClientToTotalReceivedMoney
                + ", paymentCardOfClientToTotalSentMoney = " + this.paymentCardOfClientToTotalSentMoney
                + ", paymentCardOfClientToAverageReceivedMoney = " + this.paymentCardOfClientToAverageReceivedMoney
                + ", paymentCardOfClientToAverageSentMoney = " + this.paymentCardOfClientToAverageSentMoney
                + ", senderToTotalReceivedMoney = " + this.senderToTotalReceivedMoney
                + ", receiverToTotalSentMoney = " + this.receiverToTotalSentMoney
                + ", senderToAverageReceivedMoney = " + this.senderToAverageReceivedMoney
                + ", receiverToAverageSentMoney = " + this.receiverToAverageSentMoney + "]";
    }
}
