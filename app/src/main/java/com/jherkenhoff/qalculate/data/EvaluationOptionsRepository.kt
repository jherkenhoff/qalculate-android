package com.jherkenhoff.qalculate.data

import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ParseOptions
import javax.inject.Inject

class EvaluationOptionsRepository @Inject constructor() {

    private var evaluationOptions = EvaluationOptions()

    fun getEvaluationOptions(): EvaluationOptions {
        return evaluationOptions
    }
}