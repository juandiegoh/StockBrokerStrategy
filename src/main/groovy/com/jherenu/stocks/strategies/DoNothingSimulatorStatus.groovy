package com.jherenu.stocks.strategies

import com.jherenu.stocks.StockBrokerSimulator
import com.jherenu.stocks.domain.MovementOperation
import com.jherenu.stocks.factory.MovementFactory

class DoNothingSimulatorStatus implements StockBrokerUpdater {

    @Override
    void updateSimulator(StockBrokerSimulator strategySimulator, share) {
        MovementFactory movementFactory = new MovementFactory()
        strategySimulator.addToMovements(movementFactory.create(share.date, share.companyName, MovementOperation.DO_NOTHING, 0))
    }
}
