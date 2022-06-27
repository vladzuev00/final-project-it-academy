package by.itacademy.zuevvlad.cardpaymentproject.service.converter;

import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.Cryptographer;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.StringToStringCryptographer;

import javax.persistence.AttributeConverter;

public final class StringCryptographerConverter implements AttributeConverter<String, String>
{
    private final Cryptographer<String, String> stringCryptographer;

    private StringCryptographerConverter()
    {
        super();
        this.stringCryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    @Override
    public final String convertToDatabaseColumn(final String encryptedString)
    {
        return this.stringCryptographer.encrypt(encryptedString);
    }

    @Override
    public final String convertToEntityAttribute(final String decryptedString)
    {
        return this.stringCryptographer.decrypt(decryptedString);
    }
}
