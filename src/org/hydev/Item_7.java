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

    // 
}
