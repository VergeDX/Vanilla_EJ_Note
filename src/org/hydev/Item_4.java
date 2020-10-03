package org.hydev;

// 第 4 条：通过私有构造器强化不可实例化的能力.
public class Item_4 {
    public static void main(String[] args) {
        // 工具类不希望被实例化，因为实例化没有意义.
        // 但缺少显式构造器时，编译器将自动提供一个公有、无参的缺省构造器.
        class UtilsClass {
        }
        UtilsClass utilsClass = new UtilsClass();

        // 将类变为抽象类，来防止它实例化的做法，是行不通的.
        // 因为它可以被继承，并实例化子类.
        // 这样做反而会误导用户，以为它们是是专门为了继承而设计的类.

        // 因此，只要私有构造器，便可使类不能被实例化.
        class PrivateConstructor {
            private PrivateConstructor() {
                // 并且，在其中抛出异常，防止在内部不小心实例化.
                throw new AssertionError("工具类不应该被实例化. ");
            }
        }
    }
}
