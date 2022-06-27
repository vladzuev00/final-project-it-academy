package by.itacademy.zuevvlad.cardpaymentproject.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Objects;

public final class BankAccountDTO implements DTO
{
    private long id;
    private boolean deleted;

    @NotNull(message = "is required")
    @DecimalMin(value = "0", message = "should be more or equal 0")
    @DecimalMax(value = "9999999999999999999", message = "should be less than 9999999999999999999")
    private BigDecimal money;
    private boolean blocked;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{20}", message = "invalid number, should consist of 20 digits")
    private String number;

    public BankAccountDTO()
    {
        super();
        this.id = BankAccountDTO.VALUE_OF_NOT_DEFINED_ID;
        this.deleted = BankAccountDTO.VALUE_OF_NOT_DEFINED_DELETED;
        this.money = BankAccountDTO.VALUE_OF_NOT_DEFINED_MONEY;
        this.blocked = BankAccountDTO.VALUE_OF_NOT_DEFINED_BLOCKED;
        this.number = BankAccountDTO.VALUE_OF_NOT_DEFINED_NUMBER;
    }

    private static final long VALUE_OF_NOT_DEFINED_ID = -1;
    private static final boolean VALUE_OF_NOT_DEFINED_DELETED = false;
    private static final BigDecimal VALUE_OF_NOT_DEFINED_MONEY = BigDecimal.ZERO;
    private static final boolean VALUE_OF_NOT_DEFINED_BLOCKED = false;
    private static final String VALUE_OF_NOT_DEFINED_NUMBER = "not defined";

    public BankAccountDTO(final long id, final boolean deleted, final BigDecimal money, final boolean blocked,
                          final String number)
    {
        super();
        this.id = id;
        this.deleted = deleted;
        this.money = money;
        this.blocked = blocked;
        this.number = number;
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

    public final void setMoney(final BigDecimal money)
    {
        this.money = money;
    }

    public final BigDecimal getMoney()
    {
        return this.money;
    }

    public final void setBlocked(final boolean blocked)
    {
        this.blocked = blocked;
    }

    public final boolean isBlocked()
    {
        return this.blocked;
    }

    public final void setNumber(final String number)
    {
        this.number = number;
    }

    public final String getNumber()
    {
        return this.number;
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
        final BankAccountDTO other = (BankAccountDTO)otherObject;
        return     this.id == other.id && this.deleted == other.deleted && Objects.equals(this.money, other.money)
                && this.blocked == other.blocked && Objects.equals(this.number, other.number);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.id, this.deleted, this.money, this.blocked, this.number);
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName() + "[id = " + this.id + ", deleted = " + this.deleted
                + ", money = " + this.money + ", blocked = " + this.blocked + ", number = " + this.number + "]";
    }
}
