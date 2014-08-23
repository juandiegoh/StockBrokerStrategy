package com.jherenu.stocks

import com.jherenu.stocks.common.DateFromStringCreator
import com.jherenu.stocks.domain.DateShare
import com.jherenu.stocks.domain.MovementOperation
import com.jherenu.stocks.domain.OwnedShare
import com.jherenu.stocks.strategies.SimulatorStrategy
import com.jherenu.stocks.strategies.Strategy1
import spock.lang.Specification
import spock.lang.Unroll

class Strategy1SimulatorSpec extends Specification {

    public static final String SHARE_COMPANY_NAME = 'YPF'
    public static final String ANOTHER_SHARE_COMPANY_NAME = "ANOTHER_SHARE"
    public static final Date OLD_DATE = new Date(2013, 1, 1)
    public static final Date END_DATE = new Date(2014, 1, 1)
    StockBrokerSimulator stockBrokerSimulator

    SimulatorStrategy strategy1

    void setup() {
        this.strategy1 = new Strategy1()
        this.stockBrokerSimulator = new StockBrokerSimulator(
            strategy: strategy1,
            money: 1000000
        )
    }

    // Buy
    @Unroll
    void "if buy condition is satisfied with new price = #newPrice and don't have to sell every share _ must consume max amount from \$1000 available to buy shares"() {
        given:
        def initialMoney = this.stockBrokerSimulator.getMoney()

        def firstDate = this.dateFromString("2104/1/1")
        def secondDate = this.dateFromString("2104/1/2")
        def endDate = this.dateFromString("2104/1/3")
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: newPrice)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, buyDateShare], endDate)

        then:
        this.stockBrokerSimulator.getMoney() == initialMoney - moneyToConsume

        where:
        newPrice    | moneyToConsume
        100         | 1000
        99          | 990
        50          | 1000
        1           | 1000
    }

    @Unroll
    void "if buy condition is satisfied with newPrice = #newPrice and don't have to sell every share _ must buy #sharesToBuy shares adding them to the owned shares"() {
        given:
        def firstDate = this.dateFromString("2104/1/1")
        def secondDate = this.dateFromString("2104/1/2")
        def endDate = this.dateFromString("2104/1/3")
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: newPrice)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, buyDateShare], endDate)

        then:
        def justBoughtShares = this.stockBrokerSimulator.getOwnedShareForCompanyName(SHARE_COMPANY_NAME)
        justBoughtShares.companyName == SHARE_COMPANY_NAME
        justBoughtShares.buyDate == secondDate
        justBoughtShares.count == sharesToBuy

        where:
        newPrice    | sharesToBuy
        100         | 10
        99          | 10
        50          | 20
        1           | 1000
    }

    void "if buy condition is satisfied and don't have to sell every share _ must generate a movement"() {
        given:
        def firstDate = this.dateFromString("2104/1/1")
        def secondDate = this.dateFromString("2104/1/2")
        def endDate = this.dateFromString("2104/1/3")
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: 100)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, buyDateShare], endDate)

        then:
        def movements = this.stockBrokerSimulator.getMovements()
        movements.size() == 2
        def doNothingMovement = movements.first()
        doNothingMovement.date == firstDate
        doNothingMovement.operation == MovementOperation.DO_NOTHING
        doNothingMovement.companyName == SHARE_COMPANY_NAME
        doNothingMovement.amount == 0

        def buyMovement = movements.last()
        buyMovement.date == secondDate
        buyMovement.operation == MovementOperation.BUY
        buyMovement.companyName == SHARE_COMPANY_NAME
        buyMovement.amount == -1000
    }

    void "buy condition is satisfied and available money is 500 and share value is 100 and don't have to sell every share _ must consume max amount from \$500 available to buy shares"() {
        given:
        this.stockBrokerSimulator.setMoney(500)

        def firstDate = dateFromString('2014/01/01')
        def secondDate = dateFromString('2014/01/02')
        def endDate = dateFromString('2014/01/03')
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: 100)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, buyDateShare], endDate)

        then:
        this.stockBrokerSimulator.getMoney() == 0
    }

    void "buy condition is satisfied and available money is 500 and share value is 100 and don't have to sell every share _ must buy 5 shares adding them to the owned shares"() {
        given:
        this.stockBrokerSimulator.setMoney(500)

        def firstDate = dateFromString('2014/01/01')
        def secondDate = dateFromString('2014/01/02')
        def endDate = dateFromString('2014/01/03')
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: 100)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, buyDateShare], endDate)

        then:
        def justBoughtShares = this.stockBrokerSimulator.getOwnedShareForCompanyName(SHARE_COMPANY_NAME)
        justBoughtShares.count == 5
        justBoughtShares.buyDate == secondDate
        justBoughtShares.companyName == SHARE_COMPANY_NAME
    }

    // Sell
    @Unroll
    void "sell condition is satisfied and don't have to sell every share _ must sell every share from that company so money must increase to #endMoney"() {
        given:
        this.stockBrokerSimulator.addToShares(createOldShare())

        def firstDate = dateFromString('2014/01/01')
        def secondDate = dateFromString('2014/01/02')
        def endDate = dateFromString('2014/01/03')
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 100)
        DateShare sellDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: sharePrice)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, sellDateShare], endDate)

        then:
        this.stockBrokerSimulator.getMoney() == endMoney

        where:
        sharePrice          | endMoney
        102                 | 1001020
        150                 | 1001500
        1000                | 1010000
    }

    void "if sell condition is satisfied and don't have to sell every share _ must sell every share from that company so they are not owned any more"() {
        given:
        this.stockBrokerSimulator.addToShares(createOldShare())

        def firstDate = dateFromString('2014/01/01')
        def secondDate = dateFromString('2014/01/02')
        def endDate = dateFromString('2014/01/03')
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 100)
        DateShare sellDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: 102)

        assert this.stockBrokerSimulator.getOwnedShareForCompanyName(SHARE_COMPANY_NAME) != null

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, sellDateShare], endDate)

        then:
        this.stockBrokerSimulator.getOwnedShareForCompanyName(SHARE_COMPANY_NAME) == null
    }

    void "sell condition is satisfied and don't have to sell every share _ must generate movements for every operation"() {
        given:
        this.stockBrokerSimulator.addToShares(createOldShare())

        def firstDate = dateFromString('2014/01/01')
        def secondDate = dateFromString('2014/01/02')
        def endDate = dateFromString('2014/01/03')
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 100)
        DateShare sellDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: 102)

        assert this.stockBrokerSimulator.getOwnedShareForCompanyName(SHARE_COMPANY_NAME) != null

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, sellDateShare], endDate)

        then:
        def movements = this.stockBrokerSimulator.getMovements()
        movements.size() == 2
        def doNothingMovement = movements.first()
        doNothingMovement.date == firstDate
        doNothingMovement.operation == MovementOperation.DO_NOTHING
        doNothingMovement.companyName == SHARE_COMPANY_NAME
        doNothingMovement.amount == 0

        def buyMovement = movements.last()
        buyMovement.date == secondDate
        buyMovement.operation == MovementOperation.SELL
        buyMovement.companyName == SHARE_COMPANY_NAME
        buyMovement.amount == 1020
    }

    // The rest
    void "if there is no share data from yesterday _ it must do nothing"() {
        given:
        def initialMoney = 500
        this.stockBrokerSimulator.setMoney(initialMoney)
        this.stockBrokerSimulator.addToShares(createOldShare())

        // 2014-1-1 is not 2014-1-3 yesterday
        def firstDate = dateFromString('2014/01/01')
        def thirdDate = dateFromString('2014/01/03')
        def endDate =  dateFromString('2014/01/04')
        DateShare firstDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 101)
        DateShare buyDateShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: thirdDate, price: 101)

        when:
        this.stockBrokerSimulator.simulate([firstDateShare, buyDateShare], endDate)

        then:
        this.stockBrokerSimulator.getMoney() == 500
        and:
        this.stockBrokerSimulator.getShares().size() == 1
        def justBoughtShares = this.stockBrokerSimulator.getOwnedShareForCompanyName(SHARE_COMPANY_NAME)
        justBoughtShares.buyDate == OLD_DATE
        justBoughtShares.companyName == SHARE_COMPANY_NAME
    }


    void "when the end is reached and shares are still owned must sell every share with date price"() {
        given:
        this.stockBrokerSimulator.setMoney(0)

        def ownedShare = createOldShare()
        def anotherOwnedShare = createOldShare(ANOTHER_SHARE_COMPANY_NAME)
        this.stockBrokerSimulator.addToShares(ownedShare)
        this.stockBrokerSimulator.addToShares(anotherOwnedShare)

        and: "date shares"
        DateShare shareDate = new DateShare(companyName: SHARE_COMPANY_NAME, date: END_DATE, price: 100)
        DateShare anotherShareDate = new DateShare(companyName: ANOTHER_SHARE_COMPANY_NAME, date: END_DATE, price: 50)
        def sellDateShares = [shareDate, anotherShareDate]

        when:
        this.stockBrokerSimulator.simulate(sellDateShares, END_DATE)

        then:
        this.stockBrokerSimulator.getMoney() == 1500
    }

    def createOldShare(companyName = SHARE_COMPANY_NAME) {
        return new OwnedShare(companyName: companyName,
                buyDate: OLD_DATE,
                count: 10
        )
    }

    def dateFromString(dateString) {
        DateFromStringCreator.createFromFormat(dateString)
    }


}
