package com.jherenu.stocks

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class OwnedShare {

    String companyName
    Date buyDate
    Integer count
    BigDecimal unitPrice
}
