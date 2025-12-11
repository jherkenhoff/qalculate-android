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
    private val trie = Trie<AutocompleteItem>()

    fun initialize() {

        calc.units.forEachIndexed { index, unit ->
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

        calc.functions.forEachIndexed { index, function ->

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
    }

    fun getAutocompleteSuggestions(key: String): List<AutocompleteItem> {

        return trie.search(key).map {
            it.value
        }
    }
}