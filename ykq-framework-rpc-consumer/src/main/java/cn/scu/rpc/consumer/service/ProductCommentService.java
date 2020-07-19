package cn.scu.rpc.consumer.service;

import cn.scu.rpc.api.model.Product;

/**
 * Created by jiang on 2017/5/10.
 */
public interface ProductCommentService {
    Product getById(Long productId);
}
