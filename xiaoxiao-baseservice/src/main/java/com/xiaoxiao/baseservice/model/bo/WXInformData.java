package com.xiaoxiao.baseservice.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class WXInformData implements Serializable {
   // private String first;
    private WXInformKeyword keyword1;
    private WXInformKeyword keyword2;
    private WXInformKeyword keyword3;
   // private String remark;

    public WXInformData() {
       // this.first = new WXInformKeyword();
        this.keyword1 = new WXInformKeyword();
        this.keyword2 = new WXInformKeyword();
        this.keyword3 = new WXInformKeyword();
        //this.remark = new WXInformKeyword();
    }

//    public void setFirst(String value) {
//        this.first=value;
//    }

    public void setKeyword1(String value) {
        this.keyword1.setValue(value);
    }

    public void setKeyword2(String value) {
        this.keyword2.setValue(value);
    }

    public void setKeyword3(String value) {
        this.keyword3.setValue(value);
    }

//    public void setRemark(String value) {
//        this.remark=value;
//    }

}
