package com.jherenu.stocks

class BuySimulatorStatus implements StockBrokerUpdater {

    void updateSimulator(StockBrokerSimulator strategySimulator, share) {
        def maxMoneyToSpend = strategySimulator.getMoney().min(1000)
        def sharesToBuy = Math.round(maxMoneyToSpend / share.price)
        BigDecimal actualMoneyToSpend = sharesToBuy * share.price

        strategySimulator.subtractMoney(actualMoneyToSpend)
        strategySimulator.addToShares(new OwnedShare(companyName: share.companyName,
                buyDate: share.date,
                count: sharesToBuy,
                unitPrice: share.price
        ))
    }
}
