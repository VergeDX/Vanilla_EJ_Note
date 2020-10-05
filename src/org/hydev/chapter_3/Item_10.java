package org.hydev.chapter_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 第 10 条：覆盖 equals 时请遵守通用约定.
public class Item_10 {
    // 如果不覆盖 equals 方法，类的每个实例都只和它自身相等.
    // 如果满足了以下任意一个条件，这就正是所期望的结果：

    // 1. 类的每个实例本质上都是唯一的.
    // 对于代表活动实体而不是值的类来说确实如此，例如 Thread. Object 提供的 equals 实现对于这些类来说正是正确的行为.

    // 2. 类没有必要提供 "逻辑相等" 的测试功能.
    // 例如，java.util.regex.Pattern 可以覆盖 equals，以检查两个 Pattern 实例是否代表同一个正则表达式，
    // 但是设计设并不认为客户需要或者期望这样的动能. 在这类情况之下，从 Object 继承得到的 equals 实现已经足够了.

    // 3. 超类已经覆盖了 equals，超类的行为对于这个类也是合适的.
    // 例如，大多数的 Set 实现都从 AbstractSet 继承 equals 实现，
    // List 实现从 AbstractList 继承 equals 实现，Map 实现从 AbstractMap 继承 equals 实现.

    // 4. 类是私有的，或者是包级私有的，可以确定它的 equals 方法永远不被调用.
    // 如果你非常想要规避风险，可以覆盖 equals 方法，以确保它不会被意外调用.
    // @Override public boolean equals(Object o) { throw new AssertionError(); // Method is never called }

    // 若类具有自己特有的 "逻辑相等" 概念（不同于对象等同的概念），而且超类还没有覆盖 equals. 此时就应该考虑覆盖 equals 方法.
    // 这样做使得这个类的实例可以被用作映射表的键，或者集合中的元素，使映射或者集合表现出预期的行为.

    // 覆盖 equals 时，必须要遵守它的通用约定：
    // 1. 自反性：对于任何非 null 的引用值 x，x.equals(x) 必须返回 true.
    // 2. 对称性：对于任何非 null 的引用值 x 和 y，当且仅当 y.equals(x) 返回 true 时，x.equals(y) 必须返回 true.
    // 3. 传递性：对于任何非 null 的引用值 x、y 和 z，如果 x.equals(y) 返回 true，并且 y.equals(z) 也返回 true，那么 x.equals(z) 必须返回 true.
    // 4. 一致性：对于任何非 null 的引用值 x 和 y，只要 equals 的比较操作在对象中所用的信息没有被修改，多次调用 x.equals(y) 应当一致地返回 true 或 false.
    // 5. 对于任何非 null 的引用值 x，x.equals(null) 必须返回 false.

    // 有许多类，包括所有的集合类在内，都依赖于传递给它们的对象是否遵守了 equals 约定.

    // 自反性：对象必须等于其自身. 假如违反了这一条，然后把该类的实例添加到集合中，该集合的 contains 方法将果断地告诉你，该集合不包含你刚刚添加的实例.
    // 对称性：

    public static void main(String[] args) {
        CaseInsensitivityString cis = new CaseInsensitivityString("Polish");
        String s = "polish";

        List<CaseInsensitivityString> list = new ArrayList<>();
        list.add(cis);

        //noinspection SuspiciousMethodCalls
        System.out.println(list.contains(s));
    }
}

final class CaseInsensitivityString {
    private final String s;


    CaseInsensitivityString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CaseInsensitivityString)
            return s.equalsIgnoreCase(((CaseInsensitivityString) obj).s);

        if (obj instanceof String)
            return s.equalsIgnoreCase((String) obj);

        return false;
    }
}
