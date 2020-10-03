package org.hydev;

import java.io.Serializable;

// 最佳做法，单元素枚举. 但枚举无法继承类.
enum FinalElvis {
    INSTANCE;
}

// 第 3 条：用私有构造器或者枚举类型强化 Singleton 属性.
public class Item_3 {
    // Singleton 是指仅仅被实例化一次类，通常用来表示无状态的对象（如函数）
    // [?] 测试 Singleton 会非常困难，因为不能给它替换默认实现，除非实现一个充当其类型的接口.
    public static void main(String[] args) {
        Elvis elvis = Elvis.INSTANCE;
        Elvis anotherElvis = Elvis.getInstance();
    }
}

@SuppressWarnings("InstantiationOfUtilityClass")
class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    // 另一种方法，私有静态成员 + 公有静态工厂.
    private static final Elvis ANOTHER_INSTANCE = new Elvis();

    // 私有构造器，并导出公有的静态成员.
    private Elvis() {
        // 但可借助反射机制，调用私有构造器.
        // 修改构造器，让它在被要求创建第二个实例时抛出异常.
    }

    public static Elvis getInstance() {
        return ANOTHER_INSTANCE;
    }
}

class SerializableElvis implements Serializable {
    private static final SerializableElvis INSTANCE = new SerializableElvis();

    private SerializableElvis() {
    }

    // 但如果 Singleton 是可序列化的，必须将所有域变为瞬时（transient）的，且提供 readResolve() 方法.
    private Object readResolve() {
        return INSTANCE;
    }
}
