package com.xiaoxiao.user.rpc.model.response;

import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * q
 * </p>
 *
 * @author Junwei
 * @since 2023/2/24
 */
@Data
public class UserFollowingResponse {

    private List<RpcUserVO> rpcUserVOList;
}
