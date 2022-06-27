package by.itacademy.zuevvlad.cardpaymentproject.dto.factory;

import by.itacademy.zuevvlad.cardpaymentproject.dto.BankAccountDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;

import java.math.BigDecimal;

public final class BankAccountDTOFactory implements DTOFactory<BankAccount, BankAccountDTO>
{
    public BankAccountDTOFactory()
    {
        super();
    }

    @Override
    public final BankAccountDTO createDTO(final BankAccount bankAccount)
    {
        final long idOfBankAccount = bankAccount.getId();
        final boolean deletedOfBankAccount = bankAccount.isDeleted();
        final BigDecimal moneyOfBankAccount = bankAccount.getMoney();
        final boolean blockedOfBankAccount = bankAccount.isBlocked();
        final String numberOfBankAccount = bankAccount.getNumber();
        return new BankAccountDTO(idOfBankAccount, deletedOfBankAccount, moneyOfBankAccount, blockedOfBankAccount,
                numberOfBankAccount);
    }
}
