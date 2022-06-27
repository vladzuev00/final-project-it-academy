package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier;

import by.itacademy.zuevvlad.cardpaymentproject.dto.DTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Entity;

@FunctionalInterface
public interface EntityModifierByDTO<TypeOfEntity extends Entity, TypeOfDTO extends DTO>
{
    public abstract void updateEntityByDTO(final TypeOfEntity updatedEntity, final TypeOfDTO dto)
            throws UpdatingEntityByDTOException;
}
