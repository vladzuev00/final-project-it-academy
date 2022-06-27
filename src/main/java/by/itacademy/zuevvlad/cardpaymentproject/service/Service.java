package by.itacademy.zuevvlad.cardpaymentproject.service;

import by.itacademy.zuevvlad.cardpaymentproject.dao.DAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Entity;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;

import java.util.Collection;
import java.util.Optional;

public abstract class Service<TypeOfEntity extends Entity>
{
    private final DAO<TypeOfEntity> dao;

    protected Service(final DAO<TypeOfEntity> dao)
    {
        super();
        this.dao = dao;
    }

    protected final DAO<TypeOfEntity> getDao()
    {
        return this.dao;
    }

    public final void addEntity(final TypeOfEntity addedEntity)
            throws AddingEntityException
    {
        this.dao.addEntity(addedEntity);
    }

    public final Collection<TypeOfEntity> findAllEntities()
            throws OffloadingEntitiesException
    {
        return this.dao.offloadEntities();
    }

    public final TypeOfEntity findEntityById(final long idOfFoundEntity)
            throws FindingEntityException, NoSuchEntityException
    {
        final Optional<TypeOfEntity> optionalOfFoundEntity = this.dao.findEntityById(idOfFoundEntity);
        if(optionalOfFoundEntity.isEmpty())
        {
            throw new NoSuchEntityException("Entity with id '" + idOfFoundEntity + "' doesn't exist.");
        }
        return optionalOfFoundEntity.get();
    }

    public final void updateEntity(final TypeOfEntity updatedEntity)
            throws UpdatingEntityException
    {
        this.dao.updateEntity(updatedEntity);
    }

    public final void deleteEntity(final long idOfDeletedEntity)
            throws DeletingEntityException
    {
        this.dao.deleteEntity(idOfDeletedEntity);
    }

    public final void deleteEntity(final TypeOfEntity deletedEntity)
            throws DeletingEntityException
    {
        this.dao.deleteEntity(deletedEntity);
    }

    public final Collection<TypeOfEntity> findEntitiesByDeletedStatus(final boolean deleted)
            throws OffloadingEntitiesException
    {
        return deleted ? this.dao.findDeletedEntities() : this.dao.findNotDeletedEntities();
    }

    public final boolean isEntityWithGivenIdExisting(final long id)
            throws DefiningExistingEntityException
    {
        return this.dao.isEntityWithGivenIdExisting(id);
    }
}
