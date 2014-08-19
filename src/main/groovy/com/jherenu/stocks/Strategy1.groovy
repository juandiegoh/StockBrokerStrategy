package com.jherenu.stocks

class Strategy1 implements SimulatorStrategy {


    public static final BigDecimal ACCEPTED_BUY_PERCENTAGE = 0.01
    public static final BigDecimal ACCEPTED_SELL_PERCENTAGE = -0.02

    def decideFromPreviousSharesAndActualShare(previousShares, share) {
        if(hasToBuy(previousShares, share)) {
            return new BuySimulatorStatus()
        } else if(hasToSell(previousShares, share)) {
            return new SellSimulatorStatus()
        } else {
            return new DoNothingSimulatorStatus()
        }
    }

    boolean hasToBuy(previousShares, share) {
        def oldShare = getSameShareFromYesterday(previousShares, share)
        if(oldShare == null) {
            return false
        }

        this.satisfiesPercentageDifference(oldShare, share) { percentageDifference ->
            percentageDifference <= ACCEPTED_BUY_PERCENTAGE
        }
    }

    boolean hasToSell(previousShares, share) {
        def oldShare = getSameShareFromYesterday(previousShares, share)
        if(oldShare == null) {
            return false
        }

        this.satisfiesPercentageDifference(oldShare, share) { percentageDifference ->
            percentageDifference >= ACCEPTED_SELL_PERCENTAGE
        }
    }

    def getSameShareFromYesterday(previousShares, share) {
        return previousShares.find { share.companyName == it.companyName && it.date == share.date - 1 }
    }

    def satisfiesPercentageDifference(oldShare, share, conditionClosure) {
        BigDecimal lastPrice = oldShare.price
        BigDecimal percentageDifference = (share.price - lastPrice) / share.price
        return conditionClosure(percentageDifference)
    }

}
