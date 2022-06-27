package by.itacademy.zuevvlad.cardpaymentproject.dto.factory;

import by.itacademy.zuevvlad.cardpaymentproject.dto.DTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Entity;

@FunctionalInterface
public interface DTOFactory<TypeOfEntity extends Entity, TypeOfDTO extends DTO>
{
    public abstract TypeOfDTO createDTO(final TypeOfEntity entity);
}
