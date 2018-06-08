/*
 * Copyright (c) 2018. Copyright (C) 2018 AOE GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    fun `field is not mapped so there is no empty value`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyNotEmptyValue("theOtherAddress")).isFalse()
    }

    @Test
    fun `column name is not given and fields are empty`() {
        val csv = """theName,theAddress,theBirthday
,,
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyNotEmptyValue()).isFalse()
    }

    @Test
    fun `column name is not given and at least one field is not empty`() {
        val csv = """theName,theAddress,theBirthday
,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyNotEmptyValue()).isTrue()
    }

    @Test
    fun `column name is not given and all fields are not empty`() {
        val csv = """theName,theAddress,theBirthday
,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[1]
        assertThat(record.anyNotEmptyValue()).isTrue()
    }

    @Test
    fun `field is not mapped, expected value can't be found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyValue("rocky", "theOtherAddress")).isFalse()
    }

    @Test
    fun `column name is not given, expected value found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyValue("rocky")).isTrue()
    }

    @Test
    fun `column name is not given, expected value not found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyValue("balboa")).isFalse()
    }

    @Test
    fun `column name is not given, expected value is found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[1]
        assertThat(record.anyValue("somewhere")).isTrue()
    }

    @Test
    fun `field is mapped, expected value can't be found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyValue("balboa", "theName")).isFalse()
    }

    @Test
    fun `field is mapped, expected value can be found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.anyValue("rocky", "theName")).isTrue()
    }

    @Test
    fun `field is mapped, expected values can't be found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.valueContains("theName", "balboa", "rambo")).isFalse()
    }

    @Test
    fun `field is mapped, expected at least one value can be found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.valueContains("theName", "balboa", "rocky", "rambo")).isTrue()
    }

    @Test
    fun `field is not mapped, expected values can't be found`() {
        val csv = """theName,theAddress,theBirthday
rocky,,2.2.2012
foo,somewhere,01.01.2010""".trimMargin()
        val record = CSVParser.parse(csv, CSVFormat.DEFAULT.withFirstRecordAsHeader()).records[0]
        assertThat(record.valueContains("theOtherName", "balboa", "rambo")).isFalse()
    }
}
