package cn.scu.rpc.client;

/**
 * Created by jiang on 2017/5/16.
 */
public interface ResponseCallback {

    void onSuccess(Object response);

    void onException(RuntimeException ex);
}
