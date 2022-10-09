package com.charwayh.esb.support.cache;

import cn.hutool.core.io.file.FileReader;

import java.io.FileNotFoundException;


/**
 * @author charwayH
 */
public class ReadDemoFile {
    public static String readDemo(String demoPath){
            FileReader reader = new FileReader(demoPath);
            return reader.readString();
    }
}
