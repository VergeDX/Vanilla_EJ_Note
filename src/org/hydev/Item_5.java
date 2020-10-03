package org.hydev;

import java.util.Dictionary;

// 第 5 条：优先考虑依赖注入来引用资源.
public class Item_5 {
    // "拼写检查器" 依赖 "词典"，所以应有一种方式，将依赖传递进去.
    @SuppressWarnings("InnerClassMayBeStatic")
    public class SpellChecker {
        @SuppressWarnings("FieldCanBeLocal")
        private final Dictionary<String, Integer> dictionary;

        // 在构造器中传入依赖.
        public SpellChecker(Dictionary<String, Integer> dictionary) {
            this.dictionary = dictionary;
        }
    }

    // 另一种变体是，把资源工厂传递给构造器.
    // Java 8 增加的接口 Supplier 适合表示工厂.
    // Mosaic create(Supplier<? extends Tile> tileFactory) { ... }

    // 依赖注入往往会使大型项目凌乱不堪，此时可借助依赖注入框架.
    // Dagger: https://developer.android.google.cn/training/dependency-injection/dagger-android
    // Guice: https://github.com/google/guice
    // Spring: https://spring.io/
}
