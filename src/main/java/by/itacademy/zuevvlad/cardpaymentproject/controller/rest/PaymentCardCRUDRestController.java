package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception.ConstraintException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentCardDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentCardService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController(value = "paymentCardCRUDRestController")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(path = {"/rest/payment_cards"})
public final class PaymentCardCRUDRestController
{
    private final PaymentCardService paymentCardService;
    private final ClientService clientService;

    @Autowired
    public PaymentCardCRUDRestController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE)
            final PaymentCardService paymentCardService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
            final ClientService clientService)
    {
        super();
        this.paymentCardService = paymentCardService;
        this.clientService = clientService;
    }

    @GetMapping
    public final ResponseEntity<Collection<PaymentCardDTO>> findAllPaymentCards()
            throws OffloadingEntitiesException
    {
        final Collection<PaymentCard> foundPaymentCards = this.paymentCardService.findAllEntities();
        final Collection<PaymentCardDTO> foundPaymentCardsDTO = this.paymentCardService.transformToPaymentCardsDTO(
                foundPaymentCards);
        return ResponseEntity.ok(foundPaymentCardsDTO);
    }

    @GetMapping(path = {"/{id}"})
    public final ResponseEntity<PaymentCardDTO> findPaymentCardById(
            @PathVariable(name = "id")
            final long idOfFoundPaymentCard)
            throws FindingEntityException, NoSuchEntityException
    {
        final PaymentCard foundPaymentCard = this.paymentCardService.findEntityById(idOfFoundPaymentCard);
        final PaymentCardDTO foundPaymentCardDTO = this.paymentCardService.transformToPaymentCardDTO(foundPaymentCard);
        return ResponseEntity.ok(foundPaymentCardDTO);
    }

    @PostMapping
    public final ResponseEntity<PaymentCardDTO> addNewPaymentCard(
            @Valid
            @RequestBody
            final PaymentCardDTO addedPaymentCardDTO,
            final Errors errors,
            @RequestParam(name = PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_CLIENT_OF_ADDED_PAYMENT_CARD)
            final long idOfClientOfAddedPaymentCard)
            throws ConstraintException, DefiningExistingEntityException, FindingEntityException, NoSuchEntityException,
                   UpdatingEntityByDTOException, AddingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!addedPaymentCardDTO.isDeleted() && this.paymentCardService.isNotDeletedPaymentCardWithGivenCardNumberExist(
                addedPaymentCardDTO.getCardNumber()))
        {
            throw new ConstraintException("Card's numbers of not deleted payment cards should be unique. Not deleted "
                    + "payment card with given card's number '" + addedPaymentCardDTO.getCardNumber()
                    + "' already exists.");
        }
        final Client clientOfAddedPaymentCard = this.clientService.findEntityById(idOfClientOfAddedPaymentCard);
        if(!addedPaymentCardDTO.isDeleted() && clientOfAddedPaymentCard.isDeleted())
        {
            throw new ConstraintException("Not deleted payment card should not be associated with deleted client.");
        }

        final PaymentCard addedPaymentCard = new PaymentCard();
        this.paymentCardService.modifyPaymentCardByDTO(addedPaymentCard, addedPaymentCardDTO);
        addedPaymentCard.setClient(clientOfAddedPaymentCard);

        this.paymentCardService.addEntity(addedPaymentCard);

        final PaymentCardDTO resultAddedPaymentCardDTO = this.paymentCardService.transformToPaymentCardDTO(
                addedPaymentCard);
        return ResponseEntity.ok(resultAddedPaymentCardDTO);
    }

    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_CLIENT_OF_ADDED_PAYMENT_CARD = "client_id";

    @PutMapping
    public final ResponseEntity<PaymentCardDTO> updatePaymentCard(
            @Valid
            @RequestBody
            final PaymentCardDTO updatedPaymentCardDTO,
            final Errors errors,
            @RequestParam(name = PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_CLIENT_ID_OF_UPDATED_PAYMENT_CARD,
                          required = false)
            final Long idOfNewClientOfUpdatedPaymentCard)
            throws ConstraintException, FindingEntityException, NoSuchEntityException, UpdatingEntityByDTOException,
                   UpdatingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        if(!updatedPaymentCardDTO.isDeleted())
        {
            final Optional<PaymentCard> optionalOfNotDeletedPaymentCardWithGivenCardNumber = this.paymentCardService
                    .findOptionalOfNotDeletedPaymentCardByCardNumber(updatedPaymentCardDTO.getCardNumber());
            if(optionalOfNotDeletedPaymentCardWithGivenCardNumber.isPresent())
            {
                final PaymentCard notDeletedPaymentCardWithGivenCardNumber
                        = optionalOfNotDeletedPaymentCardWithGivenCardNumber.get();
                if(updatedPaymentCardDTO.getId() != notDeletedPaymentCardWithGivenCardNumber.getId())
                {
                    throw new ConstraintException("Another not deleted payment card with card's number '"
                            + updatedPaymentCardDTO.getCardNumber() + "' already exists.");
                }
            }
        }

        final PaymentCard updatedPaymentCard = this.paymentCardService.findEntityById(updatedPaymentCardDTO.getId());
        this.paymentCardService.modifyPaymentCardByDTO(updatedPaymentCard, updatedPaymentCardDTO);

        if(idOfNewClientOfUpdatedPaymentCard != null)
        {
            final Client newClientOfUpdatedPaymentCard = this.clientService.findEntityById(
                    idOfNewClientOfUpdatedPaymentCard);
            if(!updatedPaymentCardDTO.isDeleted() && newClientOfUpdatedPaymentCard.isDeleted())
            {
                throw new ConstraintException("Not deleted payment card should not be associated with deleted client.");
            }
            updatedPaymentCard.setClient(newClientOfUpdatedPaymentCard);
        }

        this.paymentCardService.updateEntity(updatedPaymentCard);

        final PaymentCardDTO resultUpdatedPaymentCardDTO = this.paymentCardService.transformToPaymentCardDTO(
                updatedPaymentCard);
        return ResponseEntity.ok(resultUpdatedPaymentCardDTO);
    }

    public static final String NAME_OF_REQUEST_PARAM_OF_CLIENT_ID_OF_UPDATED_PAYMENT_CARD = "client_id";

    @DeleteMapping(path = {"/{id}"})
    public final ResponseEntity<PaymentCardDTO> deletePaymentCardById(
            @PathVariable(name = "id")
            final long idOfDeletedPaymentCard)
            throws FindingEntityException, NoSuchEntityException, DeletingEntityException
    {
        final PaymentCard deletedPaymentCard = this.paymentCardService.findEntityById(idOfDeletedPaymentCard);
        final PaymentCardDTO resultDeletedPaymentCardDTO = this.paymentCardService.transformToPaymentCardDTO(
                deletedPaymentCard);
        this.paymentCardService.deleteEntity(idOfDeletedPaymentCard);
        return ResponseEntity.ok(resultDeletedPaymentCardDTO);
    }
}
