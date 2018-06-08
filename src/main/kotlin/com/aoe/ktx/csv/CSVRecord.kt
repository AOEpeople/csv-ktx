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

import org.apache.commons.csv.CSVRecord

/**
 * Returns a column value by [name] if mapped  else the given [default] value
 */
operator fun CSVRecord.get(name: String, default: String): String = when {
    isMapped(name) -> this[name]
    else -> default
}

/**
 * Returns `true` if any value in given [columnName] is not empty.
 * If no [columnName] given it will search in the whole record
 */
fun CSVRecord.anyNotEmptyValue(vararg columnName: String) = when {
    columnName.isEmpty() -> any { it.isNotEmpty() }
    else -> columnName.any { isMapped(it) && this[it].isNotEmpty() }
}

/**
 * Returns `true` if any column value in [columnName] equals the [expected] String.
 * If no [columnName] given it will search in the whole record
 */
fun CSVRecord.anyValue(expected: String, vararg columnName: String) = when {
    columnName.isEmpty() -> any { it == expected }
    else -> columnName.any { isMapped(it) && this[it] == expected }
}

/**
 * Returns `true` if expected of [column] given in [expected]
 */
fun CSVRecord.valueContains(column: String, vararg expected: String) =
    isMapped(column) && expected.contains(this[column])