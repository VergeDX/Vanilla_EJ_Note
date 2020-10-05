package org.hydev;

// 第 9 条：try - with - resources 优先于 try - finally.
public class Item_9 {
    // 根据经验，try - finally 语句是确保资源会被适时关闭的最佳方法，就算发生异常或者返回也一样.
    // 这样看起来好像也不算太坏，但是如果再添加第二个资源，就会一团糟了.

    // [V] 嵌套的 try - finally 块将会导致，外部异常覆盖内部异常.
    // [V] try - with - resources 会将异常全部记录下来，参见 Throwable#getSuppressed() 方法.
}
