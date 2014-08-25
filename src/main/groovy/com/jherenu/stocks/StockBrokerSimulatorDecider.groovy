package com.jherenu.stocks

import com.jherenu.stocks.common.DateShareFileReader

class StockBrokerSimulatorDecider {


    public static final int INITIAL_MONEY = 1000000

    def evaluateStrategies(strategies, fileName) {
        def dateShares = DateShareFileReader.parseFromFile(fileName)
        def maxDate = dateShares.max { it.date }
        def stockBrokerSimulators = strategies.collect { strategy ->
            def stockBrokerSimulator = new StockBrokerSimulator(strategy: strategy, money: INITIAL_MONEY)
            stockBrokerSimulator.simulate(dateShares, maxDate.date)
            return stockBrokerSimulator
        }

        stockBrokerSimulators.each {
            println it.toString()
        }

        def winnerStrategy = stockBrokerSimulators.max { it.getMoney() }.getStrategy()
        println "The winner strategy is: ${winnerStrategy}"
    }
}
