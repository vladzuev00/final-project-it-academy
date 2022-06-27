package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception.ConstraintException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentCardService;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController(value = "paymentCRUDRestController")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(value = {"/rest/payments"})
public final class PaymentCRUDRestController
{
    private final PaymentService paymentService;
    private final PaymentCardService paymentCardService;

    @Autowired
    public PaymentCRUDRestController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_SERVICE)
            final PaymentService paymentService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE)
            final PaymentCardService paymentCardService)
    {
        super();
        this.paymentService = paymentService;
        this.paymentCardService = paymentCardService;
    }

    @GetMapping
    public final ResponseEntity<Collection<PaymentDTO>> findAllPayments()
            throws OffloadingEntitiesException
    {
        final Collection<Payment> foundPayments = this.paymentService.findAllEntities();
        final Collection<PaymentDTO> foundPaymentsDTO = this.paymentService.transformToPaymentsDTO(foundPayments);
        return ResponseEntity.ok(foundPaymentsDTO);
    }

    @GetMapping(path = {"/{id}"})
    public final ResponseEntity<PaymentDTO> findPaymentById(
            @PathVariable(name = "id")
            final long idOfFoundPayment)
            throws FindingEntityException, NoSuchEntityException
    {
        final Payment foundPayment = this.paymentService.findEntityById(idOfFoundPayment);
        final PaymentDTO foundPaymentDTO = this.paymentService.transformToPaymentDTO(foundPayment);
        return ResponseEntity.ok(foundPaymentDTO);
    }

    @PostMapping
    public final ResponseEntity<PaymentDTO> addNewPayment(
            @Valid
            @RequestBody
            final PaymentDTO addedPaymentDTO,
            final Errors errors,
            @RequestParam(name = PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_ADDED_PAYMENT)
            final long idOfPaymentCardOfSender,
            @RequestParam(name = PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_ADDED_PAYMENT)
            final long idOfPaymentCardOfReceiver)
            throws ConstraintException, FindingEntityException, NoSuchEntityException, UpdatingEntityByDTOException,
                   AddingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }
        final PaymentCard paymentCardOfSender = this.paymentCardService.findEntityById(idOfPaymentCardOfSender);
        final PaymentCard paymentCardOfReceiver = this.paymentCardService.findEntityById(idOfPaymentCardOfReceiver);
        if(!addedPaymentDTO.isDeleted() && (paymentCardOfSender.isDeleted() || paymentCardOfReceiver.isDeleted()))
        {
            throw new ConstraintException("Not deleted payment should not be associated with deleted payment card");
        }

        final Payment addedPayment = new Payment();
        this.paymentService.modifyPaymentByDTO(addedPayment, addedPaymentDTO);
        addedPayment.setCardOfSender(paymentCardOfSender);
        addedPayment.setCardOfReceiver(paymentCardOfReceiver);

        this.paymentService.addEntity(addedPayment);

        final PaymentDTO resultAddedPaymentDTO = this.paymentService.transformToPaymentDTO(addedPayment);
        return ResponseEntity.ok(resultAddedPaymentDTO);
    }

    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_ADDED_PAYMENT
            = "id_of_payment_card_of_sender";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_ADDED_PAYMENT
            = "id_of_payment_card_of_receiver";

    @PutMapping
    public final ResponseEntity<PaymentDTO> updatePayment(
            @Valid
            @RequestBody
            final PaymentDTO updatedPaymentDTO,
            final Errors errors,
            @RequestParam(name = PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_UPDATED_PAYMENT,
                          required = false)
            final Long idOfPaymentCardOfSender,
            @RequestParam(name = PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_UPDATED_PAYMENT,
                          required = false)
            final Long idOfPaymentCardOfReceiver)
            throws ConstraintException, FindingEntityException, NoSuchEntityException, UpdatingEntityByDTOException,
                   UpdatingEntityException
    {
        if(errors.hasErrors())
        {
            throw new ConstraintException(errors);
        }

        final Payment updatedPayment = this.paymentService.findEntityById(updatedPaymentDTO.getId());
        this.paymentService.modifyPaymentByDTO(updatedPayment, updatedPaymentDTO);

        if(idOfPaymentCardOfSender != null)
        {
            final PaymentCard paymentCardOfSender = this.paymentCardService.findEntityById(idOfPaymentCardOfSender);
            if(!updatedPaymentDTO.isDeleted() && paymentCardOfSender.isDeleted())
            {
                throw new ConstraintException("Not deleted payment should not be associated with deleted payment card");
            }
            updatedPayment.setCardOfSender(paymentCardOfSender);
        }

        if(idOfPaymentCardOfReceiver != null)
        {
            final PaymentCard paymentCardOfReceiver = this.paymentCardService.findEntityById(idOfPaymentCardOfReceiver);
            if(!updatedPaymentDTO.isDeleted() && paymentCardOfReceiver.isDeleted())
            {
                throw new ConstraintException("Not deleted payment should not be associated with deleted payment card");
            }
            updatedPayment.setCardOfReceiver(paymentCardOfReceiver);
        }

        this.paymentService.updateEntity(updatedPayment);

        final PaymentDTO resultUpdatedPaymentDTO = this.paymentService.transformToPaymentDTO(updatedPayment);
        return ResponseEntity.ok(resultUpdatedPaymentDTO);
    }

    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_UPDATED_PAYMENT
            = "id_of_payment_card_of_sender";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_UPDATED_PAYMENT
            = "id_of_payment_card_of_receiver";

    @DeleteMapping(path = {"/{id}"})
    public final ResponseEntity<PaymentDTO> deletePaymentById(
            @PathVariable(name = "id")
            final long idOfDeletedPayment)
            throws FindingEntityException, NoSuchEntityException, DeletingEntityException
    {
        final Payment deletedPayment = this.paymentService.findEntityById(idOfDeletedPayment);
        final PaymentDTO resultDeletedPaymentDTO = this.paymentService.transformToPaymentDTO(deletedPayment);
        this.paymentService.deleteEntity(idOfDeletedPayment);
        return ResponseEntity.ok(resultDeletedPaymentDTO);
    }
}
