package com.xiaoxiao.user.rpc.model.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.user.rpc.model.vo.RpcFollowUserVO;
import lombok.Data;

/**
 * @author zjw
 */
@Data
public class UserSearchResponse {
    private Page<RpcFollowUserVO> rpcFollowUserVOPage;
}
