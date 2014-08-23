package com.jherenu.stocks

import com.jherenu.stocks.common.DateFromStringCreator
import com.jherenu.stocks.fixtures.DateSharesFixture
import com.jherenu.stocks.strategies.SimulatorStrategy
import com.jherenu.stocks.strategies.Strategy1
import com.jherenu.stocks.strategies.Strategy2
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class StrategySimulatorBigExampleSpec extends Specification {

    StockBrokerSimulator stockBrokerSimulator

    @Shared SimulatorStrategy strategy1
    @Shared Strategy2 strategy2

    void setupSpec() {
        this.strategy1 = new Strategy1()
        this.strategy2 = new Strategy2()
    }

    void setup() {
        this.stockBrokerSimulator = new StockBrokerSimulator(
                money: 1000000
        )
    }

    @Unroll
    void "calculate simulation from big example must return endMoney = #endMoney with strategy = #strategy"() {
        given:
        this.stockBrokerSimulator.setStrategy(strategy)
        def dateShares = DateSharesFixture.createBigExample(fileName)

        when:
        this.stockBrokerSimulator.simulate(dateShares, endDate)

        then:
        println this.stockBrokerSimulator
        this.stockBrokerSimulator.getMoney() == endMoney

        and:
        this.stockBrokerSimulator.getOwnedShareForCompanyName("YPF")?.count == ypfCount
        this.stockBrokerSimulator.getOwnedShareForCompanyName("TS")?.count == tsCount
        this.stockBrokerSimulator.getOwnedShareForCompanyName("GGAL")?.count == gGalCount

        where:
        strategy    | endMoney  | ypfCount  | tsCount   | gGalCount | endDate                                               | fileName
//        strategy1   | 997143.85 | 3         | null      | 153       | DateFromStringCreator.createFromFormat('2014/4/4')    | "big_example.csv"
//        strategy1   | 999966.25 | null      | null      | null      | DateFromStringCreator.createFromFormat('2014/4/3')    | "big_example.csv"
//        strategy1   | 998180.35 | null      | null      | 152       | DateFromStringCreator.createFromFormat('2014/4/9')    | "bigger_example.csv"
//        strategy1   | 1000156.35| null      | null      | null      | DateFromStringCreator.createFromFormat('2014/4/8')    | "bigger_example.csv"
//        strategy2   | 997143.85 | 3         | null      | 153       | DateFromStringCreator.createFromFormat('2014/4/4')    | "big_example.csv"
//        strategy2   | 999966.25 | null      | null      | null      | DateFromStringCreator.createFromFormat('2014/4/3')    | "big_example.csv"
        strategy2   | 998003.20 | null      | 2         | 76        | DateFromStringCreator.createFromFormat('2014/4/9')    | "bigger_example.csv"
        strategy2   | 999991.2  | null      | null      | null      | DateFromStringCreator.createFromFormat('2014/4/8')    | "bigger_example.csv"
    }
}
