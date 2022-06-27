function checkValidOfCardNumberOfPaymentCardAndAlertIfNotValid(researchCardNumber)
{
    if(!isValidCardNumberOfPaymentCard(researchCardNumber))
    {
        alert('Not valid card number of payment card.');
        return false;
    }
    return true;
}

function isValidCardNumberOfPaymentCard(researchCardNumber)
{
    const regularExpressionOfCardNumber = new RegExp("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
    return regularExpressionOfCardNumber.test(researchCardNumber);
}