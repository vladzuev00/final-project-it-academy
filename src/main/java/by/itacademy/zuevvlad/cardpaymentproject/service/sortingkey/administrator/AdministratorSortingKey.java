package by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;

import java.util.Comparator;

public enum AdministratorSortingKey
{
    EMAIL((final Administrator firstAdministrator, final Administrator secondAdministrator) ->
    {
        final String emailOfFirstAdministrator = firstAdministrator.getEmail();
        final String emailOfSecondAdministrator = secondAdministrator.getEmail();
        return emailOfFirstAdministrator.compareTo(emailOfSecondAdministrator);
    }),
    PASSWORD((final Administrator firstAdministrator, final Administrator secondAdministrator) ->
    {
        final String passwordOfFirstAdministrator = firstAdministrator.getPassword();
        final String passwordOfSecondAdministrator = secondAdministrator.getPassword();
        return passwordOfFirstAdministrator.compareTo(passwordOfSecondAdministrator);
    }),
    DELETED((final Administrator firstAdministrator, final Administrator secondAdministrator) ->
    {
        final boolean deletedOfFirstAdministrator = firstAdministrator.isDeleted();
        final boolean deletedOfSecondAdministrator = secondAdministrator.isDeleted();
        return Boolean.compare(deletedOfFirstAdministrator, deletedOfSecondAdministrator);
    }),
    LEVEL((final Administrator firstAdministrator, final Administrator secondAdministrator) ->
    {
        final Administrator.Level levelOfFirstAdministrator = firstAdministrator.getLevel();
        final Administrator.Level levelOfSecondAdministrator = secondAdministrator.getLevel();
        return levelOfFirstAdministrator.compareTo(levelOfSecondAdministrator);
    });

    private final Comparator<Administrator> administratorComparator;

    private AdministratorSortingKey(final Comparator<Administrator> administratorComparator)
    {
        this.administratorComparator = administratorComparator;
    }

    public final Comparator<Administrator> getAdministratorComparator()
    {
        return this.administratorComparator;
    }
}
