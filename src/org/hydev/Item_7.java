package org.hydev;

import java.util.Arrays;
import java.util.EmptyStackException;

// 第 7 条：消除过期的对象引用.
public class Item_7 {
    // 相比 C++，尽管 Java 中具有垃圾回收功能，但也可能造成内存泄露.
    @SuppressWarnings("InnerClassMayBeStatic")
    class Stack {
        private static final int DEFAULT_INITIAL_SIZE = 16;
        private Object[] elements;
        private int size = 0;

        public Stack() {
            elements = new Object[DEFAULT_INITIAL_SIZE];
        }

        public void push(Object e) {
            ensureCapacity();
            elements[size++] = e;
        }

        public Object pop() {
            if (size == 0) throw new EmptyStackException();
            return elements[--size];
        }

        // 及时增长栈容量，确保新元素的存放.
        private void ensureCapacity() {
            if (elements.length == size)
                elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    // 上述程序中，并没有很明显的错误，但它隐藏着一个问题；不严格地讲，这段程序有一个 "内存泄露".
    // 如果一个栈先是增长，然后再收缩；那么从栈中弹出的元素，不会被当做垃圾回收，因为栈内部维护者对这些对象的 "过期引用".

    // [V] 栈中 Object 数组只会增大，并不会 "缩小"；在栈内部仍能被访问到，这阻止了垃圾回收.

    // 这类问题的修复方法很简单：一旦对象引用已经过期，只需清空这些引用即可.
    // Object result = elements[--size];
    // elements[size] = null;

    // [V] Vector#remove 方法中，手动清空了过期的引用.
    // [V] 原注释：Let gc do its work.

    // 清空对象引用应该是一种例外，而不是一种规范行为.
    // 只要是类自己管理内存，程序员就应该警惕内存泄露问题.

    // 内存泄露的另一个常见来源是缓存.
    // 只要在缓存之外存在对某个项的键的引用，该项就有意义，那么就可以用 WeakHashMap 代表缓存；
    // 当缓存中的项过期之后，它们就自动被删除.
    // 记住只有当所要的缓存的生命周期是由该键的外部引用而不是由值决定时，WeakHashMap 才有用处.

    // [V] An entry in a WeakHashMap will automatically be removed when its key is no longer in ordinary use.

    // 对于复杂的缓存，必须直接使用 java.lang.ref.
    // [V] https://developer.ibm.com/zh/technologies/java/articles/j-lo-langref/

    // 内存泄露的第三个常见来源是监听器和其他回调.
    // 如果你实现了一个 API，客户端在这个 API 中注册回调，却没有显式地取消注册，
    // 那么除非你采取某些动作，否则它们就会不断地堆积起来.

    // [V] 这是由于，Callback 中要保留调用者的引用；当调用者不再被需要时，该引用会防止垃圾回收.

    // 确保回调立刻被当做垃圾回收的最佳方法是只保存它们的弱引用，例如，只将它们保存成 WeakHashMap 中的键.
}
