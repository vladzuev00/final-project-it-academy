package by.itacademy.zuevvlad.cardpaymentproject.service;

import by.itacademy.zuevvlad.cardpaymentproject.dao.BankAccountDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.dto.BankAccountDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.factory.BankAccountDTOFactory;
import by.itacademy.zuevvlad.cardpaymentproject.dto.factory.DTOFactory;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.BankAccountModifierByDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.EntityModifierByDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.bankaccount.BankAccountSortingKey;

import java.util.*;
import java.util.stream.Collectors;

public final class BankAccountService extends Service<BankAccount>
{
    private final DTOFactory<BankAccount, BankAccountDTO> bankAccountDTOFactory;
    private final EntityModifierByDTO<BankAccount, BankAccountDTO> bankAccountModifierByDTO;

    public BankAccountService(final BankAccountDAO bankAccountDAO,
                              final DTOFactory<BankAccount, BankAccountDTO> bankAccountDTOFactory,
                              final EntityModifierByDTO<BankAccount, BankAccountDTO> bankAccountModifierByDTO)
    {
        super(bankAccountDAO);
        this.bankAccountDTOFactory = bankAccountDTOFactory;
        this.bankAccountModifierByDTO = bankAccountModifierByDTO;
    }

    public final BankAccount findNotDeletedBankAccountByNumber(final String numberOfFoundBankAccount)
            throws FindingEntityException, NoSuchEntityException
    {
        final BankAccountDAO bankAccountDAO = (BankAccountDAO)super.getDao();
        final Optional<BankAccount> optionalOfFoundBankAccount = bankAccountDAO.findNotDeletedBankAccountByNumber(
                numberOfFoundBankAccount);
        if(optionalOfFoundBankAccount.isEmpty())
        {
            throw new NoSuchEntityException("Bank account with number '" + numberOfFoundBankAccount
                    + "' doesn't exist.");
        }
        return optionalOfFoundBankAccount.get();
    }

    public final Optional<BankAccount> findOptionalOfNotDeletedBankAccountByNumber(final String numberOfFoundBankAccount)
            throws FindingEntityException
    {
        final BankAccountDAO bankAccountDAO = (BankAccountDAO)super.getDao();
        return bankAccountDAO.findNotDeletedBankAccountByNumber(numberOfFoundBankAccount);
    }

    public final boolean isNotDeletedBankAccountWithGivenNumberExist(final String number)
            throws DefiningExistingEntityException
    {
        final BankAccountDAO bankAccountDAO = (BankAccountDAO)super.getDao();
        return bankAccountDAO.isNotDeletedBankAccountWithGivenNumberExist(number);
    }

    public final Collection<BankAccount> findBankAccountsByNumber(final String numberOfFoundBankAccounts)
            throws OffloadingEntitiesException
    {
        final BankAccountDAO bankAccountDAO = (BankAccountDAO)super.getDao();
        return bankAccountDAO.findBankAccountsByNumber(numberOfFoundBankAccounts);
    }

    public final Collection<BankAccount> sortBankAccounts(final Collection<BankAccount> sortedBankAccounts,
                                                          final BankAccountSortingKey bankAccountSortingKey)
    {
        final Comparator<BankAccount> bankAccountComparator = bankAccountSortingKey.getBankAccountComparator();
        return sortedBankAccounts.stream().sorted(bankAccountComparator).collect(Collectors.toList());
    }

    public final BankAccountDTO transformToBankAccountDTO(final BankAccount transformedBankAccount)
    {
        return this.bankAccountDTOFactory.createDTO(transformedBankAccount);
    }

    public final Collection<BankAccountDTO> transformToBankAccountsDTO(
            final Collection<BankAccount> transformedBankAccounts)
    {
        return transformedBankAccounts.stream().map(this.bankAccountDTOFactory::createDTO).collect(Collectors.toSet());
    }

    public final void modifyBankAccountByDTO(final BankAccount modifiedBankAccount, final BankAccountDTO bankAccountDTO)
            throws UpdatingEntityByDTOException
    {
        this.bankAccountModifierByDTO.updateEntityByDTO(modifiedBankAccount, bankAccountDTO);
    }
}
