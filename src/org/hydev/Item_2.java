package org.hydev;

// 第 2 条：遇到多个构造器参数时要考虑使用构建器.
public class Item_2 {
    @SuppressWarnings({"InnerClassMayBeStatic", "FieldCanBeLocal"})
    class NutritionFacts {
        private final int servingSize;
        private final int servings;
        private final int calories;
        private final int fat;
        private final int sodium;
        private final int carbohydrate;

        // 这里有一个全参构造器，但如何为参数指定默认值？
        NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
            this.servingSize = servingSize;
            this.servings = servings;
            this.calories = calories;
            this.fat = fat;
            this.sodium = sodium;
            this.carbohydrate = carbohydrate;
        }

        // 这样的确达到了目的，但有许多参数的时候，代码难以阅读.
        public NutritionFacts(int servingSize) {
            this(servingSize, 0, 0, 0, 0, 0);
        }
    }

    // 可以使用 JavaBean 模式，默认值 + Getter & Setter.
    @SuppressWarnings({"InnerClassMayBeStatic", "FieldCanBeLocal", "FieldMayBeFinal"})
    class AnotherNutritionFacts {
        private int servingSize = -1;
        private int servings = -1;
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        // 但构造该对象时，JavaBean 可能处于不一致的状态，而且不能把它做成不可变的.
        public void setServingSize(int servingSize) {
            this.servingSize = servingSize;
        }
    }
}

@SuppressWarnings("FieldCanBeLocal")
class FinalNutritionFacts {
    // 注意，这些域都是 final 的.
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    @SuppressWarnings("FieldMayBeFinal")
    public static class Builder {
        // 这些是必选参数.
        private int servingSize;
        private int servings;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        // 这些是可选参数.
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        // 这些是可选参数的 Builder，它们都返回 Builder 以组成流式 API.
        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder fat(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder sodium(int sodium) {
            this.sodium = sodium;
            return this;
        }

        public Builder carbohydrate(int carbohydrate) {
            this.carbohydrate = carbohydrate;
            return this;
        }

        // 最后，别忘了 build 方法；它传递自身参数给要构建的类.
        public FinalNutritionFacts build() {
            return new FinalNutritionFacts(this);
        }
    }

    public static void main(String[] args) {
        FinalNutritionFacts finalNutritionFacts = new Builder(240, 8)
                .calories(100).sodium(35).carbohydrate(27).build();
    }

    // 私有的构造器，通过 Builder 来构建自身.
    private FinalNutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }
}
