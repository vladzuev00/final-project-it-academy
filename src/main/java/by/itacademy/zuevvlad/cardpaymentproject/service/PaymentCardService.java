package by.itacademy.zuevvlad.cardpaymentproject.service;

import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentCardDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentCardDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.factory.DTOFactory;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.EntityModifierByDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.paymentcardexpirationdateparser.PaymentCardExpirationDateParser;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.paymentcard.PaymentCardSortingKey;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PaymentCardService extends Service<PaymentCard>
{
    private final PaymentCardExpirationDateParser paymentCardExpirationDateParser;
    private final DTOFactory<PaymentCard, PaymentCardDTO> paymentCardDTOFactory;
    private final EntityModifierByDTO<PaymentCard, PaymentCardDTO> paymentCardModifierByDTO;

    public PaymentCardService(final PaymentCardDAO paymentCardDAO,
                              final PaymentCardExpirationDateParser paymentCardExpirationDateParser,
                              final DTOFactory<PaymentCard, PaymentCardDTO> paymentCardDTOFactory,
                              final EntityModifierByDTO<PaymentCard, PaymentCardDTO> paymentCardModifierByDTO)
    {
        super(paymentCardDAO);
        this.paymentCardExpirationDateParser = paymentCardExpirationDateParser;
        this.paymentCardDTOFactory = paymentCardDTOFactory;
        this.paymentCardModifierByDTO = paymentCardModifierByDTO;
    }

    public final PaymentCard.ExpirationDate parseDescriptionOfExpirationDate(final String parsedDescription)
    {
        return this.paymentCardExpirationDateParser.parse(parsedDescription);
    }

    public final Collection<PaymentCard> sortPaymentCards(final Collection<PaymentCard> sortedPaymentCards,
                                                          final PaymentCardSortingKey paymentCardSortingKey)
    {
        final Comparator<PaymentCard> paymentCardComparator = paymentCardSortingKey.getPaymentCardComparator();
        return sortedPaymentCards.stream().sorted(paymentCardComparator).collect(Collectors.toList());
    }

    public final boolean isNotDeletedPaymentCardWithGivenCardNumberExist(final String cardNumber)
            throws DefiningExistingEntityException
    {
        final PaymentCardDAO paymentCardDAO = (PaymentCardDAO)super.getDao();
        return paymentCardDAO.isNotDeletedPaymentCardWithGivenCardNumberExist(cardNumber);
    }

    public final Optional<PaymentCard> findOptionalOfNotDeletedPaymentCardByCardNumber(final String cardNumber)
            throws FindingEntityException
    {
        final PaymentCardDAO paymentCardDAO = (PaymentCardDAO)super.getDao();
        return paymentCardDAO.findOptionalOfNotDeletedPaymentCardByCardNumber(cardNumber);
    }

    public final Collection<PaymentCard> findPaymentCardsByCardNumber(final String cardNumber)
            throws OffloadingEntitiesException
    {
        final PaymentCardDAO paymentCardDAO = (PaymentCardDAO)super.getDao();
        return paymentCardDAO.findPaymentCardsByCardNumber(cardNumber);
    }

    public final PaymentCard findNotDeletedPaymentCardByCardNumber(final String cardNumber)
            throws FindingEntityException, NoSuchEntityException
    {
        final PaymentCardDAO paymentCardDAO = (PaymentCardDAO)super.getDao();
        final Optional<PaymentCard> optionalOfFoundPaymentCard = paymentCardDAO
                .findOptionalOfNotDeletedPaymentCardByCardNumber(cardNumber);
        if(optionalOfFoundPaymentCard.isEmpty())
        {
            throw new NoSuchEntityException("Payment card with card's number '" + cardNumber + "' doesn't exist");
        }
        return optionalOfFoundPaymentCard.get();
    }

    public final Collection<PaymentCardDTO> transformToPaymentCardsDTO(
            final Collection<PaymentCard> transformedPaymentCards)
    {
        return transformedPaymentCards.stream().map(this.paymentCardDTOFactory::createDTO).collect(Collectors.toSet());
    }

    public final PaymentCardDTO transformToPaymentCardDTO(final PaymentCard paymentCard)
    {
        return this.paymentCardDTOFactory.createDTO(paymentCard);
    }

    public final void modifyPaymentCardByDTO(final PaymentCard modifiedPaymentCard, final PaymentCardDTO paymentCardDTO)
            throws UpdatingEntityByDTOException
    {
        this.paymentCardModifierByDTO.updateEntityByDTO(modifiedPaymentCard, paymentCardDTO);
    }
}
