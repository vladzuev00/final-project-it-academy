package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier;

import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientPersonalDataDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;

public final class ClientModifierByPersonalData implements EntityModifierByDTO<Client, ClientPersonalDataDTO>
{
    public ClientModifierByPersonalData()
    {
        super();
    }

    @Override
    public final void updateEntityByDTO(final Client updatedClient, final ClientPersonalDataDTO clientPersonalDataDTO)
    {
        final String newEmail = clientPersonalDataDTO.getEmail();
        updatedClient.setEmail(newEmail);

        final String newName = clientPersonalDataDTO.getName();
        updatedClient.setName(newName);

        final String newSurname = clientPersonalDataDTO.getSurname();
        updatedClient.setSurname(newSurname);

        final String newPatronymic = clientPersonalDataDTO.getPatronymic();
        updatedClient.setPatronymic(newPatronymic);

        final String newPhoneNumber = clientPersonalDataDTO.getPhoneNumber();
        updatedClient.setPhoneNumber(newPhoneNumber);
    }
}
