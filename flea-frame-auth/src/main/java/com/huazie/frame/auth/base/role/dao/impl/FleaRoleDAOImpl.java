package com.huazie.frame.auth.base.role.dao.impl;

import com.huazie.frame.auth.base.FleaAuthDAOImpl;
import com.huazie.frame.auth.base.role.dao.interfaces.IFleaRoleDAO;
import com.huazie.frame.auth.base.role.entity.FleaRole;
import org.springframework.stereotype.Repository;

/**
 * <p> Flea角色DAO层实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaRoleDAO")
public class FleaRoleDAOImpl extends FleaAuthDAOImpl<FleaRole> implements IFleaRoleDAO {
}