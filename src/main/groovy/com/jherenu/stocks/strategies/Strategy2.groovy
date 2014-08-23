package com.jherenu.stocks.strategies

class Strategy2 extends SimulatorStrategy {

    public static final BigDecimal ACCEPTED_BUY_PERCENTAGE = -0.01
    public static final int DOUBLE = 2
    public static final int ACCEPTED_AFTER_DAYS = 5

    boolean hasToBuy(previousShares, share) {
        return percentageDifferenceIsAcceptable(previousShares, share) || priceIsAtLeastTheDoubleOfAveragePrices(previousShares, share)
    }

    private boolean percentageDifferenceIsAcceptable(previousShares, share) {
        def oldShare = getSameShareFromYesterday(previousShares, share)
        if (oldShare == null) {
            return false
        }

        this.satisfiesPercentageDifference(oldShare, share) { percentageDifference ->
            percentageDifference <= ACCEPTED_BUY_PERCENTAGE
        }
    }

    boolean priceIsAtLeastTheDoubleOfAveragePrices(previousShares, share) {
        def companyNamePreviousShares = previousShares.findAll { it.companyName == share.companyName }
        if(companyNamePreviousShares) {
            def totalSum = companyNamePreviousShares.sum { it.price }
            return share.price >= DOUBLE * (totalSum / previousShares.findAll { it.companyName == share.companyName }.size())
        } else {
            return false
        }
    }

    boolean hasToSell(previousShares, share, ownedShare) {
        if(ownedShare) {
            return ownedShare.daysOfDifferenceWithDate(share.date) >= ACCEPTED_AFTER_DAYS
        } else {
            return false
        }
    }
}
