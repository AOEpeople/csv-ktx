package com.aoe.ktx.csv

import com.google.common.truth.Truth.assertThat
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.junit.Test

class CSVRecordKtTest {

    @Test
    fun `get existing value`() {
        val csv = """theName
foo""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record["theName", "bar"]).isEqualTo("foo")
    }

    @Test
    fun `get default because of non existing value`() {
        val csv = """theName
foo""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record["otherName", "bar"]).isEqualTo("bar")
    }

    @Test
    fun `has any value if field exists and value is not empty`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[1]
        assertThat(record.anyNotEmptyValue("theAddress")).isTrue()
    }

    @Test
    fun `has any value if value is not empty in one field`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[1]
        assertThat(record.anyNotEmptyValue("theAddress", "theName", "theBirthday")).isTrue()
    }

    @Test
    fun `has any value if is not empty in one field, not existing ignored`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[1]
        assertThat(record.anyNotEmptyValue("theAddress", "theOtherName", "theBirthday")).isTrue()
    }

    @Test
    fun `has no value if field is empty`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyNotEmptyValue("theAddress")).isFalse()
    }

    @Test
    fun `field is not mapped`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyNotEmptyValue("theOtherAddress")).isFalse()
    }
}
