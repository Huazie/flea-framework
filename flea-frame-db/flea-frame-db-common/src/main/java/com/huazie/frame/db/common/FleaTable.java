package com.huazie.frame.db.common;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p> 自定义实体类注解，用于定义表名 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface FleaTable {
    /**
     * <p> 定义实体对应数据库表名 </p>
     *
     * @return 表名
     * @since 1.0.0
     */
    String name() default "";
}
