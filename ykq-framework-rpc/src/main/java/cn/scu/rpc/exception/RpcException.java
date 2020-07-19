package cn.scu.rpc.exception;

/**
 * ClassName: RpcException
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 14:09
 * Version V1.0
 * Description:
 */

public class RpcException extends RuntimeException {

    public RpcException(String errorMsg){
        super(errorMsg);
    }
    public RpcException(Exception ex){
        super(ex);
    }
    public RpcException(){
        super("rpc exception");
    }
}

