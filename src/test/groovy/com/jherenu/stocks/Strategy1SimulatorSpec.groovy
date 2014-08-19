package com.jherenu.stocks

import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll

class Strategy1SimulatorSpec extends Specification {

    public static final String SHARE_COMPANY_NAME = 'YPF'
    public static final Date OLD_DATE = new Date(2013, 1, 1)
    StockBrokerSimulator strategySimulator

    void setup() {
        Strategy1 strategy1 = new Strategy1()
        this.strategySimulator = new StockBrokerSimulator(
            strategy: strategy1,
            money: 1000000,
            shares: []
        )
    }

    // Buy
    @Unroll
    void "if buy condition is satisfied with new price = #newPrice _ must consume max amount from \$1000 available to buy shares"() {
        given:
        def initialMoney = this.strategySimulator.getMoney()

        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 101)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,2), price: newPrice)

        when:
        this.strategySimulator.simulate([firstDateShare, buyDateShare])

        then:
        this.strategySimulator.getMoney() == initialMoney - moneyToConsume

        where:
        newPrice    | moneyToConsume
        100         | 1000
        99          | 990
        50          | 1000
        1           | 1000
    }

    @Unroll
    void "if buy condition is satisfied with newPrice = #newPrice _ must buy #sharesToBuy shares adding them to the owned shares"() {
        given:
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 101)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,2), price: newPrice)

        when:
        this.strategySimulator.simulate([firstDateShare, buyDateShare])

        then:
        def justBoughtShares = this.strategySimulator.getShares().findAll { share ->
            share.companyName == SHARE_COMPANY_NAME && share.buyDate == new Date(2014,1,2) && share.unitPrice == newPrice
        }
        justBoughtShares.first().count == sharesToBuy

        where:
        newPrice    | sharesToBuy
        100         | 10
        99          | 10
        50          | 20
        1           | 1000
    }

    void "if buy condition is satisfied and available money is 500 _ must consume max amount from \$500 available to buy shares"() {
        given:
        this.strategySimulator.setMoney(500)

        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 101)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,2), price: 100)

        when:
        this.strategySimulator.simulate([firstDateShare, buyDateShare])

        then:
        this.strategySimulator.getMoney() == 0
    }

    void "if buy condition is satisfied and available money is 500 _ must buy 5 shares adding them to the owned shares"() {
        given:
        this.strategySimulator.setMoney(500)

        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 101)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,2), price: 100)

        when:
        this.strategySimulator.simulate([firstDateShare, buyDateShare])

        then:
        def justBoughtShares = this.strategySimulator.getShares().findAll { share ->
            share.companyName == SHARE_COMPANY_NAME && share.buyDate == new Date(2014,1,2) && share.unitPrice == 100
        }
        justBoughtShares.first().count == 5
    }

    // Sell

    void "if sell condition is satisfied _ must sell every share from that company so money must increase to #endMoney"() {
        given:
        this.strategySimulator.addToShares(createOldShare())

        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 100)
        DateShare sellDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,2), price: sharePrice)

        when:
        this.strategySimulator.simulate([firstDateShare, sellDateShare])

        then:
        this.strategySimulator.getMoney() == endMoney

        where:
        sharePrice | endMoney
        102                 | 1001020
        150                 | 1001500
        1000                | 1010000
    }

    void "if sell condition is satisfied _ must sell every share from that company so they are not owned any more"() {
        given:
        this.strategySimulator.addToShares(createOldShare())

        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 100)
        DateShare sellDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,2), price: 102)

        assert this.strategySimulator.getShares().find { it.companyName == SHARE_COMPANY_NAME} != null

        when:
        this.strategySimulator.simulate([firstDateShare, sellDateShare])

        then:
        this.strategySimulator.getShares().find { it.companyName == SHARE_COMPANY_NAME} == null
    }

    // The rest

    void "if there is no share data from yesterday _ it must do nothing"() {
        given:
        def initialMoney = 500
        this.strategySimulator.setMoney(initialMoney)
        this.strategySimulator.addToShares(createOldShare())

        // 2014-1-1 is not 2014-1-3 yesterday
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,1), price: 101)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: new Date(2014,1,3), price: 101)

        when:
        this.strategySimulator.simulate([firstDateShare, buyDateShare])

        then:
        this.strategySimulator.getMoney() == 500
        and:
        this.strategySimulator.getShares().size() == 1
        this.strategySimulator.getShares().find { it.companyName == SHARE_COMPANY_NAME && it.buyDate == OLD_DATE }
    }

    @IgnoreRest
    void "calculate simulation from example must return endMoney = XXX"() {
        given:
        def dateShares = DateSharesFixture.createBigExample()

        when:
        this.strategySimulator.simulate(dateShares)

        then:
        this.strategySimulator.getMoney() == 3000
    }

    def createOldShare() {
        return new OwnedShare(companyName: SHARE_COMPANY_NAME,
                buyDate: OLD_DATE,
                count: 10,
                unitPrice: 103
        )
    }


}
