package by.itacademy.zuevvlad.cardpaymentproject.service.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.user.AdministratorDAO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.administrator.AdministratorSortingKey;

import java.util.*;
import java.util.stream.Collectors;

public final class AdministratorService extends AbstractUserService<Administrator>
{
    public AdministratorService(final AdministratorDAO administratorDAO)
    {
        super(administratorDAO);
    }

    public final Map<Administrator.Level, String> findAdministratorLevelsAndTheirDescriptions()
    {
        final Map<Administrator.Level, String> administratorLevelsAndTheirDescriptions
                = new EnumMap<Administrator.Level, String>(Administrator.Level.class);
        final Administrator.Level[] levels = Administrator.Level.values();
        Arrays.stream(levels).forEach((final Administrator.Level level) ->
        {
            final String descriptionLevel = level.name();
            administratorLevelsAndTheirDescriptions.put(level, descriptionLevel);
        });
        return administratorLevelsAndTheirDescriptions;
    }

    public final Collection<Administrator> sortAdministrators(final Collection<Administrator> sortedAdministrators,
                                                              final AdministratorSortingKey administratorSortingKey)
    {
        final Comparator<Administrator> administratorComparator = administratorSortingKey.getAdministratorComparator();
        return sortedAdministrators.stream().sorted(administratorComparator).collect(Collectors.toList());
    }
}
