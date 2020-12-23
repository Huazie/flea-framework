package com.huazie.frame.jersey.client.request.impl;

import com.huazie.frame.common.slf4j.FleaLogger;
import com.huazie.frame.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.common.util.xml.JABXUtils;
import com.huazie.frame.jersey.client.request.RequestConfig;
import com.huazie.frame.jersey.client.request.RequestModeEnum;
import com.huazie.frame.jersey.common.FleaJerseyConstants;
import com.huazie.frame.jersey.common.FleaJerseyManager;
import com.huazie.frame.jersey.common.data.FleaJerseyRequest;
import com.huazie.frame.jersey.common.data.FleaJerseyResponse;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.client.WebTarget;

/**
 * <p> 文件GET请求 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FGetFleaRequest extends FleaRequest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FGetFleaRequest.class);

    /**
     * <p> 不带参数的构造方法 </p>
     *
     * @since 1.0.0
     */
    public FGetFleaRequest() {
    }

    /**
     * <p> 带参数的构造方法 </p>
     *
     * @param config 请求配置
     * @since 1.0.0
     */
    public FGetFleaRequest(RequestConfig config) {
        super(config);
    }

    @Override
    protected void init() {
        modeEnum = RequestModeEnum.FGET;
    }

    @Override
    protected FleaJerseyResponse request(WebTarget target, FleaJerseyRequest request) throws Exception {

        // 将请求报文转换成请求数据字符串
        String requestData = toRequestData(request);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(new Object() {}, "Start");
            LOGGER.debug("RequestData = {}", requestData);
        }

        FleaJerseyResponse response = null;

        // 文件下载GET请求发送
        FormDataMultiPart formDataMultiPart = target
                .path(FleaJerseyConstants.FileResourceConstants.FILE_DOWNLOAD_PATH)
                .queryParam(FleaJerseyConstants.FormDataConstants.FORM_DATA_KEY_REQUEST, requestData)
                .request(toMediaType())
                .get(FormDataMultiPart.class);

        // 将表单添加到文件上下文中
        FleaJerseyManager.getManager().getFileContext().setFormDataMultiPart(formDataMultiPart);

        // 获取响应表单数据
        FormDataBodyPart responseFormData = FleaJerseyManager.getManager().getFormDataBodyPart(FleaJerseyConstants.FormDataConstants.FORM_DATA_KEY_RESPONSE);
        String responseData = responseFormData.getValue();

        if (StringUtils.isNotBlank(requestData)) {
            response = JABXUtils.fromXml(responseData, FleaJerseyResponse.class);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaJerseyResponse = {}", response);
            LOGGER.debug("End");
        }

        return response;
    }
}
