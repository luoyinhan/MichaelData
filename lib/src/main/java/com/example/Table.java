package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 描述：
 * 作者：Micheal
 * 修改时间：2017/3/2
 */
@Target(ElementType.TYPE)
public @interface Table {
    public String tablename() default "className";
}

