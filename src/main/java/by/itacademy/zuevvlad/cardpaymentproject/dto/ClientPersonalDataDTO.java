package by.itacademy.zuevvlad.cardpaymentproject.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public final class ClientPersonalDataDTO implements DTO
{
    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9_.]+@[a-zA-Z]+\\.((ru)|(com)|(by))", message = "invalid email")
    @Size(min = 15, max = 256, message = "length of email should be from 15 to 256")
    private String email;

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

    public ClientPersonalDataDTO()
    {
        super();
        this.email = null;
        this.name = null;
        this.surname = null;
        this.patronymic = null;
        this.phoneNumber = null;
    }

    public ClientPersonalDataDTO(final String email, final String name, final String surname, final String patronymic,
                                 final String phoneNumber)
    {
        super();
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
    }

    public final void setEmail(final String email)
    {
        this.email = email;
    }

    public final String getEmail()
    {
        return this.email;
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
        final ClientPersonalDataDTO other = (ClientPersonalDataDTO)otherObject;
        return     Objects.equals(this.email, other.email) && Objects.equals(this.name, other.name)
                && Objects.equals(this.surname, other.surname) && Objects.equals(this.patronymic, other.patronymic)
                && Objects.equals(this.phoneNumber, other.phoneNumber);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.email, this.name, this.surname, this.patronymic, this.phoneNumber);
    }

    @Override
    public final String toString()
    {
        return    this.getClass().getName() + "[email = " + this.email + ", name = " + this.name + ", surname = "
                + this.surname + ", patronymic = " + this.patronymic + ", phoneNumber = " + this.phoneNumber + "]";
    }
}
