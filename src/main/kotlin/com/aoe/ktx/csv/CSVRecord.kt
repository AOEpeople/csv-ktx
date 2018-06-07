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
 * Returns a value by name, if not mapped returns the given default value
 *
 * @return the value found or default if not found
 */
operator fun CSVRecord.get(name: String, default: String): String = when {
    isMapped(name) -> this[name]
    else -> default
}

/**
 * Check if there is any value in given fields in the record
 */
fun CSVRecord.anyNotEmptyValue(vararg field: String) =
        field.any { isMapped(it) && this[it].isNotEmpty() }
