package com.xiaoxiao.toolbag.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.toolbag.config.threadPool.ThreadPoolConfig;
import com.xiaoxiao.toolbag.mapper.EncyclopediaMapper;
import com.xiaoxiao.toolbag.model.dto.encyclopedia.EncyclopediaDTO;
import com.xiaoxiao.toolbag.model.dto.encyclopedia.EncyclopediaQuery;
import com.xiaoxiao.toolbag.model.entity.Encyclopedia;
import com.xiaoxiao.toolbag.model.vo.EncyclopediaVO;
import com.xiaoxiao.toolbag.service.EncyclopediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-01-26 12:20:39
 */
@Service
@Slf4j
public class EncyclopediaServiceImpl extends ServiceImpl<EncyclopediaMapper, Encyclopedia> implements EncyclopediaService {

    @Resource
    private EncyclopediaMapper encyclopediaMapper;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public IPage<EncyclopediaVO> listQuery(EncyclopediaQuery encyclopediaQuery) {
        EncyclopediaQuery.checkIsValid(encyclopediaQuery);
        log.info("{}", encyclopediaQuery);
        Page<Encyclopedia> encyclopediaPage = this.page(
                new Page<>(encyclopediaQuery.getCurrentPage(), encyclopediaQuery.getPageSize()),
                new QueryWrapper<Encyclopedia>()
                        .orderBy(
                                true,
                                !encyclopediaQuery.getIsAsc(),
                                encyclopediaQuery.getSortField()
                        )
                        .lambda()
                        .select(Encyclopedia::getId, Encyclopedia::getTitle)
                        .like(StrUtil.isNotBlank(encyclopediaQuery.getTitle()), Encyclopedia::getTitle, encyclopediaQuery.getTitle())
                        .like(StrUtil.isNotBlank(encyclopediaQuery.getContent()), Encyclopedia::getMdContent, encyclopediaQuery.getContent())
                        .eq(Encyclopedia::getIsDelete, XiaoXiaoConstEnum.UN_DELETE)
        );

        IPage<EncyclopediaVO> encyclopediaVOPage = encyclopediaPage.convert(encyclopedia -> {
            EncyclopediaVO encyclopediaVO = new EncyclopediaVO();
            encyclopediaVO.setId(encyclopedia.getId());
            encyclopediaVO.setTitle(encyclopedia.getTitle());
            return encyclopediaVO;
        });

        return encyclopediaVOPage;

    }

    @Override
    public EncyclopediaVO queryById(String id) {
        Encyclopedia encyclopedia = encyclopediaMapper.selectOne(
                new QueryWrapper<Encyclopedia>().lambda()
                        .eq(Encyclopedia::getId, id)
                        .eq(Encyclopedia::getIsDelete, XiaoXiaoConstEnum.UN_DELETE)
        );

        // 浏览量+1
        CompletableFuture.runAsync(
                () -> {
                    UpdateWrapper<Encyclopedia> updateWrapper = new UpdateWrapper<Encyclopedia>()
                            .eq("id", id)
                            .setSql("view_count = view_count + 1");
                    encyclopediaMapper.update(null, updateWrapper);
                },
                threadPoolTaskExecutor
        );
        return EncyclopediaVO.prepareEncyclopediaVO(encyclopedia);
    }

    @Override
    public EncyclopediaVO queryByDocKey(String docKey) {
        Encyclopedia encyclopedia = encyclopediaMapper.selectOne(
                new QueryWrapper<Encyclopedia>().lambda()
                        .eq(Encyclopedia::getDocKey, docKey)
                        .eq(Encyclopedia::getIsDelete, XiaoXiaoConstEnum.UN_DELETE)
        );

        // 浏览量+1
        CompletableFuture.runAsync(
                () -> {
                    UpdateWrapper<Encyclopedia> updateWrapper = new UpdateWrapper<Encyclopedia>()
                            .eq("id", encyclopedia.getId())
                            .setSql("view_count = view_count + 1");
                    encyclopediaMapper.update(null, updateWrapper);
                },
                threadPoolTaskExecutor
        );
        return EncyclopediaVO.prepareEncyclopediaVO(encyclopedia);
    }

    @Override
    public boolean saveEncyclopedia(EncyclopediaDTO encyclopediaDTO) {
        EncyclopediaDTO.checkIsValid(encyclopediaDTO);
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        Encyclopedia encyclopedia = EncyclopediaDTO.prepareEncyclopedia(encyclopediaDTO);
        encyclopedia.setCreator(String.valueOf(currentUser.getUid()));

        log.info("新增校园百科 encyclopediaDTO: {}, uid: {}", encyclopediaDTO, currentUser.getUid());
        return this.save(encyclopedia);
    }
}
