package by.itacademy.zuevvlad.cardpaymentproject.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "bank_accounts")
@Access(value = AccessType.PROPERTY)
public final class BankAccount extends Entity
{
    @NotNull(message = "is required")
    @DecimalMin(value = "0", message = "should be more or equal 0")
    @DecimalMax(value = "9999999999999999999", message = "should be less than 9999999999999999999")
    private BigDecimal money;

    private boolean blocked;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\d{20}", message = "invalid number, should consist of 20 digits")
    private String number;

    private Set<Client> clients;

    public BankAccount()
    {
        super();
        this.money = BankAccount.VALUE_OF_NOT_DEFINED_MONEY;
        this.blocked = BankAccount.VALUE_OF_NOT_DEFINED_BLOCKED;
        this.number = BankAccount.VALUE_OF_NOT_DEFINED_NUMBER;
        this.clients = new HashSet<Client>();
    }

    private static final BigDecimal VALUE_OF_NOT_DEFINED_MONEY = BigDecimal.ZERO;
    private static final boolean VALUE_OF_NOT_DEFINED_BLOCKED = false;
    private static final String VALUE_OF_NOT_DEFINED_NUMBER = "not defined";

    public BankAccount(final long id)
    {
        super(id);
        this.money = BankAccount.VALUE_OF_NOT_DEFINED_MONEY;
        this.blocked = BankAccount.VALUE_OF_NOT_DEFINED_BLOCKED;
        this.number = BankAccount.VALUE_OF_NOT_DEFINED_NUMBER;
        this.clients = new HashSet<Client>();
    }

    public BankAccount(final BigDecimal money, final boolean blocked, final String number)
    {
        super();
        this.money = money;
        this.blocked = blocked;
        this.number = number;
        this.clients = new HashSet<Client>();
    }

    public BankAccount(final long id, final BigDecimal money, final boolean blocked, final String number)
    {
        super(id);
        this.money = money;
        this.blocked = blocked;
        this.number = number;
        this.clients = new HashSet<Client>();
    }

    public final void setMoney(final BigDecimal money)
    {
        this.money = money;
    }

    @Column(name = "money")
    public final BigDecimal getMoney()
    {
        return this.money;
    }

    public final void setBlocked(final boolean blocked)
    {
        this.blocked = blocked;
    }

    @Column(name = "is_blocked")
    public final boolean isBlocked()
    {
        return this.blocked;
    }

    public final void setNumber(final String number)
    {
        this.number = number;
    }

    @Column(name = "number")
    public final String getNumber()
    {
        return this.number;
    }

    public final void setClients(final Set<Client> clients)
    {
        this.clients = clients;
    }

    @OneToMany(mappedBy = "bankAccount", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    public final Set<Client> getClients()
    {
        return this.clients;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final BankAccount other = (BankAccount)otherObject;
        return Objects.equals(this.money, other.money) && this.blocked == other.blocked
                && Objects.equals(this.number, other.number);
    }

    @Override
    public final int hashCode()
    {
        return super.hashCode() + Objects.hash(this.money, this.blocked, this.number);
    }

    @Override
    public final String toString()
    {
        return super.toString() + "[money = " + this.money + ", blocked = " + this.blocked + ", number = " + this.number
                + "]";
    }

    @Override
    public final void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);
        objectOutput.writeObject(this.money);
        objectOutput.writeBoolean(this.blocked);
        objectOutput.writeObject(this.number);
    }

    @Override
    public final void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);
        this.money = (BigDecimal)objectInput.readObject();
        this.blocked = objectInput.readBoolean();
        this.number = (String)objectInput.readObject();
    }
}
