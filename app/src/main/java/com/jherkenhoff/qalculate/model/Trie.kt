package com.jherkenhoff.qalculate.model



interface Trie<Value> {
    data class SearchResult<Value> (
        val value: Value,
        val matchDepth: Int
    )
    fun search(key: String): List<SearchResult<Value>>
}

class MutableTrie<Value> : Trie<Value> {

    private data class Node<Value> (
        val children: MutableMap<Char, Node<Value>> = mutableMapOf(),
        val values: MutableList<Value> = mutableListOf(),
    )

    private val root = Node<Value>()

    fun insert(key: String, value: Value) {
        var currentNode = root

        for (char in key) {
            currentNode = currentNode.children.getOrPut(char) { Node() }
        }
        currentNode.values.add(value)
    }

    override fun search(key: String): List<Trie.SearchResult<Value>> {
        var currentNode = root

        for (char in key) {
            currentNode = currentNode.children[char] ?: return emptyList()
        }

        return collectMatches(currentNode, key, 0)
    }

    private fun collectMatches(node: Node<Value>, prefix: String, currentDepth: Int): List<Trie.SearchResult<Value>> {
        val matches = mutableListOf<Trie.SearchResult<Value>>()

        matches.addAll(node.values.map { Trie.SearchResult(it, matchDepth = currentDepth) })

        for ((char, child) in node.children) {
            val subjacentMatches = collectMatches(child, prefix + char, currentDepth+1)
            matches.addAll(subjacentMatches)
        }

        return matches
    }
}