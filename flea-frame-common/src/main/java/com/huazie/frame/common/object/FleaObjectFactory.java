package com.huazie.frame.common.object;

/**
 * <p> Flea对象工厂类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface FleaObjectFactory<T> {

    /**
     * <p> 新建一个T对象，并将之包装进Flea对象中 </p>
     *
     * @return Flea对象
     * @since 1.0.0
     */
    FleaObject<T> newObject();

}
