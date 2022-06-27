function checkValidOfExpirationDateAndAlertIfNotValid(researchDescriptionOfExpirationDate)
{
    if(!isValidDescriptionOfExpirationDateOfPaymentCard(researchDescriptionOfExpirationDate))
    {
        alert('Not valid description of expiration date, template: {month}/{year} without brackets by digits.'
            + ' Month value should be from ' + minimalAllowableValueOfMonth + ' to ' + maximalAllowableValueOfMonth
            + ', year value should be from ' + minimalAllowableValueOfYear + ' to ' + maximalAllowableValueOfYear);
        return false;
    }
    return true;
}

const minimalAllowableValueOfMonth = 1;
const maximalAllowableValueOfMonth = 12;

const minimalAllowableValueOfYear = 1900;
const maximalAllowableValueOfYear = 3000;

function isValidDescriptionOfExpirationDateOfPaymentCard(researchDescriptionOfExpirationDate)
{
    const delimiterOfDescriptionsOfComponentsOfExpirationDate = "/";
    const regularExpressionOfDescriptionOfExpirationDate = new RegExp("\\d{1,2}"
        + delimiterOfDescriptionsOfComponentsOfExpirationDate + "\\d{4}");
    if(regularExpressionOfDescriptionOfExpirationDate.test(researchDescriptionOfExpirationDate))
    {
        const descriptionsOfComponentsOfExpirationDate = researchDescriptionOfExpirationDate.split(
            delimiterOfDescriptionsOfComponentsOfExpirationDate);

        const descriptionOfMonthOfExpirationDate = descriptionsOfComponentsOfExpirationDate[0];
        const radixOfParsing = 10;
        const monthOfExpirationDate = parseInt(descriptionOfMonthOfExpirationDate, radixOfParsing);

        const descriptionOfYearOfExpirationDate = descriptionsOfComponentsOfExpirationDate[1];
        const yearOfExpirationDate = parseInt(descriptionOfYearOfExpirationDate, radixOfParsing);

        return isValidMonthOfExpirationDate(monthOfExpirationDate) && isValidYearOfExpirationDate(yearOfExpirationDate);
    }
    return false;
}

function isValidMonthOfExpirationDate(researchMonth)
{
    return minimalAllowableValueOfMonth <= researchMonth && researchMonth <= maximalAllowableValueOfMonth;
}

function isValidYearOfExpirationDate(researchYear)
{
    return minimalAllowableValueOfYear <= researchYear && researchYear <= maximalAllowableValueOfYear;
}