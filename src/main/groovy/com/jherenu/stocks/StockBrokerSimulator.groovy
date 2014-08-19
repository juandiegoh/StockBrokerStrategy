package com.jherenu.stocks

class StockBrokerSimulator {

    public static final FIRST = 0

    SimulatorStrategy strategy
    BigDecimal money
    def shares

    def simulate(dateShares) {
        def previousShares = []

        dateShares.each { dateShare ->
            def stockBrokerUpdater = strategy.decideFromPreviousSharesAndActualShare(previousShares, dateShare)
            stockBrokerUpdater.updateSimulator(this, dateShare)
            previousShares.add(FIRST, dateShare)
        }
    }

    def subtractMoney(amount) {
        this.money = this.money - amount
    }

    void addToShares(OwnedShare ownedShare) {
        this.shares.add(ownedShare)
    }

    def addMoney(amount) {
        this.money = this.money + amount
    }
}
