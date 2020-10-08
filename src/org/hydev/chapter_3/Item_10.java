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
    // 现在我们按照顺序逐一查看以下 5 个要求：

    // 1. 自反性：对象必须等于其自身. 假如违反了这一条，然后把该类的实例添加到集合中，该集合的 contains 方法将果断地告诉你，该集合不包含你刚刚添加的实例.
    // 2. 对称性：任何两个对象对于 "它们是否相等" 的问题都必须保持一致. 若无意中违反这一条，这种情形倒是不难想象.
    // 例如下面的类，它实现了一个区分大小写的字符串.
    public static final class CaseInsensitivityString {
        private final String s;

        CaseInsensitivityString(String s) {
            this.s = Objects.requireNonNull(s);
        }

        public static void main(String[] args) {
            // 假设我们有一个不区分大小写的字符串和一个普通的字符串：
            CaseInsensitivityString cis = new CaseInsensitivityString("Polish");
            String s = "polish";

            // 不出所料，cis.equals(s) 返回 true.
            //noinspection EqualsBetweenInconvertibleTypes
            System.out.println(cis.equals(s));

            // 问题在于，虽然 CaseInsensitivityString 类中的 equals 方法知道普通字符串对象，
            // 但是，String 类中的 equals 方法却不知道不区分大小写的字符串. 因此，s.equals(cis) 返回 false，显然违反了对称性.

            // 假设你把不区分大小写的字符串对象放到一个集合中：
            //noinspection MismatchedQueryAndUpdateOfCollection
            List<CaseInsensitivityString> list = new ArrayList<>();
            list.add(cis);

            // 此时的 list.contains(s) 会返回什么结果呢？没人知道. 在当前的 OpenJDK 实现中，它碰巧发挥 false，
            // 但这只是这个特定实现得出的结果而已. 在其它的实现中，它有可能返回 true，或者抛出一个运行时异常.
            // 一旦违反了 equals 约定，当其它对象面对你的对象时，你完全不知道这些对象的行为会怎么样.

            // 为了解决这个问题，只需把企图与 String 互操作的这段代码从 equals 方法中去掉就可以了.
            // 这样做之后，就可以重构该方法，使它变成一条单独的返回语句：
            // @Override public boolean equals(Object o) { return o instanceof CaseInsensitivityString &&
            // ((CaseInsensitivityString) o).s.equalsIgnoreCase(s); }
        }

        @Override
        // 在这个类中，equals 方法的意图非常好，它企图与普通的字符串对象进行互操作.
        public boolean equals(Object obj) {
            if (obj instanceof CaseInsensitivityString)
                return s.equalsIgnoreCase(((CaseInsensitivityString) obj).s);

            if (obj instanceof String)
                return s.equalsIgnoreCase((String) obj);

            return false;
        }
    }

    // 3. 传递性：如果一个对象等于第二个对象，而第二个对象又等于第三个对象，则第一个对象一定等于第三个对象.
    // 我们无法在扩展可实例化的类的同时，既增加新的值组件，同时又保留 equals 约定，除非愿意放弃面向对象的抽象所带来的优势.

    // [V] 设类 A 有一个字段 a，类 B 继承 A 并加入了字段 b，此时就不能保证 equals 约定.
    // [V] 因为假设 A 与 B 比较，我们只能比较 a 字段；而 B 与 B 比较时却要比较 a 和 b 字段；（或者采用其它比较规则，但将牺牲别的属性，如对称性）.
    // [V] 假设有 a1 是类 A 的实例，b1 和 b2 是类 B 的实例，a1、b1 和 b2 中的 a 字段都相同，故 b1.equals(a1) 和 a1.equals(b2) 都应该返回 true.
    // [V] 但若将 b1 与 b2 相比较，就需比较 a 字段与 b 字段，若 b1、b2 的 b 字段不同，则 b1.equals(b2) 将返回 false，这不满足传递性.

    // 你可能听说过，在 equals 中用 getClass 测试代替 instanceof 测试，可以扩展可实例化的类和增加新的值组件，同时保留 equals 约定：（代码略）.
    // [V] 但这样会导致，只有当对象具有相同的实现类时，才能使对象等同. 虽然这样也不算太糟糕，但结果却是无法接受的：
    // Point 的子类的实例仍然是一个 Point，它仍然需要发挥作用，但是如果采用了这种方法，它就无法完成任务！

    // [V] "可实例化的类" 中文版误翻为 "不可实例化的类"，原文 "instantiable class"，P44.
    // 虽然没有一种令人满意的办法既可以既扩展可实例化的类，又增加值组件，但还是有一种不错的权宜之计：
    // [V] 遵从第 18 条的 "复合优先于继承" 的建议. 我们不再使用继承，而是在 B 中加入一个私有的 A 域，以及一个公有的视图方法，此方法返回一个与 B 中 a 字段相同的 A 对象.

    // 在 Java 平台类库中，有一些类扩展了可实例化的类，并添加了新的值组件.
    // 例如，java.sql.Timestamp 对 java.util.Date 进行了扩展，并增加了 nanoseconds 域.
    // Timestamp 的 equals 实现确实违反了对称性，如果 Timestamp 和 Date 对象用于同一个集合中，或这以其他方式被混合在一起，则会引起不正确的行为.
    // Timestamp 类有一个免责声明，告诫程序员不要混合使用 Date 和 Timestamp 对象.
    // 只要你不把它们混合在一起，就不会有麻烦，除此之外没有其他的措施可以防止你这么做，而且结果导致的错误将很难调试.
    // Timestamp 类的这种行为是个错误，不值得仿效.

    // 注意，你可以在一个抽象类的子类中增加新的值组件且不违反 equals 约定. 只要不可能直接创建超类的实例，前面所述的种种问题就都不会发生.

    // 4. 一致性：如果两个对象相等，它们就必须始终保持相等，除非它们中有一个对象（或者两个都）被修改了.
    // 换句话说，可变对象在不同的时候可以与不同的对象相等，而不可变对象则不会这样. 当你在写一个类的时候，应该仔细考虑它是否应该是不可变的（详见第 17 条）.
    // 如果认为它应该是不可变的，就必须保证 equals 方法满足这样的限制条件：相等的对象永远相等，不相等的对象永远不相等.
    // 无论类是否是不可变的，都不要使 equals 方法依赖于不可靠的资源. 如果违反了这条禁令，想要满足一致性的要求就十分困难了.

    // 例如，java.net.URL 的 equals 方法依赖于对 URL 中主机 IP 地址的比较.
    // 将一个主机名转换为 IP 地址可能需要访问网络，随着时间的推移，就不能确保会产生相同的结果，即有可能 IP 地址发生了改变.
    // 这样会导致 URL equals 方法违反 equals 约定，在实践中有可能引发一些问题. URL equals 方法的行为是一个大错误且不应被模仿.

    // [V] URL equals 方法中的注释：
    // * Note: The defined behavior for equals is known to be inconsistent with virtual hosting in HTTP.

    // 5. 非空性：最后一个要求没有正式名称，我姑且称它为 "非空性"，意思是所有的对象都不能等于 null.
    // 尽管很难想象在什么情况下 o.equals(null) 的调用会意外地返回 true，但是意外抛出 NullPointerException 异常的情形却不难想象.
    // 通用约定不允许抛出 NullPointerException 异常. 许多类的 equals 方法都通过一个显式的 null 测试来防止这种情况：（代码略）.
    // [V] 在 equals 里，需要将 Object 参数转换为特定的类型；而在转换之前，需要使用 instanceof 进行判断.
    // [V] 根据 JLS 15.20.2，如果 instanceof 的第一个操作数为 null，那么，不管第二个操作数是哪种类型，instanceof 操作符都指定应该返回 false.
    // 因此，如果把 null 传给 equals 方法，类型检查就会返回 null，所以不需要显式的 null 检查.

    // 综合这些所有要求，得出了以下实现高质量 equals 方法的诀窍：
    // 1. 使用 == 操作符检查 "参数是否为这个对象的引用". 如果是，则返回 true. 这只不过是一种性能优化，如果比较操作有可能很昂贵，就值得这么做.
    // 2. 使用 instanceof 操作符检查 "参数是否为正确的类型". 如果不是，则返回 false.
    // 一般说来，所谓 "正确的类型" 是指 equals 方法所在的那个类. 某些情况下，是指该类所实现的某个接口.
    // 如果类实现的接口改进了 equals 约定，允许在实现了该接口的类之间进行比较，那么就使用接口. 集合接口如 Set、List、Map 和 Map.Entry 具有这样的特性.

    // 3. 把参数转换成正确的类型. 因为转换之前进行过 instanceof 测试，所以确保会成功.
    // 4. 对于该类中的每个 "关键" 域，检查参数中的域是否与该对象中对应的域相匹配.
    // 对于既不是 float 也不是 double 类型的基本类型域，可以使用 == 操作符进行比较；对于对象引用域，可以递归地调用 equals 方法；
    // 对于 float 域，可以使用 Float.compare(float, float) 方法；对于 double 域，则使用 Double.compare(double, double).
    // 对 float 和 double 域进行特殊的处理是有必要的，因为存在着 Float.NaN、-0.0f 以及类似的 double 常量.
    // 虽然可以用静态方法 Float.equals 和 Double.equals 对 float 和 double 域进行比较，但是每次比较都要进行自动装箱，这会导致性能下降.
    // 对于数组域，则要把以上的这些指导原则应用到每一个元素上. 如果数组域中的每个元素都很重要，就可以使用一个 Arrays.equals 方法.

    // 有些对象引用域包含 null 可能是合法的，所以，为了避免可能导致 NullPointerException 异常，
    // 则使用静态方法 Objects.equals(Object, Object) 来检查这类域的等同性.

    // 对于有些类，比如前面提到的 CaseInsensitivityString 类，域的比较要比简单的等同性测试复杂得多.
    // 如果是这种情况，可能希望保存该域的一个 "范式"，这样 equals 方法就可以根据这些范式进行低开销的精确比较，而不是高开销的非精确比较.
    // 这种方法对于不可变类（详见第 17 条）是最为合适的；如果对象可能发生变化，就必须使其范式保持最新.
    // [V] https://stackoverflow.com/questions/33992000/canonical-form-of-field
    // [V] CaseInsensitivityString 中应保存一个全大写 / 全小写的 "规范形式"，降低后续比较中的开销.

    // 域的比较顺序可能会影响 equals 方法的性能. 为了获得最佳的性能，应该最先比较最有可能不一致的域，或者是开销最低的域，最理想的情况是两个条件同时满足的域.
    // 不应该比较那些不属于对象逻辑状态的域，例如用于同步操作的 Lock 域. 也不需要比较衍生域，因为这些域可以由 "关键域" 计算获得，但是这样做有可能提高 equals 方法的性能.
    // 如果衍生域代表了整个对象的综合描述，比较这个域可以节省在比较失败时去比较实际数据所需要的开销.
    // 例如，假设有一个 Polygon 类，并缓存了该面积. 如果两个多边形有着不同的面积，就没有必要去比较它们的边和顶点.

    // 在编写完 equals 方法之后，应该问自己三个问题：它是否是对称的、传递的、一致的？并且不要只是自问，还要编写单元测试来检验这些特性.
    // 根据上面的诀窍构建 equals 方法的具体例子. 请看下面这个简单的 PhoneNumber 类：
    public static final class PhoneNumber {
        private final short areaCode, prefix, lineNum;

        public PhoneNumber(short areaCode, short prefix, short lineNum) {
            this.areaCode = rangeCheck(areaCode, 999, "area code");
            this.prefix = rangeCheck(prefix, 999, "prefix");
            this.lineNum = rangeCheck(lineNum, 9999, "line num");
        }

        private static short rangeCheck(int val, int max, String arg) {
            if (val < 0 || val > max)
                throw new IllegalArgumentException(arg + ":" + val);
            return (short) val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof PhoneNumber)) return false;

            PhoneNumber pn = (PhoneNumber) obj;
            return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
        }
    }

    // 下面是最后的一些告诫：
    // 1. 覆盖 equals 时总要覆盖 hashCode（详见第 11 条）.
    // 2. 不要企图让 equals 方法过于智能.
    // 如果只是简单地测试域中的值是否相等，则不难做到遵守 equals 约定.
    // 如果想过度地去追求各种等价关系，则很容易陷入麻烦之中. 把任何一种别名形式考虑到等价的范围内，往往不会是个好主意.
    // 例如，File 类不应该试图把指向同一个文件的符号链接当做相等的对象来看待. 所幸 File 类没有这样做.

    // 3. 不要讲 equals 声明中的 Object 对象替换为其他的类型.
    // 程序员编写出下面这样的 equals 方法并不鲜见，这会使程序员画上数个小时都搞不清为什么它不能正常工作：
    // public boolean equals(MyClass o) { ... }
    // 问题在于，这个方法并没有覆盖 Object.equals，因为它的参数应该是 Object 类型，相反，它重载了 Object.equals.

    // 编写和测试 equals（及 hashCode）方法都是十分繁琐的，得到的代码也很琐碎. 代替手工编写和测试这些方法的最佳途径，是使用 Google 开源的 AutoValue 框架，
    // 它会自动替你生成这些方法，通过类中的单个注解就能触发. 在大多数情况下，AutoValue 生成的方法本质上与你亲自编写的方法是一样的.

    // IDE 也有工具可以自动生成 equals 和 hashCode 方法，但得到的源代码比使用 AutoValue 的更加冗长，可读性也更差，它无法自动追踪类中的变化，因此需要进行测试.
    // 也就是说，让 IDE 生成 equals（及 hashCode）方法，通常优先于手工实现它们，因为 IDE 不会犯粗心的错误，但是程序员会犯错.

    // [V] IDEA (2020.2.3) 生成 equals 和 hashCode 方法后，再向类中添加字段，IDEA 不会提示说需要重新生成.
}
