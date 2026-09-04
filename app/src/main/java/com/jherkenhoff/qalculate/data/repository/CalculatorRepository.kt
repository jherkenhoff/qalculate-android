package com.jherkenhoff.qalculate.data.repository

import android.util.Log
import com.jherkenhoff.libqalculate.AutomaticApproximation
import com.jherkenhoff.libqalculate.AutomaticFractionFormat
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.mapper.toModel
import com.jherkenhoff.qalculate.domain.model.MathFunction
import com.jherkenhoff.qalculate.domain.model.MathUnit
import com.jherkenhoff.qalculate.domain.model.UserPreferences
import com.jherkenhoff.qalculate.domain.model.AbstractMathVariable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

data class CalculatorResult(
    val parsed: String,
    val result: String
)

class CalculatorRepository @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appScope: CoroutineScope
) {
    private val calc = Calculator()

    private var _variables = MutableStateFlow(calc.variables.map { it.toModel() })
    val variables: StateFlow<List<AbstractMathVariable>> = _variables.asStateFlow()

    private var _units = MutableStateFlow(calc.units.map { it.toModel() })
    val units: StateFlow<List<MathUnit>> = _units.asStateFlow()

    private var _functions = MutableStateFlow(calc.functions.map { it.toModel() })
    val functions: StateFlow<List<MathFunction>> = _functions.asStateFlow()

    val ans = com.jherkenhoff.libqalculate.KnownVariable(calc.temporaryCategory(), "ans", "undefined")

    init {
        calc.loadGlobalDefinitions()
        calc.loadLocalDefinitions()
        calc.addVariable(ans)

        userPreferencesRepository.userPreferencesFlow.onEach {
            when (it.decimalSeparator) {
                UserPreferences.DecimalSeparator.DOT -> calc.useDecimalPoint()
                UserPreferences.DecimalSeparator.COMMA -> calc.useDecimalComma()
            }
        }.launchIn(appScope)

        appScope.launch {
            // Load exchange rates
            // TODO: Error handling, i.e. show something meaningful in the UI
            // TODO: Is this the right spot to do it?

            for (i in 1..3) {
                val outputFile = File(calc.getExchangeRatesFileName(i))
                outputFile.parentFile?.mkdirs()

                val url = URL(calc.getExchangeRatesUrl(i))
                val connection = url.openConnection() as HttpURLConnection

                connection.connectTimeout = 1_000 // 1 second
                connection.readTimeout = 1_000    // 1 second

                try {
                    connection.inputStream.use { input ->
                        outputFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d("Qalculate", "Downloaded exchange rate data to ${outputFile.absolutePath}")
                } catch (e: Exception) {
                    Log.e("Qalculate", "Failed to download exchange rates from ${url.toString()}", e)
                }
            }

            calc.loadExchangeRates()
        }

        _variables.value = calc.variables.map { it.toModel() }
        _units.value = calc.units.map { it.toModel() }
        _functions.value = calc.functions.map { it.toModel() }
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

    fun findAllUnknowns(
        input: String
    ) : List<String> {
        val unlocalizedInput = calc.unlocalizeExpression(input)

        val ms = calc.parse(input)

        val unknowns = MathStructure()

        ms.findAllUnknowns(unknowns)

        return (1..unknowns.countChildren()).map { i ->
            unknowns.getChild(i).print()
        }
    }
}
