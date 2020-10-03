package org.hydev;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// 第 1 条：用静态工厂方法代替构造器.
public class Item_1 {
    public static void main(String[] args) {
        // 静态工厂，返回的是 Boolean 中的引用.
        Boolean boxingBoolean = Boolean.valueOf("true");

        // 【优势】1. 它们有名称.
        BigInteger bigInteger = BigInteger.probablePrime(7, new Random());

        // 【优势】2. 调用时无需创建对象.
        // 在 Boolean.valueOf() 返回的是内部对象，提升性能.

        //【优势】3. 可以返回任意子类型的对象.
        // 实际上类型是 ImmutableCollections，但通过接口引用.
        List<?> aList = List.of();

        //【优势】4. 返回类型可以动态变化.
        // 在源码中，返回类型会根据元素数量动态变化.
        Set<AnEnum> stringSet = EnumSet.noneOf(AnEnum.class);

        //【优势】5. 返回对象所属的类，在编写方法时可以不存在.
        // https://www.informit.com/articles/article.aspx?p=1216151

        //【缺点】1. 内部类或许无法实例化.
        // 例如，在 EnumSet.noneOf() 中，无法实例化 RegularEnumSet 或 JumboEnumSet.

        //【缺点】2. 较难以被发现.
        // 一些惯用名称：from, if, valueOf, instance / getInstance, create / new Instance,
        // getType, newType, type.
    }

    enum AnEnum {}
}
