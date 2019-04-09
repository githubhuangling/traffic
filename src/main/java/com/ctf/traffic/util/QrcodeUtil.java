package com.ctf.traffic.util;

import java.awt.image.*;
import java.util.*;

import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.decoder.*;

/**
 * @author ramer
 * @Date 7/6/2018
 * @see
 */
public class QrcodeUtil{

    private static final int ON_COLOR = 0X3E91E3;
    private static final int OFF_COLOR = 0XFFFFFF;

    /**
     * QRCodeCreate(生成二维码)
     *
     * @param content 二维码内容
     * @param w       宽度
     * @param h       高度
     * @return
     */
    public static BufferedImage QRCodeCreate(String content, Integer w, Integer h) {
        return QRCodeCreate(content, w, h, ON_COLOR, OFF_COLOR);
    }

    public static BufferedImage QRCodeCreate(String content, Integer W, Integer H, int onColor, int offColor) {
        //生成二维码
        MultiFormatWriter mfw = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        //        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 3);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        try {
            bitMatrix = mfw.encode(content, BarcodeFormat.QR_CODE, W, H, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? onColor : offColor);
            }
        }
        return image;
    }
}
