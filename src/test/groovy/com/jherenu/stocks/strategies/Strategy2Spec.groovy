package com.jherenu.stocks.strategies

import com.jherenu.stocks.domain.DateShare
import com.jherenu.stocks.domain.OwnedShare
import com.jherenu.stocks.common.DateFromStringCreator
import spock.lang.Specification
import spock.lang.Unroll

class Strategy2Spec extends Specification {

    public static final String SHARE_COMPANY_NAME = 'YPF'

    Strategy2 strategy2

    void setup() {
        this.strategy2 = new Strategy2()
    }

    @Unroll
    void "share drops at least 1% from yesterdays price _ must buy"() {
        given:
        def firstDate = this.dateFromString("2014/1/1")
        def secondDate = this.dateFromString("2014/1/2")

        def yesterdayShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        def share = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: price)
        def previousShares = [yesterdayShare]

        when:
        boolean result = this.strategy2.hasToBuy(previousShares, share)

        then:
        result == true

        where:
        price << [100, 99, 50, 10, 1]
    }

    @Unroll
    void "share does not drop at least 1% from yesterdays price _ must not buy"() {
        given:
        def firstDate = this.dateFromString("2014/1/1")
        def secondDate = this.dateFromString("2014/1/2")

        def yesterdayShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: firstDate, price: 102)
        def share = new DateShare(companyName: SHARE_COMPANY_NAME, date: secondDate, price: price)
        def previousShares = [yesterdayShare]

        when:
        boolean result = this.strategy2.hasToBuy(previousShares, share)

        then:
        result == false

        where:
        price << [101, 102, 120]
    }

    @Unroll
    void "share average price from before is half or least of today's price _ must buy"() {
        given:
        def before1 = this.dateFromString("2014/1/1")
        def before2 = this.dateFromString("2014/1/2")
        def before3 = this.dateFromString("2014/1/3")
        def before4 = this.dateFromString("2014/1/4")
        def today = this.dateFromString("2014/1/5")

        and: "average is 25 -> 100/4"
        def before1Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before1, price: 10)
        def before2Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before2, price: 20)
        def before3Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before3, price: 30)
        def before4Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before4, price: 40)
        def previousShares = [before1Share, before2Share, before3Share, before4Share]

        def todayShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: today, price: price)

        when:
        boolean result = this.strategy2.hasToBuy(previousShares, todayShare)

        then:
        result == true

        where:
        price << [50, 51, 100]
    }

    @Unroll
    void "share average price = 25 from before is NOT half or least of today's price = #price _ must NOT buy"() {
        given:
        def before1 = this.dateFromString("2014/1/1")
        def before2 = this.dateFromString("2014/1/2")
        def before3 = this.dateFromString("2014/1/3")
        def before4 = this.dateFromString("2014/1/4")
        def today = this.dateFromString("2014/1/5")

        and: "average is 25 -> 100/4"
        def before1Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before1, price: 24)
        def before2Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before2, price: 26)
        def before3Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before3, price: 24)
        def before4Share = new DateShare(companyName: SHARE_COMPANY_NAME, date: before4, price: 26)
        def previousShares = [before1Share, before2Share, before3Share, before4Share]

        def todayShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: today, price: price)

        when:
        boolean result = this.strategy2.hasToBuy(previousShares, todayShare)

        then:
        result == false

        where:
        price << [26, 40, 49, 49.99]
    }

    @Unroll
    void "share was bought 5 or more days ago _ must sell"() {
        given:
        def before5Days = this.dateFromString("2014/1/1")

        def ownedShare = new OwnedShare(companyName: SHARE_COMPANY_NAME, buyDate: before5Days, count: 1)
        def todayShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: today, price: 10)

        when:
        boolean result = this.strategy2.hasToSell([], todayShare, ownedShare)

        then:
        result == true

        where:
        today << [DateFromStringCreator.createFromFormat('2014/1/6'), DateFromStringCreator.createFromFormat('2014/1/7')]
    }

    @Unroll
    void "share was NOT bought 5 days ago _ must NOT sell"() {
        given:
        def before5Days = this.dateFromString("2014/1/1")

        def ownedShare = new OwnedShare(companyName: SHARE_COMPANY_NAME, buyDate: before5Days, count: 1)
        def todayShare = new DateShare(companyName: SHARE_COMPANY_NAME, date: today, price: 10)

        when:
        boolean result = this.strategy2.hasToSell([], todayShare, ownedShare)

        then:
        result == false

        where:
        today << [DateFromStringCreator.createFromFormat('2014/1/2'),
                DateFromStringCreator.createFromFormat('2014/1/3'),
                DateFromStringCreator.createFromFormat('2014/1/4'),
                DateFromStringCreator.createFromFormat('2014/1/5')]
    }

    def dateFromString(dateString) {
        DateFromStringCreator.createFromFormat(dateString)
    }
}
