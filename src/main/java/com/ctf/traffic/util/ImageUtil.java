package com.ctf.traffic.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

import com.ctf.traffic.util.PathUtil.SavingFolder;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 */
@Slf4j
public class ImageUtil {
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    /*
     * 生产随机文件名,当前年月日小时分钟秒钟
     */
    public static String getRandomFileName() {
        // 获取随机五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    /*
     * 保存图片
     */
    public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetAddr)
            throws Exception {
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + getRandomFileName() + getFileExtension(fileName);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnailInputStream).size(200, 200).outputQuality(0.8f).toFile(dest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取图片url失败！");
        }

        return relativeAddr;
    }

    /*
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /*
     * 创建目标路径所涉及的目录
     */
    public static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    public static String save(MultipartFile file, String name, SavingFolder savingFolder) {
        String fileName = file.getOriginalFilename();
        String rootDir = PathUtil.getImagePath(savingFolder);
        makeDirPath(rootDir);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String absolutePath = rootDir.concat(name == null ? getRandomFileName() : name)
                .concat(suffix);
        String relativePath = PathUtil.getImgBasePath().concat(absolutePath);
        log.debug(" ImageUtil.save : saved file: [{},{}]", relativePath, absolutePath);
        File file1 = new File(relativePath);

        if (file.getContentType().contains("image")) {
            try {
                //质量缩减系数
                float index = Float.parseFloat(800000.0 / Double.parseDouble(file.getSize() + "") + "");
                if (index > 1f) {
                    index = 1f;
                }
                Thumbnails.of(file.getInputStream()).scale(1).outputQuality(index).toFile(file1);
                log.info(" ImageUtil.save : [{}]", file1.length());
                return PathUtil.getResourcePath(absolutePath, savingFolder);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return null;
            }

        } else {
            try {
                file.transferTo(file1);
                return PathUtil.getResourcePath(absolutePath, savingFolder);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }

    }
}
