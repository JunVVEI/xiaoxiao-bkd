package com.xiaoxiao.user.rpc.model.response;

import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;
import lombok.Data;

import java.util.List;


@Data
public class UserFollowerResponse {

    private List<RpcUserVO> rpcUserVOList;
}
