package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.common.utils.JsonUtils;
import cn.bjzfgcjs.idefense.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 上传文件处理
 * <p>Title: FileController</p>
 * <p>Description: </p>
 */
@RestController
@RequestMapping("/idefense")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/file/upload")
    @ResponseBody
    public String pictureUpload(MultipartFile uploadFile) {
        Map result = fileService.uploadPicture(uploadFile);
        //为了保证功能的兼容性，需要把Result转换成json格式的字符串。
        String json = JsonUtils.objectToJson(result);
        return json;
    }
}

