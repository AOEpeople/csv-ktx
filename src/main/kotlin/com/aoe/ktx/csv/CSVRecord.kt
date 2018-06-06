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
