package com.jherkenhoff.qalculate.data

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.AutocompleteType
import com.jherkenhoff.qalculate.model.Trie
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AutocompleteRepository @Inject constructor(
    private val calc: Calculator
) {
    private val unitTrie = Trie<Int>()
    private val functionTree = Trie<Int>()
    private val variableTree = Trie<Int>()

    fun initialize() {
        calc.units.forEachIndexed { index, unit ->
            unitTrie.insert(unit.name(), index)
        }
        calc.functions.forEachIndexed { index, function ->
            functionTree.insert(function.name(), index)
        }
    }

    fun getAutocompleteSuggestions(key: String): List<AutocompleteItem> {

        val unitSuggestions = unitTrie.search(key).map {
            val unit = calc.units[it.value]

            return@map AutocompleteItem(
                type = if (unit.isCurrency) AutocompleteType.CURRENCY else AutocompleteType.UNIT,
                name = unit.print(false, true),
                title = unit.title(),
                abbreviations = emptyList(),
                description = unit.description(),
                matchDepth = it.matchDepth,
                typeBeforeCursor = unit.print(false, true) + " ",
                typeAfterCursor = ""
            )
        }

        val functionSuggestions = functionTree.search(key).map {
            val function = calc.functions[it.value]

            return@map AutocompleteItem(
                type = AutocompleteType.FUNCTION,
                name = function.name() + "()",
                title = function.title(),
                abbreviations = emptyList(),
                description = function.description(),
                matchDepth = it.matchDepth,
                typeBeforeCursor = function.name() + "(",
                typeAfterCursor = ")"
            )
        }

        return unitSuggestions + functionSuggestions
    }
}