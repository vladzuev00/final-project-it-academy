package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception.ConstraintException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.BankAccountService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
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

@RestController(value = "clientCRUDRestController")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(path = {"/rest/clients"})
public final class ClientCRUDRestController
{
    private final ClientService clientService;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    @Autowired
    public ClientCRUDRestController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
            final ClientService clientService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_USER_SERVICE)
            final UserService userService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_SERVICE)
            final BankAccountService bankAccountService)
    {
        super();
        this.clientService = clientService;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public final ResponseEntity<Collection<ClientDTO>> findAllClients()
            throws OffloadingEntitiesException
    {
        final Collection<Client> foundClients = this.clientService.findAllEntities();
        final Collection<ClientDTO> foundClientsDTO = this.clientService.transformToClientsDTO(foundClients);
        return ResponseEntity.ok(foundClientsDTO);
    }

    @GetMapping(path = {"/{id}"})
    public final ResponseEntity<ClientDTO> findClientById(
            @PathVariable(name = "id")
            final long idOfFoundClient)
            throws NoSuchEntityException, FindingEntityException
    {
        final Client foundClient = this.clientService.findEntityById(idOfFoundClient);
        final ClientDTO foundClientDTO = this.clientService.transformToClientDTO(foundClient);
        return ResponseEntity.ok(foundClientDTO);
    }

    @PostMapping
    public final ResponseEntity<ClientDTO> addNewClient(
            @Valid
            @RequestBody
            final ClientDTO clientDTO,
            final Errors errors,
            @RequestParam(name = ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_ADDED_CLIENT)
            final long idOfBankAccountOfAddedClient)
            throws ConstraintException, DefiningExistingEntityException, NoSuchEntityException, FindingEntityException,
                   UpdatingEntityByDTOException, AddingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!clientDTO.isDeleted() && this.userService.isNotDeletedUserWithGivenEmailExist(clientDTO.getEmail()))
        {
            throw new ConstraintException("Emails of not deleted users should be unique. Not deleted user with "
                    + "email '" + clientDTO.getEmail() + "' already exists.");
        }
        if(!clientDTO.isDeleted() && this.clientService.isNotDeletedClientWithGivenPhoneNumberExist(
                clientDTO.getPhoneNumber()))
        {
            throw new ConstraintException("Phone numbers of not deleted clients should be unique. Not deleted client "
                    + "with phone number '" + clientDTO.getPhoneNumber() + "' already exists.");
        }

        final BankAccount bankAccount = this.bankAccountService.findEntityById(idOfBankAccountOfAddedClient);
        if(bankAccount.isDeleted() && !clientDTO.isDeleted())
        {
            throw new ConstraintException("Not deleted client should not be associated with deleted bank account.");
        }

        final Client addedClient = new Client();
        this.clientService.modifyClientByDTO(addedClient, clientDTO);
        addedClient.setBankAccount(bankAccount);

        this.clientService.addEntity(addedClient);

        final ClientDTO addedClientDTO = this.clientService.transformToClientDTO(addedClient);
        return ResponseEntity.ok(addedClientDTO);
    }

    public static final String NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_ADDED_CLIENT = "bank_account_id";

    @PutMapping
    public final ResponseEntity<ClientDTO> updateClient(
            @Valid
            @RequestBody
            final ClientDTO clientDTO,
            final Errors errors,
            @RequestParam(name = ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_UPDATED_CLIENT,
                          required = false)
            final Long idOfNewBankAccountOfUpdatedClient)
            throws ConstraintException, FindingEntityException, NoSuchEntityException, UpdatingEntityByDTOException,
                   UpdatingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!clientDTO.isDeleted())
        {
            final Optional<User> optionalOfNotDeletedUserWithGivenEmail = this.userService
                    .findOptionalOfNotDeletedUserByEmail(clientDTO.getEmail());
            if(optionalOfNotDeletedUserWithGivenEmail.isPresent())
            {
                final User notDeletedUserWithGivenEmail = optionalOfNotDeletedUserWithGivenEmail.get();
                if(clientDTO.getId() != notDeletedUserWithGivenEmail.getId())
                {
                    throw new ConstraintException("Another not deleted user with email '" + clientDTO.getEmail()
                            + "' already exists.");
                }
            }

            final Optional<Client> optionalOfNotDeletedClientWithGivenPhoneNumber = this.clientService
                    .findOptionalOfNotDeletedClientByPhoneNumber(clientDTO.getPhoneNumber());
            if(optionalOfNotDeletedClientWithGivenPhoneNumber.isPresent())
            {
                final Client notDeletedClientWithGivenPhoneNumber = optionalOfNotDeletedClientWithGivenPhoneNumber
                        .get();
                if(clientDTO.getId() != notDeletedClientWithGivenPhoneNumber.getId())
                {
                    throw new ConstraintException("Another not deleted client with phone number '"
                            + clientDTO.getPhoneNumber() + "' already exists.");
                }
            }
        }

        final Client updatedClient = this.clientService.findEntityById(clientDTO.getId());
        this.clientService.modifyClientByDTO(updatedClient, clientDTO);

        final BankAccount bankAccountOfUpdatedClient = updatedClient.getBankAccount();
        if(        idOfNewBankAccountOfUpdatedClient != null
                && idOfNewBankAccountOfUpdatedClient != bankAccountOfUpdatedClient.getId())
        {
            final BankAccount newBankAccountOfUpdatedClient = this.bankAccountService.findEntityById(
                    idOfNewBankAccountOfUpdatedClient);
            if(!clientDTO.isDeleted() && newBankAccountOfUpdatedClient.isDeleted())
            {
                throw new ConstraintException("Not deleted client should not be associated with deleted bank account.");
            }
            updatedClient.setBankAccount(newBankAccountOfUpdatedClient);
        }

        this.clientService.updateEntity(updatedClient);

        final ClientDTO updatedClientDTO = this.clientService.transformToClientDTO(updatedClient);
        return ResponseEntity.ok(updatedClientDTO);
    }

    public static final String NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_UPDATED_CLIENT = "bank_account_id";

    @DeleteMapping(path = {"/{id}"})
    public final ResponseEntity<ClientDTO> deleteClientById(
            @PathVariable(name = "id")
            final long idOfDeletedClient)
            throws FindingEntityException, NoSuchEntityException, DeletingEntityException
    {
        final Client foundClient = this.clientService.findEntityById(idOfDeletedClient);
        final ClientDTO foundClientDTO = this.clientService.transformToClientDTO(foundClient);
        this.clientService.deleteEntity(idOfDeletedClient);
        return ResponseEntity.ok(foundClientDTO);
    }
}
