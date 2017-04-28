package com.example.factory;

/**
 * 描述：抽象工厂模式 实例学习
 * 作者：Micheal
 * 修改时间：2017/4/26
 */

public class BossAbstractFactory {
}


//抽象产品（Bmw和Audi同理）
abstract class BenzCar {
    private String name;

    public abstract void drive();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//具体产品（Bmw和Audi同理）
class BenzSportCar extends BenzCar {
    public void drive() {
        System.out.println(this.getName() + "----BenzSportCar-----------------------");
    }
}

class BenzBusinessCar extends BenzCar {
    public void drive() {
        System.out.println(this.getName() + "----BenzBusinessCar-----------------------");
    }
}

abstract class BmwCar {
    private String name;

    public abstract void drive();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class BmwSportCar extends BmwCar {
    public void drive() {
        System.out.println(this.getName() + "----BmwSportCar-----------------------");
    }
}

class BmwBusinessCar extends BmwCar {
    public void drive() {
        System.out.println(this.getName() + "----BmwBusinessCar-----------------------");
    }
}

abstract class AudiCar {
    private String name;

    public abstract void drive();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class AudiSportCar extends AudiCar {
    public void drive() {
        System.out.println(this.getName() + "----AudiSportCar-----------------------");
    }
}

class AudiBusinessCar extends AudiCar {
    public void drive() {
        System.out.println(this.getName() + "----AudiBusinessCar-----------------------");
    }
}

//抽象工厂
abstract class Driver3 {
    public abstract BenzCar createBenzCar(String car) throws Exception;

    public abstract BmwCar createBmwCar(String car) throws Exception;

    public abstract AudiCar createAudiCar(String car) throws Exception;
}

//具体工厂
class SportDriver extends Driver3 {
    public BenzCar createBenzCar(String car) throws Exception {
        return new BenzSportCar();
    }

    public BmwCar createBmwCar(String car) throws Exception {
        return new BmwSportCar();
    }

    public AudiCar createAudiCar(String car) throws Exception {
        return new AudiSportCar();
    }
}

class BusinessDriver extends Driver3 {
    public BenzCar createBenzCar(String car) throws Exception {
        return new BenzBusinessCar();
    }

    public BmwCar createBmwCar(String car) throws Exception {
        return new BmwBusinessCar();
    }

    public AudiCar createAudiCar(String car) throws Exception {
        return new AudiBusinessCar();
    }
}

class test {
    /**
     * 抽象工厂模式的用意为：给客户端提供一个接口，可以创建多个产品族中的产品对象。
     * 而且使用抽象工厂模式还要满足以下条件：
     * 1.系统中有多个产品族，而系统一次只可能消费其中一族产品
     * 2.同属于同一个产品族的产品以其使用。
     */
    public static void main(String[] args) throws Exception {
        Driver3 d = new BusinessDriver();
        AudiCar car = d.createAudiCar("");
        car.setName("A4");
        car.drive();
    }
}
