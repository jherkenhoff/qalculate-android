package com.jherkenhoff.qalculate.data

import androidx.compose.foundation.layout.FlowRow
import com.jherkenhoff.libqalculate.AutomaticApproximation
import com.jherkenhoff.libqalculate.AutomaticFractionFormat
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.MathFunction
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.Unit
import com.jherkenhoff.libqalculate.Variable
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CalculatorRepository @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appScope: CoroutineScope
) {
    private val calculator = Calculator()

    private var _variables = MutableStateFlow<List<Variable>>(calculator.variables)
    val variables: StateFlow<List<Variable>> = _variables.asStateFlow()

    private var _units = MutableStateFlow<List<Unit>>(calculator.units)
    val units: StateFlow<List<Unit>> = _units.asStateFlow()

    private var _functions = MutableStateFlow<List<MathFunction>>(calculator.functions)
    val functions: StateFlow<List<MathFunction>> = _functions.asStateFlow()

    init {
        userPreferencesRepository.userPreferencesFlow.onEach {
            when (it.decimalSeparator) {
                UserPreferences.DecimalSeparator.DOT -> calculator.useDecimalPoint()
                UserPreferences.DecimalSeparator.COMMA -> calculator.useDecimalComma()
            }
        }.launchIn(appScope)
    }

    fun calculateAndPrint(
        input: String,
        evaluationOptions: EvaluationOptions,
        printOptions: PrintOptions
    ): String {
        val unlocalizedInput = calculator.unlocalizeExpression(input, evaluationOptions.parse_options)

        return calculator.calculateAndPrint(
            unlocalizedInput,
            2000,
            evaluationOptions,
            printOptions,
            AutomaticFractionFormat.AUTOMATIC_FRACTION_OFF,
            AutomaticApproximation.AUTOMATIC_APPROXIMATION_OFF,
            null,
            -1,
            null,
            true,
            1,
            libqalculateConstants.TAG_TYPE_HTML
        )
    }

    fun parse(
        input: String,
        parseOptions: ParseOptions,
        printOptions: PrintOptions
    ): String {
        val unlocalizedInput = calculator.unlocalizeExpression(input, parseOptions)

        // TODO: Implement proper conversion handling
        val toExpressions = unlocalizedInput.split(" to ")
        if (toExpressions.size == 2) {
            val beforeToExpression = calculator.parse(toExpressions.first(), parseOptions)
            val afterToExpression = calculator.parse(toExpressions.last(), parseOptions)

            val beforeToString = calculator.print(beforeToExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
            val afterToString = calculator.print(afterToExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)

            return "$beforeToString to $afterToString"
        } else {
            val parsedExpression = calculator.parse(unlocalizedInput, parseOptions)
            return calculator.print(parsedExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
        }
    }
}