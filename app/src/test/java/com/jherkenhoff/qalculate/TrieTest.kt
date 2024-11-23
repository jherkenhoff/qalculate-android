package com.jherkenhoff.qalculate

import com.jherkenhoff.qalculate.model.Trie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TrieTest {
    @Test
    fun `Should return empty list when empty`() {
        val trie = Trie<Int>()
        val result = trie.search("searchstring")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `Should return leaf node`() {
        val trie = Trie<Int>()
        trie.insert("house", 2)
        trie.insert("cat", 3)
        trie.insert("car", 4)

        val result = trie.search("car")
        assertEquals(1, result.size)
        assertTrue(result[0] == 4)
    }

    @Test
    fun `Should return multiple matching nodes`() {
        val trie = Trie<Int>()
        trie.insert("house", 2)
        trie.insert("cat", 3)
        trie.insert("car", 4)

        val result = trie.search("ca")
        assertEquals(2, result.size)
        assertTrue(result.contains(3))
        assertTrue(result.contains(4))
        assertFalse(result.contains(2))
    }
}