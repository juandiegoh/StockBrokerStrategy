package com.jherenu.stocks.strategies

class Strategy1 extends SimulatorStrategy {

    public static final BigDecimal ACCEPTED_BUY_PERCENTAGE = -0.01
    public static final BigDecimal ACCEPTED_SELL_PERCENTAGE = 0.02

    boolean hasToBuy(previousShares, share) {
        def oldShare = getSameShareFromYesterday(previousShares, share)
        if(oldShare == null) {
            return false
        }

        this.satisfiesPercentageDifference(oldShare, share) { percentageDifference ->
            percentageDifference <= ACCEPTED_BUY_PERCENTAGE
        }
    }

    boolean hasToSell(previousShares, share, ownedShare) {
        def oldShare = getSameShareFromYesterday(previousShares, share)
        if(oldShare == null) {
            return false
        }

        this.satisfiesPercentageDifference(oldShare, share) { percentageDifference ->
            percentageDifference >= ACCEPTED_SELL_PERCENTAGE
        }
    }
}
