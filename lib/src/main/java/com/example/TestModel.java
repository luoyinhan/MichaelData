package com.example;

/**
 * 描述：
 * 作者：Micheal
 * 修改时间：2017/4/18
 */

public class TestModel {
    private String prop1;
    private String prop2;
    private String prop3;
    private Object obj;


    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }

    public String getProp3() {
        return prop3;
    }

    public void setProp3(String prop3) {
        this.prop3 = prop3;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    //用于输出字符串
    @Override
    public String toString() {
        return "prop1:" + prop1 + "\nprop2:" + prop2 + "\nprop3:" + prop3 + "\nobj:" + obj.toString();
    }
}
