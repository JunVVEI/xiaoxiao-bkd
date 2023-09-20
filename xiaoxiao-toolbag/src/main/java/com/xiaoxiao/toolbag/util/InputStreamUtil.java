package com.xiaoxiao.toolbag.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 从输入流获取byte数组
 *
 * @author zjh
 */
public class InputStreamUtil {

    /**
     * 将inputStream转换为byte数组
     */
    public static byte[] toArray(InputStream inputStream) {
        byte[] data = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                byteArrayOutputStream.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            byteArrayOutputStream.flush();
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
