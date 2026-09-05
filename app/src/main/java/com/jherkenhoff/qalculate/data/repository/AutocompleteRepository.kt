package com.jherkenhoff.qalculate.data.repository

import android.util.Log
import com.jherkenhoff.qalculate.domain.model.ExpressionItem
import com.jherkenhoff.qalculate.domain.model.MathFunction
import com.jherkenhoff.qalculate.domain.model.MutableTrie
import com.jherkenhoff.qalculate.domain.model.Trie
import com.jherkenhoff.qalculate.domain.model.AbstractMathVariable
import com.jherkenhoff.qalculate.domain.model.CompositeUnit
import com.jherkenhoff.qalculate.domain.model.MathUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

data class AutocompleteTries(
    val caseSensitiveTrie: Trie<ExpressionItem> = MutableTrie(),
    val caseInsensitiveTrie: Trie<ExpressionItem> = MutableTrie()
)

@Singleton
class AutocompleteRepository @Inject constructor(
    calculatorRepository: CalculatorRepository,
    appScope: CoroutineScope
) {
    private val _tries = combine(
        calculatorRepository.variables,
        calculatorRepository.units,
        calculatorRepository.functions
    ) { variables, units, functions ->
        return@combine buildTries(variables, units, functions)
    }.stateIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        initialValue = AutocompleteTries()
    )

    fun buildTries(variables: List<AbstractMathVariable>, units: List<MathUnit>, functions: List<MathFunction>): AutocompleteTries {
        val caseSensitiveTrie = MutableTrie<ExpressionItem>()
        val caseInsensitiveTrie = MutableTrie<ExpressionItem>()

        units.forEach { unit ->

            if (unit is CompositeUnit) {
                Log.i("Moin", unit.childUnits.toString())
            }

            unit.names.forEach {
                if (it.caseSensitive) {
                    caseSensitiveTrie.insert(it.name, unit)
                } else {
                    caseInsensitiveTrie.insert(it.name, unit)
                }
            }
        }

        functions.forEach { function ->
            function.names.forEach {
                if (it.caseSensitive) {
                    caseSensitiveTrie.insert(it.name, function)
                } else {
                    caseInsensitiveTrie.insert(it.name, function)
                }
            }
        }

        variables.forEach { variable ->
            variable.names.forEach {
                if (it.caseSensitive) {
                    caseSensitiveTrie.insert(it.name, variable)
                } else {
                    caseInsensitiveTrie.insert(it.name, variable)
                }
            }
        }

        return AutocompleteTries(
            caseSensitiveTrie,
            caseInsensitiveTrie
        )
    }

    fun getAutocompleteSuggestions(key: String): List<ExpressionItem> {
        val caseSensitiveMatches = _tries.value.caseSensitiveTrie.search(key)
        val caseInsensitiveMatches = _tries.value.caseInsensitiveTrie.search(key.lowercase())

        val combinedMatches = (caseSensitiveMatches + caseInsensitiveMatches).sortedBy { it.matchDepth }
        return combinedMatches.map { it.value }.distinct()
    }
}