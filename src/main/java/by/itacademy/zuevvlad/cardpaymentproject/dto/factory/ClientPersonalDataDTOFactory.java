package by.itacademy.zuevvlad.cardpaymentproject.dto.factory;

import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientPersonalDataDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;

public final class ClientPersonalDataDTOFactory implements DTOFactory<Client, ClientPersonalDataDTO>
{
    public ClientPersonalDataDTOFactory()
    {
        super();
    }

    @Override
    public final ClientPersonalDataDTO createDTO(final Client client)
    {
        final String email = client.getEmail();
        final String name = client.getName();
        final String surname = client.getSurname();
        final String patronymic = client.getPatronymic();
        final String phoneNumber = client.getPhoneNumber();
        return new ClientPersonalDataDTO(email, name, surname, patronymic, phoneNumber);
    }
}
