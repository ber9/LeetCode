package problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DynamicProgramming {
    /**
     * 10. 正则表达式匹配
     */
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        boolean[][] matrix = new boolean[s.length() + 1][p.length() + 1];
        return matchHelper(s, p, matrix);
    }

    private boolean matchHelper(String s, String p, boolean[][] matrix) {
        matrix[0][0] = true;
        //初始化第一行
        for (int i = 1; i < matrix[0].length; i++) {
            if (p.charAt(i - 1) == '*' && matrix[0][i - 2]) {
                matrix[0][i] = true;
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (p.charAt(j - 1) == '*') {
                    /**
                     * 1.可以把前一个字符抵消
                     * 2.前一个字符与比较字符相同，则取前一字符的matrix值，相当于复制一份
                     */
                    matrix[i][j] = matrix[i][j - 2] || (matrix[i - 1][j] && (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.'));
                } else {
                    matrix[i][j] = matrix[i - 1][j - 1] && (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.');
                }
            }
        }
        return matrix[s.length()][p.length()];
    }

    /**
     * 62. 不同路径
     * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
     * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
     * 问总共有多少条不同的路径？
     *
     * dp
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int j = 0; j < m; j++) {
            dp[j][0] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (i == 0) {
                    dp[i][j] = dp[i][j - 1];
                    continue;
                }
                if (j == 0) {
                    dp[i][j] = dp[i - 1][j];
                    continue;
                }
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 63. 不同路径 II
     * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
     * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
     * 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？网格中的障碍物和空位置分别用 1 和 0 来表示。
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int rows = obstacleGrid.length;
        int cols = obstacleGrid[0].length;
        int[][] dp = new int[rows][cols];
        for (int i = 0; i < cols; i++) {
            if (obstacleGrid[0][i] == 1) {
                while (i < cols) {
                    dp[0][i] = 0;
                    i++;
                }
                break;
            } else {
                dp[0][i] = 1;
            }
        }
        for (int i = 0; i < rows; i++) {
            if (obstacleGrid[i][0] == 1) {
                while (i < rows) {
                    dp[i][0] = 0;
                    i++;
                }
                break;
            } else {
                dp[i][0] = 1;
            }
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                    continue;
                }
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];

            }
        }
        return dp[rows - 1][cols - 1];
    }

    /**
     * 64. 最小路径和
     * 给定一个包含非负整数的 m x n 网格，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
     *
     * 说明：每次只能向下或者向右移动一步。
     */
    public int minPathSum(int[][] grid) {
        if (grid == null) {
            return 0;
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] minMat = new int[rows][cols];
        minMat[0][0] = grid[0][0];
        for (int i = 1; i < cols; i++) {
            minMat[0][i] = minMat[0][i - 1] + grid[0][i];
        }
        for (int i = 1; i < rows; i++) {
            minMat[i][0] = minMat[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                minMat[i][j] = grid[i][j] + Math.min(minMat[i - 1][j], minMat[i][j - 1]);
            }
        }
        return minMat[rows - 1][cols - 1];
    }

    /**
     * 72. 编辑距离
     *
     * 给定两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数 。
     * 你可以对一个单词进行如下三种操作：
     * 插入一个字符
     * 删除一个字符
     * 替换一个字符
     *
     * 将最对 word1 处理 转化为 对 word1 和 word2 均处理
     *         word1 插入一个字符   DP[i-1][j] + 1 ->  DP[i][j]
     *         word1 删除一个字符 = word2 插入一个字符  DP[i][j-1] + 1 -> DP[i][j]
     *         word1 替换一个字符 = word1 word2 都替换一个字符 DP[i-1][j-1] + 1 -> DP[i][j]
     *         step2: 动态方程
     *         DP[i][j]  A、 word1 的 i 个字母 与 word2 的 第 j 个字母 相同
     *                      DP[i][j] =  DP[i-1][j-1]  #不操作
     *                   B、不相同,需要进行 插入 删除 或者 替换操作
     *                      DP[i][j]  =  min(DP[i-1][j] + 1,DP[i][j-1] + 1,DP[i-1][j-1]+1)
     */
    public int minDistance(String word1, String word2) {
        if (word1 == null || word2 == null) {
            return -1;
        }
        int rows = word1.length() + 1;
        int cols = word2.length() + 1;
        int[][] dp = new int[rows][cols];
        for (int i = 0; i < cols; i++) {
            dp[0][i] = i;
        }
        for (int i = 0; i < rows; i++) {
            dp[i][0] = i;
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j] + 1, Math.min(dp[i - 1][j - 1] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[rows - 1][cols - 1];
    }

    /**
     * 85. 最大矩形
     * 给定一个仅包含 0 和 1 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) {
            return 0;
        }
        int maxArea = 0;
        int[] heights = new int[matrix.length + 2];
        heights[0] = Integer.MIN_VALUE;
        heights[heights.length - 1] = Integer.MIN_VALUE;
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int index = i, cnt = 0;
                while (index < matrix[0].length && matrix[j][index] == '1') {
                    cnt++;
                    index++;
                }
                heights[j + 1] = cnt;
            }
            maxArea = Math.max(maxArea, maxArea(heights));
        }
        return maxArea;
    }

    private int maxArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        stack.push(0);
        for (int i = 1; i < heights.length;) {
            if (heights[i] < heights[stack.peek()]) {
                int start = stack.pop();
                //注意这里距离计算
                maxArea = Math.max(maxArea, (i - stack.peek() - 1) * heights[start]);
            } else {
                stack.push(i);
                i++;
            }
        }
        return maxArea;
    }

    /**
     * 96. 不同的二叉搜索树
     * 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？
     * 动态规划
     *
     * 假设n个节点存在二叉排序树的个数是G(n)，令f(i)为以i为根的二叉搜索树的个数
     *
     * 即有:G(n) = f(1) + f(2) + f(3) + f(4) + ... + f(n)
     *
     * n为根节点，当i为根节点时，其左子树节点个数为[1,2,3,...,i-1]，右子树节点个数为[i+1,i+2,...n]，所以当i为根节点时，其左子树节点个数为i-1个，右子树节点为n-i，即f(i) = G(i-1)*G(n-i),
     *
     * 上面两式可得:G(n) = G(0)*G(n-1)+G(1)*(n-2)+...+G(n-1)*G(0)
     */
    public int numTrees(int n) {
        if (n < 1) {
            return 0;
        }
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i < dp.length; i++) {
            /*for (int j = 1; j < i + 1; j++) {
                dp[i]  += dp[j - 1] * dp[i - j];
            }*/
            for (int j = 1; j <= i / 2; j++) {
                dp[i] += dp[j - 1] * dp[i - j];
            }
            dp[i] = dp[i] * 2;
            if (i % 2 == 1) {
                dp[i] += dp[i / 2] * dp[i / 2];
            }
        }
        return dp[n];
    }

    /**
     * 95. 不同的二叉搜索树 II
     * 给定一个整数 n，生成所有由 1 ... n 为节点所组成的二叉搜索树。
     */
    public List<TreeNode> generateTrees(int n) {
        List<TreeNode> res = new ArrayList<>();
        if (n < 1) {
            return res;
        }
        return generateTreesHelper(1, n);

    }

    private List<TreeNode> generateTreesHelper(int left, int right) {
        List<TreeNode> res = new ArrayList<>();
        if (left > right) {
            res.add(new TreeNode(-1));
            return res;
        }
        for (int i = left; i <= right; i++) {
            List<TreeNode> leftRes = generateTreesHelper(left, i - 1);
            List<TreeNode> rightRes = generateTreesHelper(i + 1, right);
            for (TreeNode lNode : leftRes) {
                for (TreeNode rNode : rightRes) {
                    TreeNode root = new TreeNode(i);
                    root.left = lNode.val == -1 ? null : lNode;
                    root.right = rNode.val == -1 ? null : rNode;
                    res.add(root);
                }
            }
        }
        return res;
    }

    /**
     * 97. 交错字符串
     * 给定三个字符串 s1, s2, s3, 验证 s3 是否是由 s1 和 s2 交错组成的。
     *
     * 示例 1:
     *
     * 输入: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
     * 输出: true
     *
     * dp[i][j]表示s1[0~i-1]和s2[0~j-1]能否交错组成s3[0~i+j-1]。 想好边缘条件，字符串涉及子串匹配啥的统统dp完事。
     */
    public boolean isInterleave(String s1, String s2, String s3) {
        if (s1 == null || s2 == null || s3 == null || s3.length() != s1.length() + s2.length()) {
            return false;
        }
        int rows = s1.length() + 1;
        int cols = s2.length() + 1;
        int len = s3.length();
        boolean[][] dp = new boolean[rows][cols];
        dp[0][0] = true;
        for (int i = 1; i < cols; i++) {
            dp[0][i] = dp[0][i - 1] && s2.charAt(i - 1) == s3.charAt(i - 1);
        }
        for (int i = 1; i < rows; i++) {
            dp[i][0] = dp[i - 1][0] && s1.charAt(i - 1) == s3.charAt(i - 1);
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                dp[i][j] = dp[i - 1][j] && s1.charAt(i - 1) == s3.charAt(i - 1 + j) || dp[i][j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1);
            }
        }
        return dp[rows - 1][cols - 1];
    }




}
