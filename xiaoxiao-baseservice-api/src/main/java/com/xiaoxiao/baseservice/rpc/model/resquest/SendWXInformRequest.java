package com.xiaoxiao.baseservice.rpc.model.resquest;

import com.xiaoxiao.baseservice.rpc.model.WXInformEnum;
import lombok.Data;

@Data
public class SendWXInformRequest {
    private String openId;
    private String pagePath;
    private String first;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String remark;
    private WXInformEnum wxInformEnum;
}
