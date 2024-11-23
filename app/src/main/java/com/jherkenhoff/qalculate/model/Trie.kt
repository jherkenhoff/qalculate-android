package com.jherkenhoff.qalculate.model



class Trie<Value> {

    private data class Node<Value> (
        val children: MutableMap<Char, Node<Value>> = mutableMapOf(),
        val values: MutableList<Value> = mutableListOf(),
    )

    data class SearchResult<Value> (
        val value: Value,
        val matchDepth: Int
    )

    private val root = Node<Value>()

    fun insert(key: String, value: Value) {
        var currentNode = root

        for (char in key) {
            currentNode = currentNode.children.getOrPut(char) { Node() }
        }
        currentNode.values.add(value)
    }

    fun search(key: String): List<SearchResult<Value>> {
        var currentNode = root

        for (char in key) {
            currentNode = currentNode.children[char] ?: return emptyList()
        }

        return collectMatches(currentNode, key, 0)
    }

    private fun collectMatches(node: Node<Value>, prefix: String, currentDepth: Int): List<SearchResult<Value>> {
        val matches = mutableListOf<SearchResult<Value>>()

        matches.addAll(node.values.map { SearchResult(it, matchDepth = currentDepth) })

        for ((char, child) in node.children) {
            val subjacentMatches = collectMatches(child, prefix + char, currentDepth+1)
            matches.addAll(subjacentMatches)
        }

        return matches
    }
}