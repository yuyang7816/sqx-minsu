package com.sqx.app.base.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.file.utils.FileUploadUtils;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里云文件上传
 *
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Tag(name = "阿里云文件上传---通用")
@RequestMapping(value = "/alioss")
@Slf4j
public class AliFileUploadController {


    private final CommonInfoService commonRepository;

    @Autowired
    public AliFileUploadController(CommonInfoService commonRepository) {
        this.commonRepository = commonRepository;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @Operation(summary = "文件上传")
    @ResponseBody
    public Result upload(@RequestParam("file") @Parameter(description = "上传文件") MultipartFile file) {
        String value = commonRepository.findOne(234).getValue();
        if ("1".equals(value)) {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath = getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //        String src = commonRepository.findOne(72).getValue()+"/"+completePath;
            String src = commonRepository.findOne(19).getValue() + "/img/" + completePath;
            return Result.success().put("data", src);
        } else {
            try {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath = "/www/wwwroot/" + split[1] + "/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http + fileName;
                return Result.success().put("data", url);
            } catch (Exception e) {
                log.error("本地上传失败：" + e.getMessage(), e);
                return Result.error(-100, "文件上传失败！");
            }
        }

    }

    @RequestMapping(value = "/uploadUniApp", method = RequestMethod.POST)
    @Operation(summary = "文件上传")
    @ResponseBody
    public String uploadUniApp(@RequestParam("file") @Parameter(description = "上传文件") MultipartFile file) {
        String value = commonRepository.findOne(234).getValue();
        if ("1".equals(value)) {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath = getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            return commonRepository.findOne(19).getValue() + "/img/" + completePath;
        } else {
            try {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath = "/www/wwwroot/" + split[1] + "/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http + fileName;
                return url;
            } catch (Exception e) {
                log.error("本地上传失败：" + e.getMessage(), e);
                return null;
            }
        }

    }

    @RequestMapping(value = "/uploadMusic", method = RequestMethod.POST)
    @Operation(summary = "文件上传")
    @ResponseBody
    public Result uploadMusic(@RequestParam("file") @Parameter(description = "上传文件") MultipartFile file) {
        String url = "";
        String value = commonRepository.findOne(234).getValue();
        if ("1".equals(value)) {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath = getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            url = commonRepository.findOne(19).getValue() + "/img/" + completePath;
        } else {
            try {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath = "/www/wwwroot/" + split[1] + "/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                url = http + fileName;
            } catch (Exception e) {
                log.error("本地上传失败：" + e.getMessage(), e);
                return null;
            }
        }
        if ("/".equals(url.substring(url.length() - 1, url.length()))) {
            url = url.substring(0, url.length() - 1);
        }
        int trackLength = 0;
        try {
            File file1 = multipartFileToFile(file);
            MP3File f = (MP3File) AudioFileIO.read(file1);
            MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
            trackLength = audioHeader.getTrackLength();
            delteTempFile(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("sec", trackLength);
        return Result.success().put("data", result);
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    private String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = format(new Date()) + "/" + uuid;
        return path + suffix;
    }


    private String format(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(date);
        }
        return null;
    }


}