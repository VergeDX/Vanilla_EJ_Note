package org.hydev.chapter_2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

// 第 6 条：避免创建不必要的对象.
public class Item_6 {
    // 一般来说，最好能重用单个对象.
    // 如果对象是不可变的，它就始终可以被重用.

    // 为了提升性能，应当显示地将正则表达式编译成一个 Pattern 实例（不可变）.
    // 让它成为类初始化的一部分，并将它缓存起来.
    private static final Pattern SOME_REGEX = Pattern.compile("some regex");

    // "bikini" 本身就是一个 String 实例.
    String s = new String("bikini");

    // 对于既有构造器，又有工厂方法的不可变类，通常优先使用工厂方法.
    Boolean factoryBoolean = Boolean.valueOf("true");

    // Boolean 的构造器在 Java 9 中被废弃.
    @SuppressWarnings("BooleanConstructorCall")
    Boolean constructorBoolean = new Boolean("true");

    // 对于较为昂贵的对象，最好缓存下来重用.
    // String.matches 不适合在注重性能的情形中反复使用.
    // 它在内部为正则表达式创建了一个 Pattern 实例，却只用了一次，之后就可以进行垃圾回收了.
    boolean matches = "some string".matches("some regex");

    public static void main(String[] args) {
        // 如果对象是不可变的，那么它显然能够被安全地重用.
        // 例如，对于一个给定的 Map 对象，每次调用 keySet() 都返回同样的 Set 实例.
        Map<String, String> stringMap = new HashMap<>();
        Set<String> keySet = stringMap.keySet();
    }

    // 另一种创建多余对象的方法，称作 "自动装箱"（autoboxing）.
    // 它允许程序员混用 "基本类型" 和 "装箱基本类型"，按需要自动装箱和拆箱.
    // 自动装箱使得 "基本类型" 和 "装箱基本类型" 之前的差别变得模糊起来，但是并没有完全消除.
    private static long sum() {
        // 变量 sum 被声明成 Long 而不是 long，意味着程序构造了大约 2 ^ 31 个多余的 Long 实例.
        // noinspection WrapperTypeMayBePrimitive
        Long sum = 0L;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }

        return sum;
    }

    // 要优先使用基本类型，而不是装箱基本类型；要担心无意识的自动装箱.
    // 通过维护自己的对象池，来避免创建对象并非是好的做法，除非池中的对象是非常重量级的.
    // 正确使用对象池的典型实例就是 "数据库连接池".
}
