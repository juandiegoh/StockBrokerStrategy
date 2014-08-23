package com.jherenu.stocks.strategies

import com.jherenu.stocks.domain.OwnedShare
import com.jherenu.stocks.StockBrokerSimulator
import com.jherenu.stocks.domain.MovementOperation
import com.jherenu.stocks.factory.MovementFactory

class BuySimulatorStatus implements StockBrokerUpdater {

    private static final Number MAX_TO_BUY = 1000

    void updateSimulator(StockBrokerSimulator strategySimulator, share) {
        def maxMoneyToSpend = strategySimulator.getMoney() <= MAX_TO_BUY ? strategySimulator.getMoney() : MAX_TO_BUY
        def sharesToBuy = Math.floor(maxMoneyToSpend / share.price).toInteger()
        BigDecimal actualMoneyToSpend = sharesToBuy * share.price

        strategySimulator.subtractMoney(actualMoneyToSpend)
        strategySimulator.addToShares(new OwnedShare(companyName: share.companyName,
                buyDate: share.date,
                count: sharesToBuy
        ))

        MovementFactory movementFactory = new MovementFactory()
        strategySimulator.addToMovements(movementFactory.create(share.date, share.companyName, MovementOperation.BUY, -actualMoneyToSpend))
    }

}
