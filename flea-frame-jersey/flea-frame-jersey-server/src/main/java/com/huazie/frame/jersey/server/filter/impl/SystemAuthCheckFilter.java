package com.huazie.frame.jersey.server.filter.impl;

import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.common.slf4j.FleaLogger;
import com.huazie.frame.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.frame.jersey.common.data.FleaJerseyRequest;
import com.huazie.frame.jersey.common.data.FleaJerseyResponse;
import com.huazie.frame.jersey.server.filter.IFleaJerseyFilter;

/**
 * <p> 系统授权校验过滤器 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class SystemAuthCheckFilter implements IFleaJerseyFilter {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(SystemAuthCheckFilter.class);

    @Override
    public void doFilter(FleaJerseyRequest request, FleaJerseyResponse response) throws CommonException {
        Object obj = null;
        if (LOGGER.isDebugEnabled()) {
            obj = new Object() {};
            LOGGER.debug1(obj, "System Auth Check, Start");
        }
        // 系统授权校验

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(obj,"System Auth Check, End");
        }
    }

}
