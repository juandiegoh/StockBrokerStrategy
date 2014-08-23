package com.jherenu.stocks.domain

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class OwnedShare {

    String companyName
    Date buyDate
    Integer count

    def add(OwnedShare ownedShare) {
        if(this.companyName == ownedShare.companyName) {
            this.count += ownedShare.count
        }
        return this
    }

    def todayPrice(price) {
        return count * price
    }

    def daysOfDifferenceWithDate(date) {
        use(groovy.time.TimeCategory) {
            return (date - this.buyDate).days
        }
    }
}
