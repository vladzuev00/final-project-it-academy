function checkValidOfEmailOfUserAndAlertIfNotValid(researchEmail)
{
    if(!isValidEmailOfUser(researchEmail))
    {
        alert('Not valid email of user');
        return false;
    }
    return true;
}

function isValidEmailOfUser(researchEmail)
{
    const regularExpressionOfEmailOfAdministrator = new RegExp("[a-zA-Z0-9_.]+@[a-zA-Z]+\\.((ru)|(com)|(by))");
    return regularExpressionOfEmailOfAdministrator.test(researchEmail);
}