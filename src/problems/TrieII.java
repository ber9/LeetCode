package problems;

import java.util.ArrayList;
import java.util.List;


/**
 * 单词搜索 II
 * 给定一个二维网格 board 和一个字典中的单词列表 words，找出所有同时在二维网格和字典中出现的单词。
 * <p>
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母在一个单词中不允许被重复使用。
 */
public class TrieII {
    public List<String> findWords(char[][] board, String[] words) {
        List<String> res = new ArrayList<>();
        TrieNodeII root = buildTrie(words);
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++) {
                dfs(board, i, j, root, res);
            }
        return res;
    }

    private void dfs(char[][] board, int i, int j, TrieNodeII node, List<String> res) {
        char c = board[i][j];
        if (c == '#' || node.next[c - 'a'] == null) {
            return;
        }
        node = node.next[c - 'a'];
        if (node.word != null) {
            res.add(node.word);
            node.word = null;
        }
        board[i][j] = '#';
        if (i > 0) {
            dfs(board, i - 1, j, node, res);
        }
        if (j > 0) {
            dfs(board, i, j - 1, node, res);
        }
        if (i < board.length - 1) {
            dfs(board, i + 1, j, node, res);
        }
        if (j < board[0].length - 1) {
            dfs(board, i, j + 1, node, res);
        }
        board[i][j] = c;
    }

    private TrieNodeII buildTrie(String[] words) {
        TrieNodeII root = new TrieNodeII();
        for (String word : words) {
            TrieNodeII tmp = root;
            for (char c : word.toCharArray()) {
                int i = c - 'a';
                if (tmp.next[i] == null) {
                    tmp.next[i] = new TrieNodeII();
                }
                tmp = tmp.next[i];
            }
            tmp.word = word;
        }
        return root;
    }
}

class TrieNodeII {
    TrieNodeII[] next;
    String word;

    TrieNodeII() {
        this.next = new TrieNodeII[26];
    }
}
