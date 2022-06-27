package by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
//TODO: md5
public final class StringToStringCryptographer implements Cryptographer<String, String>
{
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    public static StringToStringCryptographer createStringToStringCryptographer()
    {
        if(StringToStringCryptographer.stringToStringCryptographer == null)
        {
            synchronized(StringToStringCryptographer.class)
            {
                if(stringToStringCryptographer == null)
                {
                    StringToStringCryptographer.stringToStringCryptographer = new StringToStringCryptographer();
                }
            }
        }
        return StringToStringCryptographer.stringToStringCryptographer;
    }

    private static StringToStringCryptographer stringToStringCryptographer = null;

    private StringToStringCryptographer()
    {
        super();
        this.encoder = Base64.getEncoder();
        this.decoder = Base64.getDecoder();
    }

    @Override
    public final String encrypt(final String encryptedString)
    {
        return this.encoder.encodeToString(encryptedString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public final String decrypt(final String decryptedData)
    {
        return new String(this.decoder.decode(decryptedData));
    }
}
