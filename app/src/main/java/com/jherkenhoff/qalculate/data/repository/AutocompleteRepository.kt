package com.jherkenhoff.qalculate.data.repository

import android.util.Log
import com.jherkenhoff.libqalculate.ExpressionItem
import com.jherkenhoff.libqalculate.ExpressionItemType
import com.jherkenhoff.libqalculate.MathFunction
import com.jherkenhoff.libqalculate.Unit
import com.jherkenhoff.libqalculate.Variable
import com.jherkenhoff.qalculate.data.CalculatorRepository
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.AutocompleteType
import com.jherkenhoff.qalculate.model.MutableTrie
import com.jherkenhoff.qalculate.model.Trie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

data class AutocompleteTries(
    val caseSensitiveTrie: Trie<AutocompleteItem> = MutableTrie(),
    val caseInsensitiveTrie: Trie<AutocompleteItem> = MutableTrie()
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

    fun buildTries(variables: List<Variable>, units: List<Unit>, functions: List<MathFunction>): AutocompleteTries {
        val caseSensitiveTrie = MutableTrie<AutocompleteItem>()
        val caseInsensitiveTrie = MutableTrie<AutocompleteItem>()

        units.forEach { unit ->
            val item = AutocompleteItem(
                type = AutocompleteType.FUNCTION,
                name = unit.name(),
                title = unit.title(true, true),
                category = unit.category(),
                abbreviations = emptyList(),
                description = unit.description(),
                typeBeforeCursor = unit.name(),
                typeAfterCursor = ""
            )
            for (i in 1..unit.countNames()) {
                val expressionName = unit.getName(i)

                if (expressionName.case_sensitive) {
                    caseSensitiveTrie.insert(expressionName.name, item)
                } else {
                    caseInsensitiveTrie.insert(expressionName.name, item)
                }
            }
        }

        functions.forEach { function ->
            val item = AutocompleteItem(
                type = AutocompleteType.FUNCTION,
                name = function.name() + "()",
                title = function.title(true, true),
                category = function.category(),
                abbreviations = emptyList(),
                description = function.description(),
                typeBeforeCursor = function.name() + "(",
                typeAfterCursor = ")"
            )
            for (i in 1..function.countNames()) {
                val expressionName = function.getName(i)

                if (expressionName.case_sensitive) {
                    caseSensitiveTrie.insert(expressionName.name, item)
                } else {
                    caseInsensitiveTrie.insert(expressionName.name, item)
                }
            }
        }

        return AutocompleteTries(
            caseSensitiveTrie,
            caseInsensitiveTrie
        )
    }

    fun getAutocompleteSuggestions(key: String): List<AutocompleteItem> {
        val caseSensitiveMatches = _tries.value.caseSensitiveTrie.search(key)
        val caseInsensitiveMatches = _tries.value.caseInsensitiveTrie.search(key.lowercase())

        val combinedMatches = (caseSensitiveMatches + caseInsensitiveMatches).sortedBy { it.matchDepth }
        return combinedMatches.map { it.value }
    }
}