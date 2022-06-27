package by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.bankaccount;

import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;

import java.math.BigDecimal;
import java.util.Comparator;

public enum BankAccountSortingKey
{
    MONEY((final BankAccount firstBankAccount, final BankAccount secondBankAccount) ->
    {
        final BigDecimal moneyOfFirstBankAccount = firstBankAccount.getMoney();
        final BigDecimal moneyOfSecondBankAccount = secondBankAccount.getMoney();
        return moneyOfFirstBankAccount.compareTo(moneyOfSecondBankAccount);
    }),
    BLOCKED((final BankAccount firstBankAccount, final BankAccount secondBankAccount) ->
    {
        final boolean blockedOfFirstBankAccount = firstBankAccount.isBlocked();
        final boolean blockedOfSecondBankAccount = secondBankAccount.isBlocked();
        return Boolean.compare(blockedOfFirstBankAccount, blockedOfSecondBankAccount);
    }),
    NUMBER((final BankAccount firstBankAccount, final BankAccount secondBankAccount) ->
    {
        final String numberOfFirstBankAccount = firstBankAccount.getNumber();
        final String numberOfSecondBankAccount = secondBankAccount.getNumber();
        return numberOfFirstBankAccount.compareTo(numberOfSecondBankAccount);
    }),
    DELETED((final BankAccount firstBankAccount, final BankAccount secondBankAccount) ->
    {
        final boolean deletedOfFirstBankAccount = firstBankAccount.isDeleted();
        final boolean deletedOfSecondBankAccount = secondBankAccount.isDeleted();
        return Boolean.compare(deletedOfFirstBankAccount, deletedOfSecondBankAccount);
    });

    private final Comparator<BankAccount> bankAccountComparator;

    private BankAccountSortingKey(final Comparator<BankAccount> bankAccountComparator)
    {
        this.bankAccountComparator = bankAccountComparator;
    }

    public final Comparator<BankAccount> getBankAccountComparator()
    {
        return this.bankAccountComparator;
    }
}
