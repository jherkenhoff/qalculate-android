package com.jherkenhoff.qalculate.data

import androidx.lifecycle.viewModelScope
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathFunction
import com.jherkenhoff.libqalculate.Unit
import com.jherkenhoff.libqalculate.Variable
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.AutocompleteType
import com.jherkenhoff.qalculate.model.MutableTrie
import com.jherkenhoff.qalculate.model.Trie
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AutocompleteRepository @Inject constructor(
    private val calculatorRepository: CalculatorRepository,
    private val appScope: CoroutineScope
) {
    val trie = combine(
        calculatorRepository.variables,
        calculatorRepository.units,
        calculatorRepository.functions
    ) { variables, units, functions ->
        return@combine buildTree(variables, units, functions)
    }.stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MutableTrie<AutocompleteItem>()
    )

    fun buildTree(variables: List<Variable>, units: List<Unit>, functions: List<MathFunction>): Trie<AutocompleteItem> {
        val trie = MutableTrie<AutocompleteItem>()

        units.forEachIndexed { index, unit ->
            val item = AutocompleteItem(
                type = if (unit.isCurrency) AutocompleteType.CURRENCY else AutocompleteType.UNIT,
                name = unit.print(false, true),
                title = unit.title(),
                abbreviations = emptyList(),
                description = unit.description(),
                typeBeforeCursor = unit.print(false, true),
                typeAfterCursor = ""
            )
            trie.insert(unit.name(), item)
        }

        functions.forEachIndexed { index, function ->
            val item = AutocompleteItem(
                type = AutocompleteType.FUNCTION,
                name = function.name() + "()",
                title = function.title(),
                abbreviations = emptyList(),
                description = function.description(),
                typeBeforeCursor = function.name() + "(",
                typeAfterCursor = ")"
            )
            trie.insert(function.name(), item)
        }

        return trie
    }

    fun getAutocompleteSuggestions(key: String): List<AutocompleteItem> {
        return trie.value.search(key).map {
            it.value
        }
    }
}