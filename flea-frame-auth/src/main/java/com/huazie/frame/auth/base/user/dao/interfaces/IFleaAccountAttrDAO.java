package com.huazie.frame.auth.base.user.dao.interfaces;

import com.huazie.frame.auth.base.user.entity.FleaAccountAttr;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

import java.util.List;

/**
 * <p> Flea账户属性DAO层接口 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaAccountAttrDAO extends IAbstractFleaJPADAO<FleaAccountAttr> {

    /**
     * <p> 根据账户编号获取账户扩展属性信息（属性状态 1 正常，未失效）</p>
     *
     * @param accountId 账户编号
     * @return 账户扩展属性信息
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    List<FleaAccountAttr> queryValidAccountAttrs(Long accountId) throws CommonException;
}