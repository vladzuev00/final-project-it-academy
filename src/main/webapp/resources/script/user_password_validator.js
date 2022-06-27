function checkValidDataInFormToChangePasswordAndAlertIfNot(oldPassword, newPassword, repeatedNewPassword)
{
    return checkValidOfPasswordAndAlertMessageIfNot(oldPassword, 'Old password isn\'t valid')
        && checkValidOfPasswordAndAlertMessageIfNot(newPassword, 'New password isn\'t valid')
        && checkValidOfPasswordAndAlertMessageIfNot(repeatedNewPassword, 'Repeated new password isn\'t valid');
}

function checkValidOfPasswordAndAlertMessageIfNot(researchPassword, alertedMessage)
{
    if(isValidPasswordOfUser(researchPassword))
    {
        return true;
    }
    alert(alertedMessage);
    return false;
}

function isValidPasswordOfUser(researchPassword)
{
    const minimalAllowableLengthOfPassword = 5;
    const maximalAllowableLengthOfPassword = 256;
    const lengthOfResearchPassword = researchPassword.length;
    return minimalAllowableLengthOfPassword <= lengthOfResearchPassword
        && lengthOfResearchPassword <= maximalAllowableLengthOfPassword;
}