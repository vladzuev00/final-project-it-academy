package by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.client;

import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;

import java.util.Comparator;

public enum ClientSortingKey
{
    EMAIL((final Client firstClient, final Client secondClient) ->
    {
        final String emailOfFirstClient = firstClient.getEmail();
        final String emailOfSecondClient = secondClient.getEmail();
        return emailOfFirstClient.compareTo(emailOfSecondClient);
    }),
    PASSWORD((final Client firstClient, final Client secondClient) ->
    {
        final String passwordOfFirstClient = firstClient.getPassword();
        final String passwordOfSecondClient = secondClient.getPassword();
        return passwordOfFirstClient.compareTo(passwordOfSecondClient);
    }),
    NAME((final Client firstClient, final Client secondClient) ->
    {
        final String nameOfFirstClient = firstClient.getName();
        final String nameOfSecondClient = secondClient.getName();
        return nameOfFirstClient.compareTo(nameOfSecondClient);
    }),
    SURNAME((final Client firstClient, final Client secondClient) ->
    {
        final String surnameOfFirstClient = firstClient.getSurname();
        final String surnameOfSecondClient = secondClient.getSurname();
        return surnameOfFirstClient.compareTo(surnameOfSecondClient);
    }),
    PATRONYMIC((final Client firstClient, final Client secondClient) ->
    {
        final String patronymicOfFirstClient = firstClient.getPatronymic();
        final String patronymicOfSecondClient = secondClient.getPatronymic();
        return patronymicOfFirstClient.compareTo(patronymicOfSecondClient);
    }),
    PHONE_NUMBER((final Client firstClient, final Client secondClient) ->
    {
        final String phoneNumberOfFirstClient = firstClient.getPhoneNumber();
        final String phoneNumberOfSecondClient = secondClient.getPhoneNumber();
        return phoneNumberOfFirstClient.compareTo(phoneNumberOfSecondClient);
    }),
    NUMBER_OF_BANK_ACCOUNT((final Client firstClient, final Client secondClient) ->
    {
        final BankAccount bankAccountOfFirstClient = firstClient.getBankAccount();
        final String numberOfBankAccountOfFirstClient = bankAccountOfFirstClient.getNumber();

        final BankAccount bankAccountOfSecondClient = secondClient.getBankAccount();
        final String numberOfBankAccountOfSecondClient = bankAccountOfSecondClient.getNumber();

        return numberOfBankAccountOfFirstClient.compareTo(numberOfBankAccountOfSecondClient);
    }),
    DELETED((final Client firstClient, final Client secondClient) ->
    {
        final boolean deletedOfFirstClient = firstClient.isDeleted();
        final boolean deletedOfSecondClient = secondClient.isDeleted();
        return Boolean.compare(deletedOfFirstClient, deletedOfSecondClient);
    });

    private final Comparator<Client> clientComparator;

    private ClientSortingKey(final Comparator<Client> clientComparator)
    {
        this.clientComparator = clientComparator;
    }

    public final Comparator<Client> getClientComparator()
    {
        return this.clientComparator;
    }
}
