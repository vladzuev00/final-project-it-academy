package by.itacademy.zuevvlad.cardpaymentproject.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Objects;

public final class PaymentDTO implements DTO
{
    private long id;
    private boolean deleted;

    @NotNull(message = "is required")
    @DecimalMin(value = "0", message = "should be more or equal 0")
    @DecimalMax(value = "9999999999999999999", message = "should be less than 9999999999999999999")
    private BigDecimal money;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}",
             message = "follow template: dd.MM.yyyy HH:mm:ss")
    private String descriptionOfDate;

    public PaymentDTO()
    {
        super();
        this.id = PaymentDTO.VALUE_OF_NOT_DEFINED_ID;
        this.deleted = PaymentDTO.VALUE_OF_NOT_DEFINED_DELETED;
        this.money = PaymentDTO.VALUE_OF_NOT_DEFINED_MONEY;
        this.descriptionOfDate = PaymentDTO.VALUE_OF_NOT_DEFINED_DESCRIPTION_OF_DATE;
    }

    private static final long VALUE_OF_NOT_DEFINED_ID = -1;
    private static final boolean VALUE_OF_NOT_DEFINED_DELETED = false;
    private static final BigDecimal VALUE_OF_NOT_DEFINED_MONEY = BigDecimal.ZERO;
    private static final String VALUE_OF_NOT_DEFINED_DESCRIPTION_OF_DATE = "not defined";

    public PaymentDTO(final long id, final boolean deleted, final BigDecimal money, final String descriptionOfDate)
    {
        super();
        this.id = id;
        this.deleted = deleted;
        this.money = money;
        this.descriptionOfDate = descriptionOfDate;
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

    public final void setDescriptionOfDate(final String descriptionOfDate)
    {
        this.descriptionOfDate = descriptionOfDate;
    }

    public final String getDescriptionOfDate()
    {
        return this.descriptionOfDate;
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
        final PaymentDTO other = (PaymentDTO)otherObject;
        return     this.id == other.id && this.deleted == other.deleted
                && Objects.equals(this.money, other.money)
                && Objects.equals(this.descriptionOfDate, other.descriptionOfDate);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.id, this.deleted, this.money, this.descriptionOfDate);
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName() + "[id = " + this.id + ", deleted = " + this.deleted
                + ", money = " + this.money + ", descriptionOfDate = " + this.descriptionOfDate + "]";
    }
}

