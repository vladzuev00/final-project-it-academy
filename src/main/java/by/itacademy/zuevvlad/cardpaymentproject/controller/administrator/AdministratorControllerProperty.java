package by.itacademy.zuevvlad.cardpaymentproject.controller.administrator;

public enum AdministratorControllerProperty
{
    DELIMITER_OF_OPERATIONS("|");

    private final String value;

    private AdministratorControllerProperty(final String value)
    {
        this.value = value;
    }

    public final String getValue()
    {
        return this.value;
    }
}
