package by.itacademy.zuevvlad.cardpaymentproject.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public final class ClientDTO implements DTO
{
    private long id;
    private boolean deleted;

    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9_.]+@[a-zA-Z]+\\.((ru)|(com)|(by))", message = "invalid email")
    @Size(min = 15, max = 256, message = "length of email should be from 15 to 256")
    private String email;

    @NotNull(message = "is required")
    @Size(min = 5, max = 256, message = "length of password should be from 5 to 256")
    private String password;

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

    public ClientDTO()
    {
        super();
        this.id = ClientDTO.VALUE_OF_NOT_DEFINED_ID;
        this.deleted = ClientDTO.VALUE_OF_NOT_DEFINED_DELETED;
        this.email = ClientDTO.VALUE_OF_NOT_DEFINED_EMAIL;
        this.password = ClientDTO.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.name = ClientDTO.VALUE_OF_NOT_DEFINED_NAME;
        this.surname = ClientDTO.VALUE_OF_NOT_DEFINED_SURNAME;
        this.patronymic = ClientDTO.VALUE_OF_NOT_DEFINED_PATRONYMIC;
        this.phoneNumber = ClientDTO.VALUE_OF_NOT_DEFINED_PHONE_NUMBER;
    }

    private static final long VALUE_OF_NOT_DEFINED_ID = -1;
    private static final boolean VALUE_OF_NOT_DEFINED_DELETED = false;
    private static final String VALUE_OF_NOT_DEFINED_EMAIL = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PASSWORD = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_NAME = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_SURNAME = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PATRONYMIC = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PHONE_NUMBER = "not defined";

    public ClientDTO(final long id, final boolean deleted, final String email, final String password, final String name,
                     final String surname, final String patronymic, final String phoneNumber)
    {
        super();
        this.id = id;
        this.deleted = deleted;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
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

    public final void setEmail(final String email)
    {
        this.email = email;
    }

    public final String getEmail()
    {
        return this.email;
    }

    public final void setPassword(final String password)
    {
        this.password = password;
    }

    public final String getPassword()
    {
        return this.password;
    }

    public final void setName(final String name)
    {
        this.name = name;
    }

    public final String getName()
    {
        return this.name;
    }

    public final void setSurname(final String surname)
    {
        this.surname = surname;
    }

    public final String getSurname()
    {
        return this.surname;
    }

    public final void setPatronymic(final String patronymic)
    {
        this.patronymic = patronymic;
    }

    public final String getPatronymic()
    {
        return this.patronymic;
    }

    public final void setPhoneNumber(final String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public final String getPhoneNumber()
    {
        return this.phoneNumber;
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
        final ClientDTO other = (ClientDTO)otherObject;
        return     this.id == other.id && this.deleted == other.deleted && Objects.equals(this.email, other.email)
                && Objects.equals(this.password, other.password) && Objects.equals(this.name, other.name)
                && Objects.equals(this.surname, other.surname) && Objects.equals(this.patronymic, other.patronymic)
                && Objects.equals(this.phoneNumber, other.phoneNumber);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.id, this.deleted, this.email, this.password, this.name, this.surname, this.patronymic,
                this.phoneNumber);
    }

    @Override
    public final String toString()
    {
        return    this.getClass().getName() + "[id = " + this.id + ", deleted = " + this.deleted
                + ", email = " + this.email + ", password = " + this.password + ", name = " + this.name
                + ", surname = " + this.surname + ", patronymic = " + this.patronymic
                + ", phoneNumber = " + this.phoneNumber + "]";
    }
}
