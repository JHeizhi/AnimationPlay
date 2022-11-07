package com.kinoct.Utils;

import java.io.File;

/**
 * @author Silolo
 * @version 1.0
 * @ClassName deleteTempFiles
 * @description: TODO
 * @date 2022/11/7 22:36
 */
public class DeleteTempFiles {

    public Boolean deleteFiles(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件是否存在以及文件路径是否正确");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFiles(f);
            } else {
                //文件删除
                f.delete();
            }
        }
        file.delete();
        return true;
    }
}
