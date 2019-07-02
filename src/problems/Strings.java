package problems;

import java.util.*;
import java.util.LinkedList;
import java.util.concurrent.PriorityBlockingQueue;

public class Strings {
    /**
     * 336. 回文对
     * 给定一组唯一的单词， 找出所有不同 的索引对(i, j)，使得列表中的两个单词， words[i] + words[j] ，可拼接成回文串。
     */
    public List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> res = new ArrayList<>();
        if (words == null || words.length < 2) {
            return res;
        }
        //map存word索引
        HashMap<String, Integer> wordsMap = new HashMap<>(16);
        for (int i = 0; i < words.length; i++) {
            wordsMap.put(words[i], i);
        }
        for (int i = 0; i < words.length; i++) {
            //将word遍历分段截,j<=
            for (int j = 0; j <= words[i].length(); j++) {
                String str1 = words[i].substring(0, j);
                //从j开始截
                String str2 = words[i].substring(j);
                if (isPalindrome(str1)) {
                    //str1是回文的话，找到str2的逆序串就行了
                    Integer index = wordsMap.get(new StringBuilder(str2).reverse().toString());
                    if (index != null && i != index) {
                        res.add(Arrays.asList(new Integer[]{index, i}));
                    }
                }
                if (isPalindrome(str2)) {
                    Integer index = wordsMap.get(new StringBuilder(str1).reverse().toString());
                    // check "str.length() != 0" to avoid duplicates
                    if (index != null && i != index && str2.length() != 0) {
                        res.add(Arrays.asList(i, index));
                    }
                }
            }
        }
        return res;
    }

    private boolean isPalindrome(String str) {
        int i = 0, j = str.length() - 1;
        while (i < j) {
            if (str.charAt(i) != str.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    /**
     * 3. 无重复字符的最长子串
     * 滑动窗口
     */
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> chars = new HashMap<>(16);
        int len = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (chars.containsKey(c)) {
                Integer index = chars.get(c);
                //窗口移动
                if (index >= start) {
                    start = index + 1;
                }
            }
            //每次循环都比较，记录大小
            len = Math.max(len, i - start + 1);
            chars.put(c, i);
        }
        return len;
    }

    /**
     * 5. 最长回文子串
     * 中心拓展法
     */
    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = lenPalindrome(s, i, i);
            int len2 = lenPalindrome(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start + 1) {
                //更长的，将start，end更新，可以先将奇偶情况变化分别列出，再做融合
                start = i - (len-1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private int lenPalindrome(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        //注意是-1，因为最后l和r已经不属于回文串内了
        return r - l - 1;
    }

    /**
     * 6. Z 字形变换
     * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
     *
     * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
     *
     * L   C   I   R
     * E T O E S I I G
     * E   D   H   N
     * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
     */
    public String convert(String s, int numRows) {
        if (s == null || s.length() == 0 || numRows >= s.length() || numRows == 1) {
            return s;
        }
        StringBuilder res = new StringBuilder();
        //下一个打印位置
        int cycleLen = 2 * numRows - 2;
        for (int i = 0; i < numRows; i++) {
            for(int j = 0; j + i < s.length(); j += cycleLen) {
                res.append(s.charAt(i + j));
                //不是第一行也不是最后一行,插在中间的是j + cycleLen - i
                if (i != 0 && i != numRows - 1 && j + cycleLen - i < s.length()) {
                    res.append(s.charAt(j + cycleLen - i));
                }
            }
        }
        return res.toString();
    }
    /**
     * 10. 正则表达式匹配
     */
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        return matchHelper(s, 0, p, 0);
    }

    private boolean matchHelper(String s, int i, String p, int j) {
        //两个都走到尾才算ok
        if (j >= p.length()) {
            return i >= s.length();
        }
        if (j < p.length() - 1 && p.charAt(j + 1) == '*') {
            if (i < s.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '.')) {
                //注意这里，列举所有情况要
                return matchHelper(s, i + 1, p, j) || matchHelper(s, i, p, j + 2);
            } else {
                return matchHelper(s, i, p, j + 2);
            }
        }
        if (i < s.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '.')) {
            return matchHelper(s, i + 1, p, j + 1);
        } else {
            return false;
        }
    }

    /**
     * 14. 最长公共前缀
     * 编写一个函数来查找字符串数组中的最长公共前缀。
     *
     * 如果不存在公共前缀，返回空字符串 ""。
     * 分治法
     */
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        return longestCommonPrefix(strs, 0, strs.length - 1);
    }

    private String longestCommonPrefix(String[] strs, int i, int j) {
        if (i == j) {
            return strs[i];
        } else {
            int mid = (i + j) / 2;
            String a = longestCommonPrefix(strs, i, mid);
            String b = longestCommonPrefix(strs, mid + 1, j);
            return prefixMerge(a, b);
        }
    }

    private String prefixMerge(String a, String b) {
        int minLen = Math.min(a.length(), b.length());
        for (int i = 0; i < minLen; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return a.substring(0, i);
            }
        }
        return a.substring(0, minLen);
    }


    /**
     * 28. 实现strStr()
     * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
     * 当 needle 是空字符串时我们应当返回 0
     */
    public int strStr(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() == 0) {
            return -1;
        }
        /**
         * shiftMap用于当遇到不匹配情况，看haystack后一个字符是否在模式串needle中，
         * 如果在的话，需要haystack后移到模式串不匹配字符跟后一个字符对齐位置。
         * 如果不在则可以直接跳过这一段。
         */
        HashMap<Character, Integer> shiftMap = new HashMap<>(16);
        for (int i = 0; i < needle.length(); i++) {
            shiftMap.put(needle.charAt(i), needle.length() - i);
        }
        int i = 0;
        int j;
        while (i <= haystack.length() - needle.length()) {
            j = 0;
            while (haystack.charAt(i + j) == needle.charAt(j)) {
                if (j == needle.length() - 1) {
                    return i;
                }
                j++;
            }
            if (i < haystack.length() - needle.length() && shiftMap.containsKey(haystack.charAt(i + needle.length()))) {
                i += shiftMap.get(haystack.charAt(i + needle.length()));
            } else {
                i += needle.length() + 1;
            }
        }
        return -1;
    }

    /**
     * 22. 括号生成
     * 给出 n 代表生成括号的对数，请你写出一个函数，使其能够生成所有可能的并且有效的括号组合。
     * 回溯法
     * 只有在我们知道序列仍然保持有效时才添加 '(' or ')'，而不是像 方法一 那样每次添加。我们可以通过跟踪到目前为止放置的左括号和右括号的数目来做到这一点，
     * 如果我们还剩一个位置，我们可以开始放一个左括号。 如果它不超过左括号的数量，我们可以放一个右括号。
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        if (n == 0) {
            return res;
        }
        backtrace(res, "", 0, 0, n);
        return res;
    }
    private void backtrace(List<String> res, String str, int open, int close, int n) {
        if (n * 2 == str.length()) {
            res.add(str);
            return;
        }
        if (open < n) {
            backtrace(res, str + "(", open + 1, close, n);
        }
        if (open > close) {
            backtrace(res, str + ")", open, close + 1, n);
        }
    }

    /**
     * 32. 最长有效括号
     * 给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。
     * 一般对于最长XX问题容易想到利用DP求解，在这题中利用逆向DP，设状态dp[i]为从i到len - 1中，以i开头的最长合法子串长度：
     *
     * 初始化：dp[len - 1] = 0
     * 如果s[i] = ')'，则跳过，因为不可能有由'('开头的串
     * 如果s[i] = '('，则需要找到右括号和它匹配，可以跳过以i + 1开头的合法子串，则需要看j = i + dp[i + 1] + 1是否为右括号。
     * 如果没越界且为右括号，那么有dp[i] = dp[i + 1] + 2，此外在这个基础上还要将j + 1开头的子串加进来（只要不越界）
     */
    public int longestValidParentheses(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        int[] dp = new int[s.length()];
        dp[s.length() - 1] = 0;
        int res = 0,j;
        for (int i = s.length() - 2; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                j = i + dp[i + 1] + 1;
                if (j < s.length() && s.charAt(j) == ')') {
                    dp[i] = dp[i + 1] + 2;
                    if (j + 1 < s.length()) {
                        dp[i] += dp[j + 1];
                    }
                    res = Math.max(dp[i], res);
                }
            }
        }
        return res;
    }

    /**
     * 49. 字母异位词分组
     * 给定一个字符串数组，将字母异位词组合在一起。字母异位词指字母相同，但排列不同的字符串。
     *
     * 当且仅当它们的字符计数（每个字符的出现次数）相同时，两个字符串是字母异位词。
     * 我们可以将每个字符串 \text{s}s 转换为字符数 \text{count}count，由26个非负整数组成，表示 \text{a}a，\text{b}b，\text{c}c 的数量等。我们使用这些计数作为哈希映射的基础。
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        if (strs == null || strs.length == 0) {
            return res;
        }
        HashMap<String, List> map = new HashMap<>(16);
        int[] cnt = new int[26];
        for (String str : strs) {
            Arrays.fill(cnt, 0);
            for (int i = 0; i < str.length(); i++) {
                cnt[str.charAt(i) - 'a'] += 1;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cnt.length; i++) {
                sb.append('#');
                sb.append(cnt[i]);
            }
            String key = sb.toString();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<String>());
            }
            map.get(key).add(str);
        }
        return new ArrayList(map.values());
    }
}
