package org.hydev.chapter_2;

import java.lang.ref.Cleaner;

// 第 8 条：避免使用终结方法.
public class Item_8 {
    // 终结方法通常是不可预测的，也是很危险的，一般情况下是不必要的.
    // 清除方法没有终结方法那么危险，但仍然是不可预测、运行缓慢，一般情况下也是不必要的.

    // 注重时间的任务不应该由终结方法或者清除方法来完成.

    // 永远不应该依赖终结方法或者清除方法来更新重要的持久状态.

    // 使用终结方法和清除方法有一个非常严重的性能损失.

    // 终结方法有一个很严重的问题：它们为终结方法攻击打开了类的大门.
    // 终结方法攻击背后的思想很简单：如果从构造器或者它的序列化对等体抛出异常，
    // 恶意子类的终结方法就可以在构造了一部分的应该已经半途夭折的对象上运行.
    // 这个终结方法会将该对象的引用记录在一个静态域中，阻止它被垃圾回收.
    // 一旦记录到异常的对象，就可以轻松地在这个对象上调用任何原本永远不允许出现在这里的方法.

    // 从构造器抛出的异常，应该足以防止对象继续存在；有了终结方法的存在，这一点就做不到了.
    // 为了防止非 final 类受到终结方法攻击，要编写一个空的 final 的 finalize 方法.

    // [V] 在构造函数中抛出异常 -> 对象会被垃圾回收 -> 调用 finalize() 方法 -> 回收内存
    // [V] 若重载 finalize() 方法，在这个方法中将对象保存在静态域中，阻止垃圾清理.
    // [V] Java 6 之后，若创建 Object 对象前抛出异常，它的 finalize() 方法就不会被执行.
    // [V] https://blog.csdn.net/sumoyu/article/details/23909905

    // 如果类的对象中封装的资源确实确实需要终止，那么就应该让类实现 AutoCloseable.
    public static void main(String[] args) {
        // [V] Room 依赖 State，当 Room 被自动关闭时：
        // [V] Room#close() -> state#clean() -> state#run()
        try (Room myRoom = new Room(7)) {
            System.out.println("Goodbye! ");
        }

        // Cleaner 的规范指出："清除方法在 System.exit 期间的行为是与实现相关的. 不确保清除动作是否被会被调用. ".
        new Room(99);
        // System.gc();
    }
}

// [V] Room 引用了其内部资源 State.
class Room implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        State state = new State(numJunkPiles);

        // [V] 当 this 被垃圾回收时，执行 state 中的 run() 方法.
        this.cleanable = Cleaner.create().register(this, state);
    }

    @Override
    public void close() {
        // [V] 在 close 时释放内部资源 State.
        cleanable.clean();
    }

    // 关键是 State 实例没有引用它的 Room 实例. 如果它引用了，会造成循环，阻止 Room 实例被垃圾回收（以及防止被自动清除）.
    // 因此 State 必须是一个静态的嵌套类，因为非静态的嵌套类包含了对其外围实例的引用.
    // 同样地，也不建议使用 lambda，因为它们很容易捕捉到对外围对象的引用.
    private static class State implements Runnable {
        int numJunkPiles;

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        @Override
        // [V] 依赖 State 的 Room 被垃圾回收时，需要释放内部的 State，这里模拟了释放资源的过程.
        public void run() {
            System.out.println("Cleaning room... ");
            this.numJunkPiles = 0;
        }
    }
}
