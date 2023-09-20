package com.xiaoxiao.user.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class UserEnergyPageVO {
    private IPage<UserEnergyVO> page;
    private Double sum;
}
