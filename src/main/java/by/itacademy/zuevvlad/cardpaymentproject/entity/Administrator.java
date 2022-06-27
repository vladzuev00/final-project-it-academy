package by.itacademy.zuevvlad.cardpaymentproject.entity;

import by.itacademy.zuevvlad.cardpaymentproject.dto.DTO;
import by.itacademy.zuevvlad.cardpaymentproject.service.bindingvalidation.annotation.administrator.AdministratorLevelShouldBeDefined;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@Entity
@Table(name = "administrators")
@Access(value = AccessType.PROPERTY)
public final class Administrator extends User implements DTO
{
    @NotNull(message = "is required")
    @AdministratorLevelShouldBeDefined(message = "administrator's level should be defined")
    private Level level;

    public Administrator()
    {
        super();
        this.level = Level.NOT_DEFINED;
    }

    public Administrator(final long id, final String email, final String password)
    {
        super(id, email, password);
        this.level = Level.NOT_DEFINED;
    }

    public Administrator(final String email, final String password, final Administrator.Level level)
    {
        super(email, password);
        this.level = level;
    }

    public Administrator(final long id, final String email, final String password, final Administrator.Level level)
    {
        super(id, email, password);
        this.level = level;
    }

    public final void setLevel(final Administrator.Level level)
    {
        this.level = level;
    }

    @Column(name = "level")
    @Enumerated(value = EnumType.STRING)
    public final Administrator.Level getLevel()
    {
        return this.level;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final Administrator other = (Administrator)otherObject;
        return this.level == other.level;
    }

    @Override
    public final int hashCode()
    {
        return super.hashCode() + Objects.hashCode(this.level);
    }

    @Override
    public final String toString()
    {
        return super.toString() + "[level = " + this.level + "]";
    }

    @Override
    public final void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);

        objectOutput.writeObject(this.level);
    }

    @Override
    public final void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);

        this.level = (Level)objectInput.readObject();
    }

    public static enum Level
    {
        NOT_DEFINED, SUPPORTER, MODIFIER, MAIN
    }
}

