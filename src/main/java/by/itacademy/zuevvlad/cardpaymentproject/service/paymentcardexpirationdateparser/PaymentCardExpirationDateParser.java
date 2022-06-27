package by.itacademy.zuevvlad.cardpaymentproject.service.paymentcardexpirationdateparser;

import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

public final class PaymentCardExpirationDateParser
{
    public PaymentCardExpirationDateParser()
    {
        super();
    }

    public final PaymentCard.ExpirationDate parse(final String descriptionOfExpirationDate)
    {
        final String[] descriptionsOfComponentsOfExpirationDate = descriptionOfExpirationDate.split(
                PaymentCardExpirationDateParser.DELIMITER_OF_MONTH_AND_YEAR);

        final String descriptionOfNumberOfMonth = descriptionsOfComponentsOfExpirationDate[
                PaymentCardExpirationDateParser.INDEX_OF_MONTH_IN_COMPONENTS_OF_EXPIRATION_DATE];
        final short numberOfMonth = Short.parseShort(descriptionOfNumberOfMonth);

        final String descriptionOfYear = descriptionsOfComponentsOfExpirationDate[
                PaymentCardExpirationDateParser.INDEX_OF_YEAR_IN_COMPONENTS_OF_EXPIRATION_DATE];
        final int year = Integer.parseInt(descriptionOfYear);

        return new PaymentCard.ExpirationDate(numberOfMonth, year);
    }

    private static final String DELIMITER_OF_MONTH_AND_YEAR = "/";
    private static final int INDEX_OF_MONTH_IN_COMPONENTS_OF_EXPIRATION_DATE = 0;
    private static final int INDEX_OF_YEAR_IN_COMPONENTS_OF_EXPIRATION_DATE = 1;
}
