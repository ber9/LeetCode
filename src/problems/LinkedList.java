package problems;

import java.util.List;

class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

/**
 * @author DELL
 */
public class LinkedList {

    /**
     * 合并k个排序链表,分治法，归并思想
     *
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        return sort(lists, 0, lists.length - 1);
    }

    private ListNode sort(ListNode[] lists, int i, int j) {
        if (i < j) {
            int mid = (i + j) >> 1;
            ListNode a = sort(lists, i, mid);
            ListNode b = sort(lists, mid + 1, j);
            return merge(a, b);
        }
        return lists[i];//!!!
    }

    private ListNode merge(ListNode a, ListNode b) {
        ListNode dummyNode = new ListNode(0);
        ListNode cur = dummyNode;
        while (a != null && b != null) {
            if (a.val < b.val) {
                cur.next = a;
                a = a.next;
                cur = cur.next;
            } else {
                cur.next = b;
                b = b.next;
                cur = cur.next;
            }
        }
        if (b != null) {
            cur.next = b;
        }
        if (a != null) {
            cur.next = a;
        }
        return dummyNode.next;
    }

    /**
     * 两两交换链表中的节点
     */
    //递归
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode next = head.next;
        head.next = swapPairs(next.next);
        next.next = head;
        return next;
    }

    //非递归
    public ListNode swapPairsII(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummyNode = head.next;
        ListNode next = null;
        ListNode pre = null;
        while (head != null && head.next != null) {
            next = head.next;
            if (pre == null) {
                head.next = next.next;
                next.next = head;
            } else {
                head.next = next.next;
                next.next = head;
                pre.next = next;
            }
            pre = head;
            head = head.next;
        }
        return dummyNode;
    }

    /**
     * K个一组翻转链表
     * 给出一个链表，每 k 个节点一组进行翻转，并返回翻转后的链表。
     * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么将最后剩余节点保持原有顺序。
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null || k <= 1) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        ListNode cur = head;
        int cnt = 1;
        while (cur != null) {
            if (cnt % k == 0) {
                pre = reverseKNodes(pre, cur.next);
                cur = pre.next;
            } else {
                cur = cur.next;
            }
            cnt++;
        }
        return dummy.next;
    }

    /**
     * @param pre 翻转链前一节点
     * @param suf 翻转链后一节点
     * @return 新的pre
     */
    private ListNode reverseKNodes(ListNode pre, ListNode suf) {
        ListNode lpre = pre.next;
        ListNode cur = lpre.next;
        while (cur != suf) {
            lpre.next = cur.next;
            cur.next = pre.next;
            pre.next = cur;
            cur = lpre.next;
        }
        return lpre;
    }

    /**
     * 旋转链表
     * 给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || k == 0) {
            return head;
        }
        int cnt = 1;
        ListNode last = head;
        while (last.next != null) {
            last = last.next;
            cnt++;
        }
        k = k % cnt;
        if (k == 0) {
            return head;
        }
        ListNode cur = head;
        for (int i = 1; i < cnt - k; i++) {
            cur = cur.next;
        }
        ListNode res = cur.next;
        cur.next = null;
        last.next = head;
        return res;
    }

    /**
     * 删除排序链表中的重复元素 II
     * 给定一个排序链表，删除所有含有重复数字的节点，只保留原始链表中 没有重复出现 的数字。
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(head.val + 1);
        dummy.next = head;
        ListNode pre = dummy;
        ListNode cur = pre.next;
        ListNode next;
        while (cur != null) {
            if (cur.next != null && cur.val == cur.next.val) {
                while (cur.next != null && cur.val == cur.next.val) {
                    cur = cur.next;
                }
                pre.next = cur.next;
                cur = pre.next;
            } else {
                pre = pre.next;
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    /**
     * 分隔链表
     * 给定一个链表和一个特定值 x，对链表进行分隔，使得所有小于 x 的节点都在大于或等于 x 的节点之前。
     * <p>
     * 你应当保留两个分区中每个节点的初始相对位置。
     */
    public ListNode partition(ListNode head, int x) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode low = new ListNode(-1);
        ListNode high = new ListNode(-1);
        ListNode lowCursor = low;
        ListNode highCursor = high;
        while (head != null) {
            if (head.val < x) {
                lowCursor.next = head;
                lowCursor = lowCursor.next;
            } else {
                highCursor.next = head;
                highCursor = highCursor.next;
            }
            head = head.next;
        }
        highCursor.next = null;//注意置空
        lowCursor.next = high.next;
        return low.next;
    }

    /**
     * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || head.next == null) {
            return head;
        }
        //记得dummynode
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        int i = 0;
        ListNode pre = dummy;
        while (i < m - 1) {
            pre = pre.next;
            i++;
        }
        //记录，画图
        ListNode lpre = pre.next;
        ListNode cur = lpre.next;
        i = m + 1;
        while (i <= n) {
            i++;
            lpre.next = cur.next;
            cur.next = pre.next;
            pre.next = cur;
            cur = lpre.next;
        }
        return dummy.next;
    }

    /**
     * 有序链表转换二叉搜索树
     * 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
     */
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode fast = head;
        ListNode slow = head;
        ListNode pre = null;
        //get中间节点
        while (fast.next != null && fast.next.next != null) {
            pre = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode sufNode = slow.next;
        slow.next = null;
        TreeNode root = new TreeNode(slow.val);
        if (pre != null) {
            pre.next = null;
            root.left = sortedListToBST(head);
        } else { //已经只剩一个节点
            root.left = null;
        }
        root.right = sortedListToBST(sufNode);
        return root;
    }

    /**
     * 重排链表
     * 给定一个单链表 L：L0→L1→…→Ln-1→Ln ，
     * 将其重新排列后变为： L0→Ln→L1→Ln-1→L2→Ln-2→…
     * <p>
     * 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
     */
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        //找中点
        ListNode pre;
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        //前面断开
        pre = slow;
        slow = slow.next;
        pre.next = null;
        //逆转后面链表
        pre = reverseLinkedList(slow);
        ListNode next = null;

        //结合
        slow = head;
        fast = pre;
        ListNode fnext;
        while (slow != null && fast != null) {
            next = slow.next;
            fnext = fast.next;
            slow.next = fast;
            fast.next = next;
            slow = next;
            fast = fnext;
        }
        return;
    }

    private ListNode reverseLinkedList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        ListNode next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    /**
     * 对链表进行插入排序
     * dummyNode
     */
    public ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummyNode = new ListNode(Integer.MIN_VALUE);
        dummyNode.next = head;
        ListNode pre = dummyNode;
        ListNode cur = head;
        while (cur != null) {
            if (cur.val < pre.val) {
                ListNode loc = dummyNode;
                while (loc.next != null && loc.next.val <= cur.val) {
                    loc = loc.next;
                }
                ListNode next = cur.next;
                pre.next = next;
                cur.next = loc.next;
                loc.next = cur;
                cur = pre.next;
            } else {
                cur = cur.next;
                pre = pre.next;
            }
        }
        return dummyNode.next;
    }

    /**
     * 排序链表
     * 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
     * 归并排序,注意dummyNode的位置
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        //找中点
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode right = slow.next;
        slow.next = null;
        ListNode left = sortList(head);
        right = sortList(right);
        return mergeII(left, right);
    }

    private ListNode mergeII(ListNode left, ListNode right) {
        ListNode dummyNode = new ListNode(Integer.MIN_VALUE);
        ListNode cur = dummyNode;
        while (left != null && right != null) {
            if (left.val > right.val) {
                cur.next = right;
                right = right.next;
            } else {
                cur.next = left;
                left = left.next;
            }
            cur = cur.next;
        }
        if (left != null) {
            cur.next = left;
        }
        if (right != null) {
            cur.next = right;
        }
        return dummyNode.next;
    }

    /**
     * 相交链表
     * 编写一个程序，找到两个单链表相交的起始节点。
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        //找终点
        ListNode lastB = headB;
        while (lastB.next != null) {
            lastB = lastB.next;
        }
        //成环
        lastB.next = headB;
        //快慢指针
        ListNode slowA = headA;
        ListNode fastA = headA;
        while (fastA.next != null && fastA.next.next != null) {
            slowA = slowA.next;
            fastA = fastA.next.next;
            if (slowA == fastA) {
                //找环入口
                slowA = headA;
                while (slowA != fastA) {
                    slowA = slowA.next;
                    fastA = fastA.next;
                }
                lastB.next = null;
                return slowA;
            }
        }
        //重置空
        lastB.next = null;
        return null;
    }

    /**
     * 移除链表元素
     * 删除链表中等于给定值 val 的所有节点。
     */
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        while (head != null) {
            if (head.val == val) {
                pre.next = head.next;
                head = pre.next;
            } else {
                pre = head;
                head = head.next;
            }
        }
        return dummy.next;
    }

    /**
     * 反转链表
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        //新的头结点
        ListNode pHead = reverseList(head.next);
        //逆转
        head.next.next = head;
        //此时为最后节点
        head.next = null;
        return pHead;
    }

    public ListNode reverseListII(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    /**
     * 回文链表
     * 请判断一个链表是否为回文链表。
     */
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        //找中点
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        //后半段头结点
        ListNode cur = slow.next;
        slow.next = null;
        ListNode pre = null;
        ListNode next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        //翻转后的头
        ListNode pHead = pre;
        //对比，不用考虑最后一个值
        while (pHead != null && head != null) {
            if (pHead.val != head.val) {
                return false;
            }
            pHead = pHead.next;
            head = head.next;
        }
        return true;
    }

    /**
     * 奇偶链表
     * 给定一个单链表，把所有的奇数节点和偶数节点分别排在一起。请注意，这里的奇数节点和偶数节点指的是节点编号的奇偶性，而不是节点的值的奇偶性。
     * 请尝试使用原地算法完成。你的算法的空间复杂度应为 O(1)，时间复杂度应为 O(nodes)，nodes 为节点总数。
     */
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        //双指针替换前进
        ListNode c1 = head;
        ListNode c2 = head.next;
        ListNode c2Head = c2;
        while (c1.next != null && c2.next != null) {
            c1.next = c2.next;
            c1 = c1.next;
            c2.next = c1.next;
            c2 = c2.next;
        }
        c1.next = c2Head;
        return head;
    }
}
