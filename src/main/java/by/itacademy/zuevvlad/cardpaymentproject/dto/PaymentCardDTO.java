package by.itacademy.zuevvlad.cardpaymentproject.dto;

import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public final class PaymentCardDTO implements DTO
{
    private long id;
    private boolean deleted;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{4}-\\d{4}-\\d{4}-\\d{4}",
            message = "invalid card's number, template: ****-****-****-****, where * is digit")
    private String cardNumber;

    @Valid
    private PaymentCard.ExpirationDate expirationDate;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "invalid payment system")
    @Size(min = 2, max = 256, message = "size of payment system should be from 2 to 256")
    private String paymentSystem;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{3}", message = "invalid cvc, should consist of 3 digits")
    private String cvc;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "invalid name of bank")
    private String nameOfBank;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{4}", message = "invalid password, should consist of 4 digits")
    private String password;

    public PaymentCardDTO()
    {
        super();
        this.id = PaymentCardDTO.VALUE_OF_NOT_DEFINED_ID;
        this.deleted = PaymentCardDTO.VALUE_OF_NOT_DEFINED_DELETED;
        this.cardNumber = PaymentCardDTO.VALUE_OF_NOT_DEFINED_CARD_NUMBER;
        this.expirationDate = new PaymentCard.ExpirationDate();
        this.paymentSystem = PaymentCardDTO.VALUE_OF_NOT_DEFINED_PAYMENT_SYSTEM;
        this.cvc = PaymentCardDTO.VALUE_OF_NOT_DEFINED_CVC;
        this.nameOfBank = PaymentCardDTO.VALUE_OF_NOT_DEFINED_NAME_OF_BANK;
        this.password = PaymentCardDTO.VALUE_OF_NOT_DEFINED_PASSWORD;
    }

    private static final long VALUE_OF_NOT_DEFINED_ID = -1;
    private static final boolean VALUE_OF_NOT_DEFINED_DELETED = false;
    private static final String VALUE_OF_NOT_DEFINED_CARD_NUMBER = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PAYMENT_SYSTEM = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_CVC = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_NAME_OF_BANK = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PASSWORD = "not defined";

    public PaymentCardDTO(final long id, final boolean deleted, final String cardNumber,
                          final PaymentCard.ExpirationDate expirationDate, final String paymentSystem, final String cvc,
                          final String nameOfBank, final String password)
    {
        super();
        this.id = id;
        this.deleted = deleted;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.paymentSystem = paymentSystem;
        this.cvc = cvc;
        this.nameOfBank = nameOfBank;
        this.password = password;
    }

    public final void setId(final long id)
    {
        this.id = id;
    }

    public final long getId()
    {
        return this.id;
    }

    public final void setDeleted(final boolean deleted)
    {
        this.deleted = deleted;
    }

    public final boolean isDeleted()
    {
        return this.deleted;
    }

    public final void setCardNumber(final String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public final String getCardNumber()
    {
        return this.cardNumber;
    }

    public final void setExpirationDate(final PaymentCard.ExpirationDate expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    public final PaymentCard.ExpirationDate getExpirationDate()
    {
        return this.expirationDate;
    }

    public final void setPaymentSystem(final String paymentSystem)
    {
        this.paymentSystem = paymentSystem;
    }

    public final String getPaymentSystem()
    {
        return this.paymentSystem;
    }

    public final void setCvc(final String cvc)
    {
        this.cvc = cvc;
    }

    public final String getCvc()
    {
        return this.cvc;
    }

    public final void setNameOfBank(final String nameOfBank)
    {
        this.nameOfBank = nameOfBank;
    }

    public final String getNameOfBank()
    {
        return this.nameOfBank;
    }

    public final void setPassword(final String password)
    {
        this.password = password;
    }

    public final String getPassword()
    {
        return this.password;
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
        final PaymentCardDTO other = (PaymentCardDTO)otherObject;
        return     this.id == other.id && this.deleted == other.deleted
                && Objects.equals(this.cardNumber, other.cardNumber)
                && Objects.equals(this.expirationDate, other.expirationDate)
                && Objects.equals(this.paymentSystem, other.paymentSystem)
                && Objects.equals(this.cvc, other.cvc)
                && Objects.equals(this.nameOfBank, other.nameOfBank)
                && Objects.equals(this.password, other.password);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.id, this.deleted, this.cardNumber, this.expirationDate, this.paymentSystem, this.cvc,
                this.nameOfBank, this.password);
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName() + "[id = " + this.id + ", deleted = " + this.deleted
                + ", cardNumber = " + this.cardNumber + ", expirationDate = " + this.expirationDate
                + ", paymentSystem = " + this.paymentSystem + ", cvc = " + this.cvc
                + ", nameOfBank = " + this.nameOfBank + ", password = " + this.password + "]";
    }
}

