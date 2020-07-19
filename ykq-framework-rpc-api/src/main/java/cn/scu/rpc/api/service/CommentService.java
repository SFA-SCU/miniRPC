package cn.scu.rpc.api.service;

import cn.scu.rpc.api.model.Comment;

/**
 * Created by jiang on 2017/5/9.
 */
public interface CommentService {
    Comment getCommentByProductId(Long productId);
}
