package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier;

import by.itacademy.zuevvlad.cardpaymentproject.dto.BankAccountDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;

import java.math.BigDecimal;

public final class BankAccountModifierByDTO implements EntityModifierByDTO<BankAccount, BankAccountDTO>
{
    public BankAccountModifierByDTO()
    {
        super();
    }

    @Override
    public final void updateEntityByDTO(final BankAccount updatedBankAccount, final BankAccountDTO bankAccountDTO)
    {
        final long newId = bankAccountDTO.getId();
        updatedBankAccount.setId(newId);

        final boolean newDeleted = bankAccountDTO.isDeleted();
        updatedBankAccount.setDeleted(newDeleted);

        final BigDecimal newMoney = bankAccountDTO.getMoney();
        updatedBankAccount.setMoney(newMoney);

        final boolean newBlocked = bankAccountDTO.isBlocked();
        updatedBankAccount.setBlocked(newBlocked);

        final String newNumber = bankAccountDTO.getNumber();
        updatedBankAccount.setNumber(newNumber);
    }
}
