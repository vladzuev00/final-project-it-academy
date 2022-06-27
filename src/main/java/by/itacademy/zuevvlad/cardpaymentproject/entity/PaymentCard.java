package by.itacademy.zuevvlad.cardpaymentproject.entity;

import by.itacademy.zuevvlad.cardpaymentproject.service.converter.StringCryptographerConverter;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.Cryptographer;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.StringToStringCryptographer;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "payment_cards")
@Access(value = AccessType.PROPERTY)
public final class PaymentCard extends Entity
{
    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{4}-\\d{4}-\\d{4}-\\d{4}",
             message = "invalid card's number, template: ****-****-****-****, where * is digit")
    private String cardNumber;

    private ExpirationDate expirationDate;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "invalid payment system")
    @Size(min = 2, max = 256, message = "size of payment system should be from 2 to 256")
    private String paymentSystem;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{3}", message = "invalid cvc, should consist of 3 digits")
    private String cvc;

    private Client client;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "invalid name of bank")
    private String nameOfBank;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{4}", message = "invalid password, should consist of 4 digits")
    private String password;

    @Transient
    private final Cryptographer<String, String> cryptographer;

    public PaymentCard()
    {
        super();
        this.cardNumber = PaymentCard.VALUE_OF_NOT_DEFINED_CARD_NUMBER;
        this.expirationDate = new ExpirationDate();
        this.paymentSystem = PaymentCard.VALUE_OF_NOT_DEFINED_PAYMENT_SYSTEM;
        this.cvc = PaymentCard.VALUE_OF_NOT_DEFINED_CVC;
        this.client = new Client();
        this.nameOfBank = PaymentCard.VALUE_OF_NOT_DEFINED_NAME_OF_BANK;
        this.password = PaymentCard.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.cryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    private static final String VALUE_OF_NOT_DEFINED_CARD_NUMBER = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PAYMENT_SYSTEM = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_CVC = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_NAME_OF_BANK = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PASSWORD = "not defined";

    public PaymentCard(final long id)
    {
        super(id);
        this.cardNumber = PaymentCard.VALUE_OF_NOT_DEFINED_CARD_NUMBER;
        this.expirationDate = new ExpirationDate();
        this.paymentSystem = PaymentCard.VALUE_OF_NOT_DEFINED_PAYMENT_SYSTEM;
        this.cvc = PaymentCard.VALUE_OF_NOT_DEFINED_CVC;
        this.client = new Client();
        this.nameOfBank = PaymentCard.VALUE_OF_NOT_DEFINED_NAME_OF_BANK;
        this.password = PaymentCard.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.cryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    public PaymentCard(final String cardNumber, final ExpirationDate expirationDate,
                       final String paymentSystem, final String cvc, final Client client, final String nameOfBank,
                       final String password)
    {
        super();
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.paymentSystem = paymentSystem;
        this.cvc = cvc;
        this.client = client;
        this.nameOfBank = nameOfBank;
        this.password = password;
        this.cryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    public PaymentCard(final long id, final String cardNumber, final ExpirationDate expirationDate,
                       final String paymentSystem, final String cvc, final Client client, final String nameOfBank,
                       final String password)
    {
        super(id);
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.paymentSystem = paymentSystem;
        this.cvc = cvc;
        this.client = client;
        this.nameOfBank = nameOfBank;
        this.password = password;
        this.cryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    public final void setCardNumber(final String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    @Column(name = "card_number")
    public final String getCardNumber()
    {
        return this.cardNumber;
    }

    public final void setExpirationDate(final ExpirationDate expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "month", column = @Column(name = "number_of_month_of_expiration_date")),
            @AttributeOverride(name = "year", column = @Column(name = "year_of_expiration_date"))
    })
    public final ExpirationDate getExpirationDate()
    {
        return this.expirationDate;
    }

    public final void setPaymentSystem(final String paymentSystem)
    {
        this.paymentSystem = paymentSystem;
    }

    @Column(name = "payment_system")
    public final String getPaymentSystem()
    {
        return this.paymentSystem;
    }

    public final void setCvc(final String cvc)
    {
        this.cvc = cvc;
    }

    @Column(name = "encrypted_cvc")
    @Convert(converter = StringCryptographerConverter.class)
    public final String getCvc()
    {
        return this.cvc;
    }

    public final void setClient(final Client client)
    {
        this.client = client;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    public final Client getClient()
    {
        return this.client;
    }

    public final void setNameOfBank(final String nameOfBank)
    {
        this.nameOfBank = nameOfBank;
    }

    @Column(name = "name_of_bank")
    public final String getNameOfBank()
    {
        return this.nameOfBank;
    }

    public final void setPassword(final String password)
    {
        this.password = password;
    }

    @Column(name = "encrypted_password")
    @Convert(converter = StringCryptographerConverter.class)
    public final String getPassword()
    {
        return this.password;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final PaymentCard other = (PaymentCard)otherObject;
        return     Objects.equals(this.cardNumber, other.cardNumber)
                && Objects.equals(this.expirationDate, other.expirationDate)
                && Objects.equals(this.paymentSystem, other.paymentSystem)
                && Objects.equals(this.cvc, other.cvc)
                && Objects.equals(this.client, other.client)
                && Objects.equals(this.nameOfBank, other.nameOfBank)
                && Objects.equals(this.password, other.password);
    }

    @Override
    public final int hashCode()
    {
        return super.hashCode() + Objects.hash(this.cardNumber, this.expirationDate, this.paymentSystem, this.cvc,
                this.client, this.nameOfBank, this.password);
    }

    @Override
    public final String toString()
    {
        return super.toString() + "[cardNumber = " + this.cardNumber + ", expirationDate = " + this.expirationDate
                + ", paymentSystem = " + this.paymentSystem + ", cvc = " + this.cvc + ", client = " + this.client
                + ", nameOfBank = " + this.nameOfBank + ", password = " + this.password + "]";
    }

    @Override
    public final void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);

        objectOutput.writeObject(this.cardNumber);
        objectOutput.writeObject(this.expirationDate);
        objectOutput.writeObject(this.paymentSystem);

        final String encryptedCvc = this.cryptographer.encrypt(this.cvc);
        objectOutput.writeObject(encryptedCvc);

        objectOutput.writeObject(this.client);
        objectOutput.writeObject(this.nameOfBank);

        final String encryptedPassword = this.cryptographer.encrypt(this.password);
        objectOutput.writeObject(encryptedPassword);
    }

    @Override
    public final void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);

        this.cardNumber = (String)objectInput.readObject();
        this.expirationDate = (ExpirationDate)objectInput.readObject();
        this.paymentSystem = (String)objectInput.readObject();

        final String encryptedCvs = (String)objectInput.readObject();
        this.cvc = this.cryptographer.decrypt(encryptedCvs);

        this.client = (Client)objectInput.readObject();
        this.nameOfBank = (String)objectInput.readObject();

        final String encryptedPassword = (String)objectInput.readObject();
        this.password = this.cryptographer.decrypt(encryptedPassword);
    }

    @Embeddable
    public static final class ExpirationDate implements Externalizable
    {
        @Min(value = 1, message = "value of month should be more or equal 1")
        @Max(value = 12, message = "value of month should be less or equal 12")
        private short month;

        @Min(value = 1900, message = "value of year should be more or equal 1900")
        @Max(value = 3000, message = "value of year should be less or equal 3000")
        private int year;

        public ExpirationDate()
        {
            super();

            this.month = ExpirationDate.VALUE_OF_NOT_DEFINED_MONTH;
            this.year = ExpirationDate.VALUE_OF_NOT_DEFINED_YEAR;
        }

        private static final short VALUE_OF_NOT_DEFINED_MONTH = 1;
        private static final int VALUE_OF_NOT_DEFINED_YEAR = -999_999_999;

        public ExpirationDate(final short month, final int year)
        {
            super();

            this.month = month;
            this.year = year;
        }

        public final void setMonth(final short month)
        {
            this.month = month;
        }

        public final short getMonth()
        {
            return this.month;
        }

        public final void setYear(final int year)
        {
            this.year = year;
        }

        public final int getYear()
        {
            return this.year;
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
            final ExpirationDate other = (ExpirationDate)otherObject;
            return this.month == other.month && this.year == other.year;
        }

        @Override
        public final int hashCode()
        {
            return Objects.hash(this.month, this.year);
        }

        @Override
        public final String toString()
        {
            return this.getClass().getName() + "[month = " + this.month + ", year = " + this.year + "]";
        }

        @Override
        public final void writeExternal(final ObjectOutput objectOutput)
                throws IOException
        {
            objectOutput.writeShort(this.month);
            objectOutput.writeInt(this.year);
        }

        @Override
        public final void readExternal(final ObjectInput objectInput)
                throws IOException
        {
            this.month = objectInput.readShort();
            this.year = objectInput.readInt();
        }
    }
}
