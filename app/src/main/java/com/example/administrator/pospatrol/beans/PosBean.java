package com.example.administrator.pospatrol.beans;

/**
 * 终端信息类 对应items.xml
 *
 * @author Administrator
 *
 */
public class PosBean {
    private String posNo;//终端编号
    private String posName;//终端名称

    public String getPosNo() {
        return posNo;
    }

    public void setPosNo(String posNo) {
        this.posNo = posNo;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    @Override
    public String toString() {
        return "PosBean [posNo=" + posNo + ", posName=" + posName + "]";
    }

}
