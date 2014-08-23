package com.jherenu.stocks.fixtures

import com.jherenu.stocks.common.DateShareFileReader

class DateSharesFixture {

    static def createBigExample(fileName) {
        DateShareFileReader.parseFileFromResource(fileName)
    }
}
