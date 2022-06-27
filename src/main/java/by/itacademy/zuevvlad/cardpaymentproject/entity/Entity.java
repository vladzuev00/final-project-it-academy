package by.itacademy.zuevvlad.cardpaymentproject.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@MappedSuperclass
@Access(value = AccessType.PROPERTY)
public abstract class Entity implements Externalizable
{
    private long id;
    private boolean deleted;

    public Entity()
    {
        super();
        this.id = Entity.VALUE_OF_NOT_DEFINED_ID;
        this.deleted = Entity.VALUE_OF_NOT_DEFINED_DELETED;
    }

    private static final long VALUE_OF_NOT_DEFINED_ID = -1;
    private static final boolean VALUE_OF_NOT_DEFINED_DELETED = false;

    public Entity(final long id)
    {
        super();
        this.id = id;
        this.deleted = Entity.VALUE_OF_NOT_DEFINED_DELETED;
    }

    public final void setId(final long id)
    {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public final long getId()
    {
        return this.id;
    }

    public final void setDeleted(final boolean deleted)
    {
        this.deleted = deleted;
    }

    @Column(name = "is_deleted")
    public final boolean isDeleted()
    {
        return this.deleted;
    }

    @Override
    public boolean equals(final Object otherObject)
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
        final Entity other = (Entity)otherObject;
        return this.id == other.id && this.deleted == other.deleted;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.deleted);
    }

    @Override
    public String toString()
    {
        return this.getClass().getName() + "[id = " + this.id + ", deleted = " + this.deleted + "]";
    }

    @Override
    public void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        objectOutput.writeLong(this.id);
        objectOutput.writeBoolean(this.deleted);
    }

    @Override
    public void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        this.id = objectInput.readLong();
        this.deleted = objectInput.readBoolean();
    }
}
