package cn.bjzfgcjs.idefense.service;

import cn.bjzfgcjs.idefense.common.utils.FtpUtil;
import cn.bjzfgcjs.idefense.common.utils.IDUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.joda.time.DateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传服务
 * <p>Title: PictureServiceImpl</p>
 * <p>Description: </p>
 */
@Service
public class FileService {

    private String FTP_ADDRESS="192.168.226.128";

    private Integer FTP_PORT=21;

    private String FTP_USERNAME="ftpuser";

    private String FTP_PASSWORD="ftpuser";

    private String FTP_BASE_PATH="/home/ftpuser/www/file";

    private String FILE_BASE_URL="/2018/03/14";


    public Map uploadPicture(MultipartFile uploadFile) {
        Map resultMap = new HashMap<>();
        try {
            //生成一个新的文件名
            //取原始文件名
            String oldName = uploadFile.getOriginalFilename();
            //生成新文件
            String newName = IDUtils.genImageName();
            newName = newName + oldName.substring(oldName.lastIndexOf("."));
            //文件上传
            String filePath = new DateTime().toString("/yyyy/MM/dd");
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    FTP_BASE_PATH, filePath, newName, uploadFile.getInputStream());
            //返回结果
            if(!result) {
                resultMap.put("error", 1);
                resultMap.put("message", "文件上传失败");
                return resultMap;
            }
            resultMap.put("error", 0);
            resultMap.put("url", FILE_BASE_URL + filePath + "/" + newName);
            return resultMap;

        } catch (Exception e) {
            resultMap.put("error", 1);
            resultMap.put("message", "文件上传发生异常");
            return resultMap;
        }
    }

}
