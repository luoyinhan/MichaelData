package com.example.factory;

/**
 * 描述：工厂模式 应用
 * 作者：Micheal
 * 修改时间：2017/2/16
 */

public interface IFruit {
    void eat();
}

class Apple implements IFruit {
    @Override
    public void eat() {
        System.out.println("Apple");
    }
}

//构造工厂类 ------简单工厂
//也就是说以后如果我们在添加其他的实例的时候只需要修改工厂类就行了
//class IFactory {
//    static IFruit getInstance(String name) {
//        IFruit f = null;
//        if ("Apple".equals(name)) {
//            f = new Apple();
//        } else if ("Orange".equals(name)) {
//            f = new Orange();
//        }
//        return f;
//    }
//}

//通过反射机制实现 简单工厂模式
class Factory {
    static IFruit getInstance(String name) {
        IFruit f = null;
        try {
            f = (IFruit) Class.forName(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return f;
    }
}

//抽象工厂类
abstract class IFactory {
    abstract IFruit create();
}

//工厂方法
class MyFactory extends IFactory {
    @Override
    IFruit create() {
        return new Apple();
    }
}

class Hello {
    public static void main(String[] args) {
        //通过反射 拿到相应的类   完整的包和类名
        //简单工厂模式
        IFruit f = Factory.getInstance("com.example.factory.Orange");
        f.eat();
        /**
         * 工厂方法模式
         * 优点：使用开闭原则来分析下工厂方法模式。当有新的产品（即暴发户的汽车）产生时，只要按照抽象产品角色、抽象工厂角色提供的合同来生成，那么就可以被客户使用，
         * 而不必去修改任何已有的代码。（即当有新产品时，只要创建并基础抽象产品；新建具体工厂继承抽象工厂；而不用修改任何一个类）工厂方法模式是完全符合开闭原则的！
         * 缺点:当产品种类非常多时，就会出现大量的与之对应的工厂类，这不应该是我们所希望的
         * 可以结合 简单工厂 和 工厂模式来处理
         */
        IFactory factory = new MyFactory();
        IFruit f1 = factory.create();
        f1.eat();
    }
}