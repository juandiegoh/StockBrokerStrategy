package com.jherenu.stocks.strategies

public abstract class SimulatorStrategy {

    def decideFromPreviousSharesAndActualShare(previousShares, share, ownedShare) {
        if(hasToBuy(previousShares, share)) {
            return new BuySimulatorStatus()
        } else if(hasToSell(previousShares, share, ownedShare)) {
            return new SellSimulatorStatus()
        } else {
            return new DoNothingSimulatorStatus()
        }
    }

    abstract boolean hasToBuy(def previousShares, def share)
    abstract boolean hasToSell(def previousShares, def share, ownedShare)

    def satisfiesPercentageDifference(oldShare, share, conditionClosure) {
        BigDecimal lastPrice = oldShare.price
        BigDecimal percentageDifference = (share.price - lastPrice) / lastPrice
        return conditionClosure(percentageDifference)
    }

    def getSameShareFromYesterday(previousShares, share) {
        return previousShares.find {
            share.companyName == it.companyName && it.date == share.date - 1 }
    }

    String toString() {
        return this.getClass().getSimpleName()
    }
}