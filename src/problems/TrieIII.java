package problems;


/**
 * 设计一个支持以下两种操作的数据结构：
 * <p>
 * void addWord(word)
 * bool search(word)
 * search(word) 可以搜索文字或正则表达式字符串，字符串只包含字母 . 或 a-z 。 . 可以表示任何一个字母。
 */
public class TrieIII {
    TrieNodeIII root;

    /**
     * Initialize your data structure here.
     */
    public TrieIII() {
        this.root = new TrieNodeIII();
    }

    /**
     * Adds a word into the data structure.
     */
    public void addWord(String word) {
        if (word == null) {
            return;
        }
        TrieNodeIII node = root;
        for (char c : word.toCharArray()) {
            if (node.next[c - 'a'] == null) {
                node.next[c - 'a'] = new TrieNodeIII();
            }
            node = node.next[c - 'a'];
        }
        node.isEnd = true;
    }

    /**
     * Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter.
     */
    public boolean search(String word) {
        if (word == null) {
            return false;
        }
        return searchHelper(root, 0, word.toCharArray());
    }

    private boolean searchHelper(TrieNodeIII root, int i, char[] word) {
        if (root == null) {
            return false;
        }
        if (i == word.length) {
            return root.isEnd;
        }
        char c = word[i];
        if (c == '.') {
            for (TrieNodeIII node : root.next) {
                if (searchHelper(node, i + 1, word)) {
                    return true;
                }
            }
            return false;
        }
        if (root.next[c - 'a'] == null) {
            return false;
        } else {
            return searchHelper(root.next[c - 'a'], i + 1, word);
        }
    }
}

class TrieNodeIII {
    TrieNodeIII[] next;
    boolean isEnd;

    public TrieNodeIII() {
        this.next = new TrieNodeIII[26];
    }
}
