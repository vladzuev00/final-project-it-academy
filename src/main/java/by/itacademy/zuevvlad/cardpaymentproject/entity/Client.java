package by.itacademy.zuevvlad.cardpaymentproject.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clients")
@Access(value = AccessType.PROPERTY)
public final class Client extends User
{
    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "should contain only letters")
    @Size(min = 3, max = 256, message = "size should be from 3 to 256")
    private String name;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "should contain only letters")
    @Size(min = 3, max = 256, message = "size should be from 3 to 256")
    private String surname;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "should contain only letters")
    @Size(min = 3, max = 256, message = "size should be from 3 to 256")
    private String patronymic;

    @NotNull(message = "is required")
    @Pattern(regexp = "\\+375-\\d{2}-\\d{3}-\\d{2}-\\d{2}",
             message = "invalid phone number, follow template: +375-**-***-**-** ")
    private String phoneNumber;

    private BankAccount bankAccount;

    private Set<PaymentCard> paymentCards;

    public Client()
    {
        super();
        this.name = Client.VALUE_OF_NOT_DEFINED_NAME;
        this.surname = Client.VALUE_OF_NOT_DEFINED_SURNAME;
        this.patronymic = Client.VALUE_OF_NOT_DEFINED_PATRONYMIC;
        this.phoneNumber = Client.VALUE_OF_NOT_DEFINED_PHONE_NUMBER;
        this.bankAccount = new BankAccount();
        this.paymentCards = new HashSet<PaymentCard>();
    }

    private static final String VALUE_OF_NOT_DEFINED_NAME = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_SURNAME = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PATRONYMIC = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PHONE_NUMBER = "not defined";

    public Client(final long id, final String email, final String password)
    {
        super(id, email, password);
        this.name = Client.VALUE_OF_NOT_DEFINED_NAME;
        this.surname = Client.VALUE_OF_NOT_DEFINED_SURNAME;
        this.patronymic = Client.VALUE_OF_NOT_DEFINED_PATRONYMIC;
        this.phoneNumber = Client.VALUE_OF_NOT_DEFINED_PHONE_NUMBER;
        this.bankAccount = new BankAccount();
        this.paymentCards = new HashSet<PaymentCard>();
    }

    public Client(final String name, final String surname, final String patronymic, final String phoneNumber,
                  final BankAccount bankAccount)
    {
        super();
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.bankAccount = bankAccount;
        this.paymentCards = new HashSet<PaymentCard>();
    }

    public Client(final String email, final String password, final String name, final String surname,
                  final String patronymic, final String phoneNumber, final BankAccount bankAccount)
    {
        super(email, password);
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.bankAccount = bankAccount;
        this.paymentCards = new HashSet<PaymentCard>();
    }

    public Client(final long id, final String email, final String password, final String name, final String surname,
                  final String patronymic, final String phoneNumber, final BankAccount bankAccount)
    {
        super(id, email, password);
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.bankAccount = bankAccount;
        this.paymentCards = new HashSet<PaymentCard>();
    }

    public final void setName(final String name)
    {
        this.name = name;
    }

    @Column(name = "name")
    public final String getName()
    {
        return this.name;
    }

    public final void setSurname(final String surname)
    {
        this.surname = surname;
    }

    @Column(name = "surname")
    public final String getSurname()
    {
        return this.surname;
    }

    public final void setPatronymic(final String patronymic)
    {
        this.patronymic = patronymic;
    }

    @Column(name = "patronymic")
    public final String getPatronymic()
    {
        return this.patronymic;
    }

    public final void setPhoneNumber(final String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "phone_number")
    public final String getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public final void setBankAccount(final BankAccount bankAccount)
    {
        this.bankAccount = bankAccount;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id")
    public final BankAccount getBankAccount()
    {
        return this.bankAccount;
    }

    public final void setPaymentCards(final Set<PaymentCard> paymentCards)
    {
        this.paymentCards = paymentCards;
    }

    @OneToMany(mappedBy = "client", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    public final Set<PaymentCard> getPaymentCards()
    {
        return this.paymentCards;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final Client other = (Client)otherObject;
        return     Objects.equals(this.name, other.name)
                && Objects.equals(this.surname, other.surname)
                && Objects.equals(this.patronymic, other.patronymic)
                && Objects.equals(this.phoneNumber, other.phoneNumber)
                && Objects.equals(this.bankAccount, other.bankAccount);
    }

    @Override
    public final int hashCode()
    {
        return super.hashCode() + Objects.hash(this.name, this.surname, this.patronymic, this.phoneNumber,
                this.bankAccount);
    }

    @Override
    public final String toString()
    {
        return super.toString() + "[name = " + this.name + ", surname = " + this.surname
                + ", patronymic = " + this.patronymic + ", phoneNumber = " + this.phoneNumber
                + ", bankAccount = " + this.bankAccount + "]";
    }

    @Override
    public final void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);

        objectOutput.writeObject(this.name);
        objectOutput.writeObject(this.surname);
        objectOutput.writeObject(this.patronymic);
        objectOutput.writeObject(this.phoneNumber);
        objectOutput.writeObject(this.bankAccount);
    }

    @Override
    public final void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);

        this.name = (String)objectInput.readObject();
        this.surname = (String)objectInput.readObject();
        this.patronymic = (String)objectInput.readObject();
        this.phoneNumber = (String)objectInput.readObject();
        this.bankAccount = (BankAccount)objectInput.readObject();
    }
}

