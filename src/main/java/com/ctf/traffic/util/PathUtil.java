package com.ctf.traffic.util;

import java.text.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

import lombok.extern.slf4j.*;

@Configuration
@Slf4j
public class PathUtil{
    private static String winBasePath;
    private static String otherBasePath;

    @Value("${ImgPath.winBasePath}")
    public void setWinBasePath(String winBasePath) {
        PathUtil.winBasePath = winBasePath;
    }

    @Value("${ImgPath.winBasePath}")
    public void setOtherBasePath(String otherBasePath) {
        PathUtil.otherBasePath = otherBasePath;
    }

    private static String FILE_SEPARATOR = System.getProperty("file.separator");

    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath;
        if (os.toLowerCase().startsWith("win")) {
            basePath = winBasePath;
        } else {
            basePath = otherBasePath;
        }
//        basePath = basePath.replace("/", FILE_SEPARATOR);
        return basePath;
    }

    public static String getPersonImagePath(String personEmpNo) {
        return (SavingFolder.SYSPERSON.desc.concat(personEmpNo).concat("/")).replace("/", FILE_SEPARATOR);
    }

    public static String getImagePath(SavingFolder savingFolder) {
        String path;
        switch (savingFolder) {
        case ACCIDENT:
            path = SavingFolder.ACCIDENT.desc;
            break;
        case SYSPERSON:
            path = SavingFolder.SYSPERSON.desc;
            break;
        default:
            path = new String();
            break;
        }
        Date date = new Date();
        return path.concat(new SimpleDateFormat("yyyy").format(date)).concat("/")
                .concat(new SimpleDateFormat("MM").format(date)).concat("/")
                .concat(new SimpleDateFormat("dd").format(date)).concat("/");
//                .replace("/", FILE_SEPARATOR);
    }

    public static String getResourcePath(String absolutePath, SavingFolder savingFolder) {
        String path;
        switch (savingFolder) {
        case ACCIDENT:
            path = "/accidentImage/".concat(absolutePath);
            break;
        case SYSPERSON:
            path = "/personImage/".concat(absolutePath);
            break;
        default:
            path = new String();
            break;
        }
        return path;
//        return path.replace("/", FILE_SEPARATOR);
    }

    /**
     * 文件保存目录.
     */
    public enum SavingFolder {
        SYSPERSON("upload/sysperson/"), ACCIDENT("upload/accident/");
        private String desc;

        SavingFolder(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return this.desc;
        }
    }

}
