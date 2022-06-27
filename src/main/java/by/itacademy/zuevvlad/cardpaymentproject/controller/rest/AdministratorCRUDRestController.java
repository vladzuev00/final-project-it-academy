package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception.ConstraintException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.AdministratorService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController(value = "administratorCRUDRestController")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(path = {"/rest/administrators"})
public final class AdministratorCRUDRestController
{
    private final UserService userService;
    private final AdministratorService administratorService;

    @Autowired
    public AdministratorCRUDRestController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_USER_SERVICE)
            final UserService userService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_ADMINISTRATOR_SERVICE)
            final AdministratorService administratorService)
    {
        super();
        this.userService = userService;
        this.administratorService = administratorService;
    }

    @GetMapping
    public final ResponseEntity<Collection<Administrator>> findAllAdministrators()
            throws OffloadingEntitiesException
    {
        final Collection<Administrator> foundAdministrators = this.administratorService.findAllEntities();
        return ResponseEntity.ok(foundAdministrators);
    }

    @GetMapping(path = {"/{id}"})
    public final ResponseEntity<Administrator> findAdministratorById(
            @PathVariable(name = "id")
            final long idOfFoundAdministrator)
            throws NoSuchEntityException, FindingEntityException
    {
        final Administrator foundAdministrator = this.administratorService.findEntityById(idOfFoundAdministrator);
        return ResponseEntity.ok(foundAdministrator);
    }

    @PostMapping
    public final ResponseEntity<Administrator> addNewAdministrator(
            @Valid
            @RequestBody
            final Administrator addedAdministrator,
            final Errors errors)
            throws ConstraintException, DefiningExistingEntityException, AddingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!addedAdministrator.isDeleted() && this.userService.isNotDeletedUserWithGivenEmailExist(
                addedAdministrator.getEmail()))
        {
            throw new ConstraintException("Emails of not deleted users should be unique. Not deleted user "
                    + "with email '" + addedAdministrator.getEmail() + "' already exists.");
        }
        this.administratorService.addEntity(addedAdministrator);
        return ResponseEntity.ok(addedAdministrator);
    }

    @PutMapping
    public final ResponseEntity<Administrator> updateAdministrator(
            @Valid
            @RequestBody
            final Administrator updatedAdministrator,
            final Errors errors)
            throws ConstraintException, DefiningExistingEntityException, NoSuchEntityException,
                   FindingEntityException, UpdatingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!this.administratorService.isEntityWithGivenIdExisting(updatedAdministrator.getId()))
        {
            throw new NoSuchEntityException("Administrator with given id '" + updatedAdministrator.getId()
                    + "' doesn't exist.");
        }
        final Optional<User> optionalOfNotDeletedUserWithGivenEmail = this.userService
                .findOptionalOfNotDeletedUserByEmail(updatedAdministrator.getEmail());
        if(!updatedAdministrator.isDeleted() && optionalOfNotDeletedUserWithGivenEmail.isPresent())
        {
            final User notDeletedUserWithGivenEmail = optionalOfNotDeletedUserWithGivenEmail.get();
            if(updatedAdministrator.getId() != notDeletedUserWithGivenEmail.getId())
            {
                throw new ConstraintException("Emails of not deleted users should be unique. Not deleted user "
                        + "with email '" + updatedAdministrator.getEmail() + "' already exists.");
            }
        }
        this.administratorService.updateEntity(updatedAdministrator);
        return ResponseEntity.ok(updatedAdministrator);
    }

    @DeleteMapping(path = {"/{id}"})
    public final ResponseEntity<Administrator> deleteEntityById(
            @PathVariable(name = "id")
            final long idOfDeletedAdministrator)
            throws NoSuchEntityException, FindingEntityException, DeletingEntityException
    {
        final Administrator deletedAdministrator = this.administratorService.findEntityById(idOfDeletedAdministrator);
        this.administratorService.deleteEntity(idOfDeletedAdministrator);
        return ResponseEntity.ok(deletedAdministrator);
    }
}
