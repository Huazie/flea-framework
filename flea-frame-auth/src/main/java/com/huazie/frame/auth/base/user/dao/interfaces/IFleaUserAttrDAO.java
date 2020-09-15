package com.huazie.frame.auth.base.user.dao.interfaces;

import com.huazie.frame.auth.base.user.entity.FleaUserAttr;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

import java.util.List;

/**
 * <p> Flea用户属性DAO层接口 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaUserAttrDAO extends IAbstractFleaJPADAO<FleaUserAttr> {

    /**
     * <p> 根据用户编号获取用户扩展属性信息（属性状态 1 正常，未失效）</p>
     *
     * @param userId 用户编号
     * @return 用户扩展属性信息
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    List<FleaUserAttr> queryValidUserAttrs(Long userId) throws CommonException;
}