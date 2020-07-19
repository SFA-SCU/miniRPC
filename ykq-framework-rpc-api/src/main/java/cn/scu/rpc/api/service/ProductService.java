package cn.scu.rpc.api.service;

import cn.scu.rpc.api.model.Product;


public interface ProductService {
    Product getById(Long id);
}
