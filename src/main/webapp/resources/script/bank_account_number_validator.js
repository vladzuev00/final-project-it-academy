function checkValidOfNumberOfBankAccountAndAlertIfNotValid(researchNumberOfBankAccount)
{
    if(!isValidNumberOfBankAccount(researchNumberOfBankAccount))
    {
        alert('Not valid number of bank account.');
        return false;
    }
    return true;
}

function isValidNumberOfBankAccount(researchNumberOfBankAccount)
{
    const regularExpressionOfNumberOfBankAccount = new RegExp("\\d{20}");
    return regularExpressionOfNumberOfBankAccount.test(researchNumberOfBankAccount);
}