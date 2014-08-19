package com.jherenu.stocks

class SellSimulatorStatus implements StockBrokerUpdater {

    @Override
    void updateSimulator(StockBrokerSimulator strategySimulator, share) {
        def sharesToSell = strategySimulator.getShares().findAll { it.companyName == share.companyName }

        sharesToSell.each { shareToSell ->
            strategySimulator.addMoney(share.price * shareToSell.count)
            strategySimulator.getShares().remove(shareToSell)
        }
    }
}
