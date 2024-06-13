package com.jherkenhoff.qalculate.data

import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ParseOptions

class EvaluationOptionsRepository {

    private var evaluationOptions = EvaluationOptions()

    fun getEvaluationOptions(): EvaluationOptions {
        return evaluationOptions
    }
}