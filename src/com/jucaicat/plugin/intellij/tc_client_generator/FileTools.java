package com.jucaicat.plugin.intellij.tc_client_generator;

import java.io.*;

/**
 * Created by HUANG_YA_DONG cast ZHANG_SAN_FENG on 16/3/23.
 * 文件操作类
 */
class FileTools {
    /**
     * 复制文件.
     *
     * @param srcFileName  源文件名
     * @param destFileName 目标文件名
     * @param overlay      是否覆盖
     * @return 复制是否成功
     */
    static boolean copyFile(String srcFileName,
                            String destFileName,
                            boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            //源文件不存在
            return false;
        } else if (!srcFile.isFile()) {
            //源文件不是一个文件
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteRead; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取txt文件的内容
     *
     * @param fileName 文件名
     * @return 返回文件内容 string
     */
    static String file2String(String fileName) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            //构造一个BufferedReader类来读取文件
            String s;
            while ((s = br.readLine()) != null) {
                //使用readLine方法，一次读一行
                result = result + "\n" + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 方法追加文件：使用FileWriter
     *
     * @param fileName 写入的文件名
     * @param content  写入的内容
     */
    static void string2File(String fileName, String content) {
        try {
            //检查目录
            String dirPath = fileName.substring(0, fileName.lastIndexOf("/"));

            File writeFile = new File(dirPath);
            //如果目录不存在,则创建目录;
            if (!writeFile.exists()) {
                //mkdirs是创建多级目录,而mkdir是创建单级目录;
                writeFile.mkdirs();
            }

            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, false);

            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除文件内容.
     *
     * @param fileName 文件名
     */
    static void clearFileContent(String fileName) {
        File f = new File(fileName);
        FileWriter fw;
        try {
            fw = new FileWriter(f);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
