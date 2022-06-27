function checkValidOfDescriptionOfDateOfPaymentAndAlertIfNotValid(researchDescriptionOfDate)
{
    if(!isValidDescriptionOfDateOfPayment(researchDescriptionOfDate))
    {
        alert('Not valid date of payment. Pattern: \'dd.MM.yyyy HH:mm:ss\'');
        return false;
    }
    return true;
}

function isValidDescriptionOfDateOfPayment(researchDescriptionOfDate)
{
    const regularExpressionOfDescriptionOfDateOfPaymentCard = new RegExp(
        "\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}");
    if(!regularExpressionOfDescriptionOfDateOfPaymentCard.test(researchDescriptionOfDate))
    {
        return false;
    }
    const descriptionsOfComponentsOfDate = findDescriptionsOfComponentsOfDate(researchDescriptionOfDate);
    for(let i = 0; i < functionsForValidationComponentsOfDate.length; i++)
    {
        const descriptionOfCurrentResearchComponent = descriptionsOfComponentsOfDate[i];
        const currentResearchValueOfComponent = parseInt(descriptionOfCurrentResearchComponent);
        if(!functionsForValidationComponentsOfDate[i](currentResearchValueOfComponent))
        {
            return false;
        }
    }
    return true;
}

function findDescriptionsOfComponentsOfDate(descriptionOfDate)
{
    const regularExpressionOfSplitterOfComponents = new RegExp("[. :]");
    return descriptionOfDate.split(regularExpressionOfSplitterOfComponents);
}

const functionsForValidationComponentsOfDate = [     //порядок должен быть соответсвующим 'regularExpressionOfDescriptionOfDateOfPaymentCard'
    isValidValueOfDay, isValidValueOfMonth, isValidValueOfYear, isValidValueOfHour, isValidValueOfMinute,
    isValidValueOfSecond
];

function isValidValueOfDay(researchValueOfDay)
{
    const minimalAllowableValueOfDay = 1;
    const maximalAllowableValueOfDay = 31;
    return minimalAllowableValueOfDay <= researchValueOfDay && researchValueOfDay <= maximalAllowableValueOfDay;
}

function isValidValueOfMonth(researchValueOfMonth)
{
    const minimalAllowableValueOfMonth = 1;
    const maximalAllowableValueOfMonth = 12;
    return minimalAllowableValueOfMonth <= researchValueOfMonth && researchValueOfMonth <= maximalAllowableValueOfMonth;
}

function isValidValueOfYear(researchValueOfYear)
{
    const minimalAllowableValueOfYear = 1970;

    const currentDate = new Date();
    const maximalAllowableValueOfYear = currentDate.getFullYear();

    return minimalAllowableValueOfYear <= researchValueOfYear && researchValueOfYear <= maximalAllowableValueOfYear;
}

function isValidValueOfHour(researchValueOfHour)
{
    const minimalAllowableValueOfHour = 0;
    const maximalAllowableValueOfHour = 23;
    return minimalAllowableValueOfHour <= researchValueOfHour && researchValueOfHour <= maximalAllowableValueOfHour;
}

function isValidValueOfMinute(researchValueOfMinute)
{
    const minimalAllowableValueOfMinute = 0;
    const maximalAllowableValueOfMinute = 59;
    return minimalAllowableValueOfMinute <= researchValueOfMinute
        && researchValueOfMinute <= maximalAllowableValueOfMinute;
}

function isValidValueOfSecond(researchValueOfSecond)
{
    const minimalAllowableValueOfSecond = 0;
    const maximalAllowableValueOfSecond = 59;
    return minimalAllowableValueOfSecond <= researchValueOfSecond
        && researchValueOfSecond <= maximalAllowableValueOfSecond;
}