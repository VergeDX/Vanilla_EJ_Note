package org.hydev.chapter_3;

// 第 12 条：始终要覆盖 toString.
public class Item_12 {
    // 虽然 Object 提供了 toString 方法的一个实现，但它返回的字符串通常并不是类的用户所期望看到的.
    // 它包含类的名称，以及一个 "@" 符号，接着是散列码的无符号十六进制表示法，例如 PhoneNumber@163b91.

    // toString 约定进一步指出,"建议所有的子类都覆盖这个方法". 这是一个很好的建议，真的！

    // 遵守 toString 约定并不像遵守 equals 和 hashCode 的约定（见第 10 条和第 11 条）那么重要，
    // 但是，提供好的 toString 实现可以使类用起来更加舒适，使用了这个类的系统也更易于调试.
    // 当对象被传递给 println、printf、字符串联操作符（+）以及 asset，或者被调试器打印出来时，toString 方法会被自动调用.

    // 提供好的 toString 方法，不仅有益于这个类的实例，同样也有益于那些包含这些实例的引用的对象，特别是集合对象.
    // 打印 Map 时会看到消息 { Jenny = PhoneNumber@163b91 } 或 { Jenny = 707-867-5309 }，你更愿意看到哪一个？

    // 在实际应用中，toString 方法应该返回对象中包含的所有值得关注的信息，例如上述电话号码例子的那样.
    // 如果对象太大，或者对象中包含的状态信息难以用字符串来表达，这样做就有点不切实际.
    // 在这种情况下，toString 应该返回一个摘要信息，例如 "Manhattan residential phone directory (1487536 listings)" 或者 "Thread [main, 5, main]".

    // 如果对象的字符串没有包含对象的所有必要信息，测试失败时的得到的报告将会像下面这样：
    // Assertion failure: exceeded {abc, 123}, but was {abc, 123}.

    // [V] 在实现 toString 的时候，可以指定它的格式；同时并提供一个静态工厂方法，以便可以解析回对象. 但若指定了格式，它就会变为 API 的一部分.
    // Java 平台类库中许多值类都采用了这种做法，包括 BitInteger、BigDecimal 和绝大多数的基本类型包装类.

    // 无论是否决定指定格式，都应该在文档中明确地表明你的意图.
    // 无论是否指定格式，都为 toString 返回值中包含的所有信息提供一种可以通过编程访问之的途径.
}
