package com.jherenu.stocks.domain

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class DateShare {

    String companyName
    Date date
    Number price

    boolean isBefore(endDate) {
        date.before(endDate)

    }

    public String toString() {
        return "date share -> companyName: ${companyName}, date: ${date}, price: ${price}"
    }
}
