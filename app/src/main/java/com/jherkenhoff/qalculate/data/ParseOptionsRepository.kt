package com.jherkenhoff.qalculate.data

import com.jherkenhoff.libqalculate.ParseOptions
import kotlinx.coroutines.flow.Flow

class ParseOptionsRepository {

    private var parseOptions = ParseOptions()

    fun getParseOptions(): ParseOptions {
        return parseOptions
    }
}