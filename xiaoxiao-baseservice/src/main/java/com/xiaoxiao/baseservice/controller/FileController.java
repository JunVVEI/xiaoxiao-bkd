package com.xiaoxiao.baseservice.controller;

import com.xiaoxiao.baseservice.model.dto.FileQuery;
import com.xiaoxiao.baseservice.service.FileService;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.common.util.AssertUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author caijiachun
 */
@RestController
@RequestMapping("/bs/file")
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("/policy")
    public CommonResp policy() {
        return CommonResp.success(fileService.policy());
    }

    @PostMapping("/isDeleteFile")
    public boolean isDeleteFile(String fileName) {
        AssertUtil.isTrue(fileService.isDeleteFile(fileName) == 1, "删除失败");
        return true;
    }

    @PostMapping("/deleteFile")
    public boolean deleteFile(String fileName) {
        AssertUtil.isTrue(fileService.deleteFile(fileName) == 1, "删除失败");
        return true;
    }

    @GetMapping("/selectFile")
    public CommonResp selectFile(FileQuery fileQuery) {
        return CommonResp.success(fileService.selectFile(fileQuery));
    }

}
