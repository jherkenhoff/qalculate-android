package com.jherkenhoff.qalculate.data

import com.jherkenhoff.libqalculate.ParseOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ParseOptionsRepository @Inject constructor() {

    private var parseOptions = ParseOptions()

    init {
        parseOptions.preserve_format = true
    }

    fun getParseOptions(): ParseOptions {
        return parseOptions
    }
}