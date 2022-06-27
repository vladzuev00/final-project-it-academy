package by.itacademy.zuevvlad.cardpaymentproject.entity;

import by.itacademy.zuevvlad.cardpaymentproject.service.converter.StringCryptographerConverter;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.Cryptographer;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.StringToStringCryptographer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Access(value = AccessType.PROPERTY)
public class User extends Entity
{
    @NotNull(message = "is required")
    @Pattern(regexp = "[a-zA-Z0-9_.]+@[a-zA-Z]+\\.((ru)|(com)|(by))", message = "invalid email")
    @Size(min = 15, max = 256, message = "length of email should be from 15 to 256")
    private String email;

    @NotNull(message = "is required")
    @Size(min = 5, max = 256, message = "length of password should be from 5 to 256")
    private String password;

    @Transient
    private final Cryptographer<String, String> stringCryptographer;

    public User()
    {
        super();
        this.email = User.VALUE_OF_NOT_DEFINED_EMAIL;
        this.password = User.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.stringCryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    private static final String VALUE_OF_NOT_DEFINED_EMAIL = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PASSWORD = "not defined";

    public User(final long id)
    {
        super(id);
        this.email = User.VALUE_OF_NOT_DEFINED_EMAIL;
        this.password = User.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.stringCryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    public User(final String email, final String password)
    {
        super();
        this.email = email;
        this.password = password;
        this.stringCryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    public User(final long id, final String email, final String password)
    {
        super(id);
        this.email = email;
        this.password = password;
        this.stringCryptographer = StringToStringCryptographer.createStringToStringCryptographer();
    }

    public final void setEmail(final String email)
    {
        this.email = email;
    }

    @Column(name = "email")
    public final String getEmail()
    {
        return this.email;
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
    public boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final User other = (User)otherObject;
        return Objects.equals(this.email, other.email) && Objects.equals(this.password, other.password);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() + Objects.hash(this.email, this.password);
    }

    @Override
    public String toString()
    {
        return super.toString() + "[email = " + this.email + ", password = " + this.password + "]";
    }

    @Override
    public void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);

        objectOutput.writeObject(this.email);

        final String encryptedPassword = this.stringCryptographer.encrypt(this.password);
        objectOutput.writeObject(encryptedPassword);
    }

    @Override
    public void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);

        this.email = (String)objectInput.readObject();

        final String encryptedPassword = (String)objectInput.readObject();
        this.password = this.stringCryptographer.decrypt(encryptedPassword);
    }
}
