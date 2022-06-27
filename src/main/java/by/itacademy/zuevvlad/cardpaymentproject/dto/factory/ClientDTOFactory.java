package by.itacademy.zuevvlad.cardpaymentproject.dto.factory;

import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;

public final class ClientDTOFactory implements DTOFactory<Client, ClientDTO>
{
    public ClientDTOFactory()
    {
        super();
    }

    @Override
    public final ClientDTO createDTO(final Client client)
    {
        final long id = client.getId();
        final boolean deleted = client.isDeleted();
        final String email = client.getEmail();
        final String password = client.getPassword();
        final String name = client.getName();
        final String surname = client.getSurname();
        final String patronymic = client.getPatronymic();
        final String phoneNumber = client.getPhoneNumber();
        return new ClientDTO(id, deleted, email, password, name, surname, patronymic, phoneNumber);
    }
}
