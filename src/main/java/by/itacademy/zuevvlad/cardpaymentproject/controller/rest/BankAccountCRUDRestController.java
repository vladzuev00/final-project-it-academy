package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception.ConstraintException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.dto.BankAccountDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.service.BankAccountService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
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

@RestController(value = "bankAccountCRUDRestController")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(path = {"/rest/bank_accounts"})
public final class BankAccountCRUDRestController
{
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountCRUDRestController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_SERVICE)
            final BankAccountService bankAccountService)
    {
        super();
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public final ResponseEntity<Collection<BankAccountDTO>> findAllBankAccounts()
            throws OffloadingEntitiesException
    {
        final Collection<BankAccount> foundBankAccounts = this.bankAccountService.findAllEntities();
        final Collection<BankAccountDTO> foundBankAccountDTO = this.bankAccountService.transformToBankAccountsDTO(
                foundBankAccounts);
        return ResponseEntity.ok(foundBankAccountDTO);
    }

    @GetMapping(path = {"/{id}"})
    public final ResponseEntity<BankAccountDTO> findBankAccountById(
            @PathVariable(name = "id")
            final long idOfFoundBankAccount)
            throws NoSuchEntityException, FindingEntityException
    {
        final BankAccount foundBankAccount = this.bankAccountService.findEntityById(idOfFoundBankAccount);
        final BankAccountDTO foundBankAccountDTO = this.bankAccountService.transformToBankAccountDTO(foundBankAccount);
        return ResponseEntity.ok(foundBankAccountDTO);
    }

    @PostMapping
    public final ResponseEntity<BankAccountDTO> addNewBankAccount(
            @Valid
            @RequestBody
            final BankAccountDTO bankAccountDTO,
            final Errors errors)
            throws DefiningExistingEntityException, ConstraintException, UpdatingEntityByDTOException,
                   AddingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!bankAccountDTO.isDeleted() && this.bankAccountService.isNotDeletedBankAccountWithGivenNumberExist(
                bankAccountDTO.getNumber()))
        {
            throw new ConstraintException("Numbers of not deleted bank accounts should be unique. Not deleted "
                    + "bank account with number '" + bankAccountDTO.getNumber() + "' already exists.");
        }
        final BankAccount addedBankAccount = new BankAccount();
        this.bankAccountService.modifyBankAccountByDTO(addedBankAccount, bankAccountDTO);
        this.bankAccountService.addEntity(addedBankAccount);
        final BankAccountDTO addedBankAccountDTO = this.bankAccountService.transformToBankAccountDTO(addedBankAccount);
        return ResponseEntity.ok(addedBankAccountDTO);
    }

    @PutMapping
    public final ResponseEntity<BankAccountDTO> updateBankAccount(
            @Valid
            @RequestBody
            final BankAccountDTO bankAccountDTO,
            final Errors errors)
            throws ConstraintException, NoSuchEntityException, FindingEntityException, UpdatingEntityByDTOException,
                   UpdatingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        final Optional<BankAccount> optionalOfNotDeletedBankAccountWithGivenNumber = this.bankAccountService
                .findOptionalOfNotDeletedBankAccountByNumber(bankAccountDTO.getNumber());
        if(!bankAccountDTO.isDeleted() && optionalOfNotDeletedBankAccountWithGivenNumber.isPresent())
        {
            final BankAccount notDeletedBankAccountWithGivenNumber = optionalOfNotDeletedBankAccountWithGivenNumber
                    .get();
            if(bankAccountDTO.getId() != notDeletedBankAccountWithGivenNumber.getId())
            {
                throw new ConstraintException("Numbers of not deleted bank accounts should be unique. Not deleted "
                        + "bank account with number '" + bankAccountDTO.getNumber() + "' already exists.");
            }
        }
        final BankAccount updatedBankAccount = this.bankAccountService.findEntityById(bankAccountDTO.getId());
        this.bankAccountService.modifyBankAccountByDTO(updatedBankAccount, bankAccountDTO);
        this.bankAccountService.updateEntity(updatedBankAccount);
        return ResponseEntity.ok(bankAccountDTO);
    }

    @DeleteMapping(path = "/{id}")
    public final ResponseEntity<BankAccountDTO> deleteEntityById(
            @PathVariable(name = "id")
            final long idOfDeletedBankAccount)
            throws FindingEntityException, NoSuchEntityException, DeletingEntityException
    {
        final BankAccount foundBankAccount = this.bankAccountService.findEntityById(
                idOfDeletedBankAccount);
        final BankAccountDTO foundBankAccountDTO = this.bankAccountService.transformToBankAccountDTO(foundBankAccount);
        this.bankAccountService.deleteEntity(idOfDeletedBankAccount);
        return ResponseEntity.ok(foundBankAccountDTO);
    }
}
