package com.example;

import java.lang.reflect.Field;

/**
 * 描述：熟悉 java 反射原理 合并两个类的属性值
 * 作者：Micheal
 * 修改时间：2017/4/18
 */

public class CombineBeans {
    /**
     * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖tagetBean重点的值
     *
     * @param sourceBean 被提取的对象bean
     * @param targetBean 用于合并的对象bean
     * @return targetBean, 合并后的对象
     */
    private TestModel combineSydwCore(TestModel sourceBean, TestModel targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();//获取所有的属性
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                //这个 只能是 基本数据类型
                if (!(sourceField.get(sourceBean) == null) && !"serialVersionUID".equals(sourceField.getName().toString())) {
                    targetField.set(targetBean, sourceField.get(sourceBean));//动态设置属性值   覆盖
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }

    //测试 combineBeans方法
    public static void main(String[] args) {
        TestModel sourceModel = new TestModel();    //    第一个对象
        TestModel targetModel = new TestModel();    //    第二个model对象
        sourceModel.setProp1("prop1");
        sourceModel.setProp2("prop2");
        sourceModel.setObj("b");
        targetModel.setProp2("prop2");
        targetModel.setProp3("prop3");
        targetModel.setObj("a");
        CombineBeans test = new CombineBeans();
        test.combineSydwCore(sourceModel, targetModel);
        System.out.println(targetModel.toString());
    }
}
