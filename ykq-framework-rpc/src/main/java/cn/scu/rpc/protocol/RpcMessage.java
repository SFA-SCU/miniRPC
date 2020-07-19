package cn.scu.rpc.protocol;

import java.io.Serializable;

/**
 * ClassName: RpcMessage
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 13:59
 * Version V1.0
 * Description:
 */

public class RpcMessage implements Serializable {


    private RpcMessageHeader messageHeader;

    private Object messageBody;

    public RpcMessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(RpcMessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Object getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Object messageBody) {
        this.messageBody = messageBody;
    }

}
