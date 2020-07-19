package cn.scu.rpc.consumer.controller;

import cn.scu.rpc.api.model.Product;
import cn.scu.rpc.consumer.service.ProductCommentService;
import cn.scu.rpc.context.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

/**
 * Created by jiang on 2016/12/5.
 */
@RestController
@RequestMapping("/product")
public class ProductController{

    @Autowired
    private ProductCommentService productCommentService;


    @RequestMapping("/{productId}")
    public Product getById(@PathVariable final long productId) throws UnknownHostException {


        RpcContext.getContext().addContextParameter("rpc-version","1.0");
        Product result= this.productCommentService.getById(productId);

        return result;
    }


}
