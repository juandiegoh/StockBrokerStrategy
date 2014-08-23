package com.jherenu.stocks

import com.jherenu.stocks.domain.Movement
import com.jherenu.stocks.domain.OwnedShare
import com.jherenu.stocks.strategies.SellSimulatorStatus
import com.jherenu.stocks.strategies.SimulatorStrategy

class StockBrokerSimulator {

    public static final FIRST = 0

    SimulatorStrategy strategy
    Number money
    def shares = [:]
    def movements = []

    def simulate(dateShares, Date endDate) {
        def previousShares = []

        dateShares.each { dateShare ->
            def stockBrokerUpdater = strategy.decideFromPreviousSharesAndActualShare(previousShares, dateShare,
                    this.getOwnedShareForCompanyName(dateShare.companyName))
            stockBrokerUpdater.updateSimulator(this, dateShare)

            if(isLastDate(dateShare, endDate)) {
                sellRemainingSharesFromDateShare(dateShare)
            }
            previousShares.add(FIRST, dateShare)
        }
    }

    private boolean isLastDate(dateShare, endDate) {
        return !dateShare.isBefore(endDate)
    }

    def sellRemainingSharesFromDateShare(sellDateShare) {
            new SellSimulatorStatus().updateSimulator(this, sellDateShare)
    }

    def addMoney(amount) {
        this.money = this.money + amount
    }

    def subtractMoney(amount) {
        this.money = this.money - amount
    }

    void addToShares(OwnedShare newOwnedShare) {
        def previousOldShares = getOwnedShareForCompanyName(newOwnedShare.companyName)
        if(previousOldShares) {
            previousOldShares.add(newOwnedShare)
            this.shares.put(previousOldShares.companyName, previousOldShares)
        } else {
            this.shares.put(newOwnedShare.companyName, newOwnedShare)
        }
    }

    def getOwnedShareForCompanyName(companyName) {
        return this.shares.get(companyName)
    }

    def removeShareForCompanyName(companyName) {
        this.shares.remove(companyName)
    }

    void addToMovements(Movement movement) {
        this.movements.add(movement)
    }

    String toString() {
        def movementsString = this.getMovements().inject("") { acc, mov -> acc + "${mov}\n" }
        return "Name: ${strategy.toString()}\nMovements:\n ${movementsString}\nMoney: ${this.money}\n"
    }
}
