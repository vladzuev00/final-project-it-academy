package by.itacademy.zuevvlad.cardpaymentproject.dao;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Entity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class DAO<TypeOfStoredEntity extends Entity>
{
    private final SessionFactory sessionFactory;
    private final Class<TypeOfStoredEntity> typeOfStoredEntity;

    protected DAO(final SessionFactory sessionFactory, final Class<TypeOfStoredEntity> typeOfStoredEntity)
    {
        super();
        this.sessionFactory = sessionFactory;
        this.typeOfStoredEntity = typeOfStoredEntity;
    }

    protected final SessionFactory getSessionFactory()
    {
        return this.sessionFactory;
    }

    protected final Class<TypeOfStoredEntity> getTypeOfStoredEntity()
    {
        return this.typeOfStoredEntity;
    }

    public final void addEntity(final TypeOfStoredEntity addedEntity)
            throws AddingEntityException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            session.save(addedEntity);
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new AddingEntityException(cause);
        }
    }

    public final Collection<TypeOfStoredEntity> offloadEntities()
            throws OffloadingEntitiesException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final String hqlQueryToSelectAllEntities = "FROM " + this.typeOfStoredEntity.getName();
            final Query<TypeOfStoredEntity> queryToSelectAllEntities = session.createQuery(hqlQueryToSelectAllEntities,
                    this.typeOfStoredEntity);
            final List<TypeOfStoredEntity> offloadedEntities = queryToSelectAllEntities.getResultList();
            transaction.commit();
            return offloadedEntities;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    public final Optional<TypeOfStoredEntity> findEntityById(final long idOfFoundEntity)
            throws FindingEntityException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final String parameterNameOfId = "id";
            final String hqlQueryToSelectEntityById = "FROM " + this.typeOfStoredEntity.getName() + " entity "
                    + "WHERE entity.id = :" + parameterNameOfId;
            final Query<TypeOfStoredEntity> queryToSelectEntityById = session.createQuery(hqlQueryToSelectEntityById,
                    this.typeOfStoredEntity);
            queryToSelectEntityById.setParameter(parameterNameOfId, idOfFoundEntity);
            final Optional<TypeOfStoredEntity> optionalOfFoundEntity = queryToSelectEntityById.uniqueResultOptional();
            transaction.commit();
            return optionalOfFoundEntity;
        }
        catch(final PersistenceException cause)
        {
            throw new FindingEntityException(cause);
        }
    }

    public final Collection<TypeOfStoredEntity> findNotDeletedEntities()
            throws OffloadingEntitiesException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final String hqlQueryToSelectNotDeletedEntities = "FROM " + this.typeOfStoredEntity.getName() + " entity "
                    + "WHERE entity.deleted = false";
            final Query<TypeOfStoredEntity> queryToSelectNotDeletedEntities = session.createQuery(
                    hqlQueryToSelectNotDeletedEntities, this.typeOfStoredEntity);
            final List<TypeOfStoredEntity> notDeletedEntities = queryToSelectNotDeletedEntities.getResultList();
            transaction.commit();
            return notDeletedEntities;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    public final Collection<TypeOfStoredEntity> findDeletedEntities()
            throws OffloadingEntitiesException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final String hqlQueryToSelectDeletedEntities = "FROM " + this.typeOfStoredEntity.getName() + " entity "
                    + "WHERE entity.deleted = true";
            final Query<TypeOfStoredEntity> queryToSelectDeletedEntities = session.createQuery(
                    hqlQueryToSelectDeletedEntities, this.typeOfStoredEntity);
            final List<TypeOfStoredEntity> deletedEntities = queryToSelectDeletedEntities.getResultList();
            transaction.commit();
            return deletedEntities;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    public void updateEntity(final TypeOfStoredEntity updatedEntity)
            throws UpdatingEntityException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            session.update(updatedEntity);
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new UpdatingEntityException(cause);
        }
    }

    public void updateDeletedStatusOfEntity(final long idOfUpdatedEntity, final boolean newDeleted)
            throws UpdatingEntityException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final String parameterNameOfDeleted = "deleted";
            final String parameterNameOfId = "id";
            final String hqlQueryToUpdateDeletedStatusOfEntity = "UPDATE " + this.typeOfStoredEntity.getName()
                    + " entity SET entity.deleted = :" + parameterNameOfDeleted
                    + " WHERE entity.id = :" + parameterNameOfId;
            final Query<?> queryToUpdateDeletedStatusOfEntity = session.createQuery(
                    hqlQueryToUpdateDeletedStatusOfEntity);
            queryToUpdateDeletedStatusOfEntity.setParameter(parameterNameOfDeleted, newDeleted);
            queryToUpdateDeletedStatusOfEntity.setParameter(parameterNameOfId, idOfUpdatedEntity);

            queryToUpdateDeletedStatusOfEntity.executeUpdate();
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new UpdatingEntityException(cause);
        }
    }

    public final void updateDeletedStatusOfEntity(final TypeOfStoredEntity updatedEntity, final boolean newDeleted)
            throws UpdatingEntityException
    {
        this.updateDeletedStatusOfEntity(updatedEntity.getId(), newDeleted);
    }

    public final void deleteEntity(final long idOfDeletedEntity)
            throws DeletingEntityException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final String parameterNameOfId = "id";
            final String hqlQueryToDeleteEntityById = "DELETE FROM " + this.typeOfStoredEntity.getName() + " entity "
                    + "WHERE entity.id = :" + parameterNameOfId;
            final Query<?> queryToDeleteEntityById = session.createQuery(hqlQueryToDeleteEntityById);
            queryToDeleteEntityById.setParameter(parameterNameOfId, idOfDeletedEntity);

            queryToDeleteEntityById.executeUpdate();
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new DeletingEntityException(cause);
        }
    }

    public final void deleteEntity(final TypeOfStoredEntity deletedEntity)
            throws DeletingEntityException
    {
        this.deleteEntity(deletedEntity.getId());
    }

    public final boolean isEntityWithGivenIdExisting(final long idOfResearchEntity)
            throws DefiningExistingEntityException
    {
        try(final Session session = this.sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final String parameterNameOfId = "id";
            final String hqlQueryToDefineEntityExistingById = "SELECT 1 FROM " + this.typeOfStoredEntity.getName()
                    + " entity WHERE entity.id = :" + parameterNameOfId;
            final Query<Integer> queryToDefineEntityExisting = session.createQuery(hqlQueryToDefineEntityExistingById,
                    Integer.class);
            queryToDefineEntityExisting.setParameter(parameterNameOfId, idOfResearchEntity);

            final Integer result = queryToDefineEntityExisting.uniqueResult();
            transaction.commit();
            return result != null;
        }
        catch(final PersistenceException cause)
        {
            throw new DefiningExistingEntityException(cause);
        }
    }
}