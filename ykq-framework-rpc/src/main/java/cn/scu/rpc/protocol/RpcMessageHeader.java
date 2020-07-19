package cn.scu.rpc.protocol;

import java.io.Serializable;

/**
 * Created by jim on 2017/9/24.
 */
public class RpcMessageHeader implements Serializable {
    private int length;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
