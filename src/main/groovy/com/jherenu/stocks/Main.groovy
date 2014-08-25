package com.jherenu.stocks

import com.jherenu.stocks.strategies.Strategy1
import com.jherenu.stocks.strategies.Strategy2

class Main {

    static main(args) {
        def fileName = args[0]
        def stockBrokerDecider = new StockBrokerSimulatorDecider()
        def strategy1 = new Strategy1()
        def strategy2 = new Strategy2()
        stockBrokerDecider.evaluateStrategies([strategy1, strategy2], fileName)
    }
}
