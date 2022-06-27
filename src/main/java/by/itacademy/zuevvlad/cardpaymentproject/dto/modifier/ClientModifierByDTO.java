package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier;

import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;

public final class ClientModifierByDTO implements EntityModifierByDTO<Client, ClientDTO>
{
    public ClientModifierByDTO()
    {
        super();
    }

    @Override
    public final void updateEntityByDTO(final Client updatedClient, final ClientDTO clientDTO)
    {
        final long newId = clientDTO.getId();
        updatedClient.setId(newId);

        final boolean newDeleted = clientDTO.isDeleted();
        updatedClient.setDeleted(newDeleted);

        final String newEmail = clientDTO.getEmail();
        updatedClient.setEmail(newEmail);

        final String newPassword = clientDTO.getPassword();
        updatedClient.setPassword(newPassword);

        final String newName = clientDTO.getName();
        updatedClient.setName(newName);

        final String newSurname = clientDTO.getSurname();
        updatedClient.setSurname(newSurname);

        final String newPatronymic = clientDTO.getPatronymic();
        updatedClient.setPatronymic(newPatronymic);

        final String newPhoneNumber = clientDTO.getPhoneNumber();
        updatedClient.setPhoneNumber(newPhoneNumber);
    }
}
