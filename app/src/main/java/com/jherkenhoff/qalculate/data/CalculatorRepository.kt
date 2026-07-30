package com.jherkenhoff.qalculate.data

import com.jherkenhoff.libqalculate.AutomaticApproximation
import com.jherkenhoff.libqalculate.AutomaticFractionFormat
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.KnownVariable
import com.jherkenhoff.libqalculate.MathFunction
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.Unit
import com.jherkenhoff.libqalculate.Variable
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
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
    private val calc = Calculator()

    private var _variables = MutableStateFlow<List<Variable>>(calc.variables)
    val variables: StateFlow<List<Variable>> = _variables.asStateFlow()

    private var _units = MutableStateFlow<List<Unit>>(calc.units)
    val units: StateFlow<List<Unit>> = _units.asStateFlow()

    private var _functions = MutableStateFlow<List<MathFunction>>(calc.functions)
    val functions: StateFlow<List<MathFunction>> = _functions.asStateFlow()

    val ans = KnownVariable(calc.temporaryCategory(), "ans", "undefined")

    init {
        calc.loadGlobalDefinitions()

        calc.addVariable(ans)

        userPreferencesRepository.userPreferencesFlow.onEach {
            when (it.decimalSeparator) {
                UserPreferences.DecimalSeparator.DOT -> calc.useDecimalPoint()
                UserPreferences.DecimalSeparator.COMMA -> calc.useDecimalComma()
            }
        }.launchIn(appScope)

        _variables.value = calc.variables
        _units.value = calc.units
        _functions.value = calc.functions
    }

    fun setAnsExpression(expression: String) {
        val parsedExpression = calc.parse(expression)
        ans.set(parsedExpression)
    }

    fun calculateAndPrint(
        input: String,
        evaluationOptions: EvaluationOptions,
        printOptions: PrintOptions,
        format: Boolean = true
    ): String {
        val unlocalizedInput = calc.unlocalizeExpression(input, evaluationOptions.parse_options)

        return calc.calculateAndPrint(
            unlocalizedInput,
            2000,
            evaluationOptions,
            printOptions,
            AutomaticFractionFormat.AUTOMATIC_FRACTION_OFF,
            AutomaticApproximation.AUTOMATIC_APPROXIMATION_OFF,
            null,
            -1,
            null,
            format,
            if (format) 1 else 0,
            libqalculateConstants.TAG_TYPE_HTML
        )
    }

    fun parseAndPrint(
        input: String,
        parseOptions: ParseOptions,
        printOptions: PrintOptions
    ): String {
        val unlocalizedInput = calc.unlocalizeExpression(input, parseOptions)

        // TODO: Implement proper conversion handling
        val parts = unlocalizedInput.split(Regex("""(?=\s(?:to|where)\s)|(?<=\s(?:to|where)\s)"""))

        return parts.joinToString("") {
            if (it in setOf(" to ", " where ")) {
                it
            } else {
                val expression = calc.parse(it, parseOptions)
                calc.print(expression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
            }
        }
    }
}
