package com.huazie.frame.jersey.common.filter.config;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p> flea-jersey-filter.xml 配置 {@code <filterchain> </filterchain>} 节点 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FilterChain {

    private Before before;

    private Service service;

    private After after;

    private Error error;

    public Before getBefore() {
        return before;
    }

    public void setBefore(Before before) {
        this.before = before;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public After getAfter() {
        return after;
    }

    public void setAfter(After after) {
        this.after = after;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
