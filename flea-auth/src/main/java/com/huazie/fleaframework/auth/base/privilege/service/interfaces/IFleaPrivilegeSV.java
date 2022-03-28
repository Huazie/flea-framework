package com.huazie.fleaframework.auth.base.privilege.service.interfaces;

import com.huazie.fleaframework.auth.base.privilege.entity.FleaPrivilege;
import com.huazie.fleaframework.auth.common.pojo.privilege.FleaPrivilegePOJO;
import com.huazie.fleaframework.common.exception.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

/**
 * Flea权限SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaPrivilegeSV extends IAbstractFleaJPASV<FleaPrivilege> {

    /**
     * 保存Flea权限
     *
     * @param fleaPrivilegePOJO Flea权限POJO类对象
     * @return Flea权限实体对象
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    FleaPrivilege savePrivilege(FleaPrivilegePOJO fleaPrivilegePOJO) throws CommonException;
}