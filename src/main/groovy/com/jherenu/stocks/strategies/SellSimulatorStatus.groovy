package com.jherenu.stocks.strategies

import com.jherenu.stocks.StockBrokerSimulator
import com.jherenu.stocks.domain.MovementOperation
import com.jherenu.stocks.factory.MovementFactory

class SellSimulatorStatus implements StockBrokerUpdater {

    @Override
    void updateSimulator(StockBrokerSimulator strategySimulator, share) {
        def ownedShareToSell = strategySimulator.getOwnedShareForCompanyName(share.companyName)
        def amount = 0
        if(ownedShareToSell) {
            amount = ownedShareToSell.todayPrice(share.price)
            strategySimulator.addMoney(amount)
            strategySimulator.removeShareForCompanyName(ownedShareToSell.companyName)

        }

        MovementFactory movementFactory = new MovementFactory()
        strategySimulator.addToMovements(movementFactory.create(share.date, share.companyName, MovementOperation.SELL, amount))
    }
}
