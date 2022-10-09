package com.charwayh.esb.support.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.charwayh.esb.support.cache.ReadDemoFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author charwayH
 * <p>
 * 1.初始化文件夹
 * 2.读取MQ模板(生成队列管理器脚本)
 * 3.根据队列脚本找到相应的队列管理器脚本依次进行追加补充
 */
@Component
public class MQUtils {
    // 脚本生成的路径
    @Value("${esb.createfile.path}")
    private static String targetPath = "/Users/CharwayH/temp1";


    // 分隔符
    static String separator = File.separator;

    // 前置机1IP
    @Value("${esb.createfile.f1port}")
    private static String f1port = "172.16.90.27";
    // 前置机2IP
    @Value("${esb.createfile.f1port}")
    private static String f2port = "172.16.90.28";
    // 集群机1IP
    @Value("${esb.createfile.c1port}")
    private static String c1port = "172.16.90.29";
    // 集群机2IP
    @Value("${esb.createfile.c2port}")
    private static String c2port = "172.16.90.30";

    /**
     * 初始化文件夹
     */
    static {
        init();
    }


    /**
     * 创建队列管理器脚本
     * @param fTotalNum 前置机总数
     */
    public static void createQMGRsh(int fTotalNum) {

        for (int fNum = 1; fNum < fTotalNum; fNum++) {
            // 获取所有的系统集合
            Iterator<String> it = getQmgrSet().iterator();
            while (it.hasNext()) {
                String sysCode =  it.next();
                // 前置队列管理器编号 如S01_1、S01_2、P01_1、P01_2
                String qmgrNum = sysCode + "_" + fNum;
                // 请求响应为S 发布订阅P
                String qmgrType = sysCode.substring(0, 1);

                String shFilePath;
                String qmgrFilePath;
                if (fNum == 1) {
                    shFilePath = new StringBuilder(targetPath).
                            append(separator + f1port + separator + "sh" + separator + qmgrNum + ".sh").toString();
                    qmgrFilePath = new StringBuilder(targetPath).
                            append(separator + f1port + separator + "sh" + separator + "mqsc" + separator + qmgrNum + ".QMGR").toString();
                } else {
                    shFilePath = new StringBuilder(targetPath).
                            append(separator + f2port + separator + "sh" + separator + qmgrNum + ".sh").toString();
                    qmgrFilePath = new StringBuilder(targetPath).
                            append(separator + f2port + separator + "sh" + separator + "mqsc" + separator + qmgrNum + ".QMGR").toString();
                }
                // 创建sh脚本执行文件
                File shFile = new File(shFilePath);
                if (!shFile.exists()) {
                    FileUtil.touch(shFile);
                }
                try {
                    FileWriter writer = new FileWriter(shFile);
                    writer.write(shContent(qmgrNum, "demo/sh.txt"));
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 创建QMGR脚本文件
                File qmgrFile = new File(qmgrFilePath);
                if (!shFile.exists()) {
                    FileUtil.touch(qmgrFile);
                }
                try {
                    FileWriter writer = new FileWriter(qmgrFile);
                    if ("S".equals(qmgrType)) {
                        writer.write(qmgrContent(sysCode.replace("S", ""),
                                "demo/QMGR_BS.txt", fNum));
                    } else {
                        writer.write(qmgrContent(sysCode.replace("P", ""),
                                "demo/QMGR_PS.txt", fNum));
                    }
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 根据csv文件生成脚本
     *
     * @param fTotalNum 前置机总数
     */
    public static void createMQsh(int fTotalNum) {
        CsvReader reader = CsvUtil.getReader();
        //从文件中读取CSV数据
        CsvData data = reader.read(FileUtil.file("demo/channel.csv"));
        List<CsvRow> rows = data.getRows();

        String qmgrNum;
        String qmgrFilePath;
        FileWriter writer;
        // 有几台前置机则执行几次。从前置机到前置机N
        for (int fNum = 1; fNum <= fTotalNum; fNum++) {
            for (int j = 1; j < rows.size(); j++) {
                // 如S01_1 、P01_2
                qmgrNum = rows.get(j).get(0) + "_" + fNum;
                if (fNum == 1) {
                    qmgrFilePath = new StringBuilder(targetPath).
                            append(separator + f1port + separator + "sh" + separator + "mqsc" + separator + qmgrNum + ".QMGR").toString();
                } else {
                    qmgrFilePath = new StringBuilder(targetPath).
                            append(separator + f2port + separator + "sh" + separator + "mqsc" + separator + qmgrNum + ".QMGR").toString();
                }
                File qmgrFile = new File(qmgrFilePath);
                try {
                    // 追加写模式
                    writer = new FileWriter(qmgrFile, true);
                    // 传入系统编号和通道号
                    writer.write(shMQContent(rows.get(j).get(0), rows.get(j).get(1)));
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 队列脚本内容
     *
     * @param sysCode
     * @param channel 通道编号
     */
    public static String shMQContent(String sysCode, String channel) {
        if (channel.contains("DC10")) {
            return new String("* {channel} 视图通道服务----------------------------\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.PUT') cluster(QMGR_CLUSTER1) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(1) clwluseq(any) replace\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.GET') cluster(QMGR_CLUSTER1) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(9) clwluseq(any) replace\n").replace("{sysCode}", sysCode).replace("{channel}", channel);
        } else if (channel.contains("DC30")) {
            return new String("* {channel} WebService通道服务----------------------------\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.PUT') cluster(QMGR_CLUSTER1) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(1) clwluseq(any) replace\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.GET') cluster(QMGR_CLUSTER1) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(9) clwluseq(any) replace\n").replace("{sysCode}", sysCode).replace("{channel}", channel);
        } else if (channel.contains("DC40")) {
            return new String("* {channel} 存储过程通道服务----------------------------\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.PUT') cluster(QMGR_CLUSTER1) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(1) clwluseq(any) replace\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.GET') cluster(QMGR_CLUSTER1) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(9) clwluseq(any) replace\n").replace("{sysCode}", sysCode).replace("{channel}", channel);
        } else if (channel.contains("PS")) {
            return new String("* {channel}服务----------------------------\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.PUT') cluster(QMGR_CLUSTER2) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(1) clwluseq(any) replace\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.GET') cluster(QMGR_CLUSTER2) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(9) clwluseq(any) replace\n" +
                    "define qlocal('EQ.{sysCode}.{channel}.COLLECT') cluster(QMGR_CLUSTER2) defpsist(YES) maxdepth(99999999) maxmsgl(99999999) clwlprty(9) clwluseq(any) replace\n").replace("{sysCode}", sysCode).replace("{channel}", channel);
        } else {
            return null;
        }
    }


    /**
     * 创建sh脚本文件
     */
    public static String shContent(String qmgrNum, String demoPath) {
        return ReadDemoFile.readDemo(demoPath).replace("{qmgrNum}", qmgrNum);
    }

    /**
     * 队列管理器脚本内容
     */
    private static String qmgrContent(String sysCode, String demoPath, Integer fNum) {
        if (fNum == 1) {
            return ReadDemoFile.readDemo(demoPath).replace("{sysCode}", sysCode).replace("{fNum}", fNum.toString())
                    .replace("{fport}", f1port)
                    .replace("{c1port}", c1port).replace("{c2port}", c2port);
        } else {
            return ReadDemoFile.readDemo(demoPath).replace("{sysCode}", sysCode).replace("{fNum}", fNum.toString())
                    .replace("{fport}", f2port)
                    .replace("{c1port}", c1port).replace("{c2port}", c2port);
        }
    }


    public static void main(String[] args) {
//        createQMGRsh("S01","1");
//        createQMGRsh("S01","2");
        createQMGRsh( 2);
        createMQsh(2);


    }

    /**
     * 初始化文件夹
     */
    public static void init() {
        createOriginal(f1port);
        createOriginal(f2port);
        createOriginal(c1port);
        createOriginal(c2port);
    }


    /**
     * 创建原始文件夹，用IP做区分
     *
     * @param ip
     */
    public static void createOriginal(String ip) {
        String shFilePath = new StringBuilder(targetPath).
                append(separator + ip).
                append(separator + "sh" + separator).append("mqsc").toString();
        File dir = new File(shFilePath);
        if (!dir.exists()) {
            FileUtil.mkdir(dir);
        }
    }

    /**
     * 根据csv文件获取队列管理器集合
     * @return
     */
    public static Set<String> getQmgrSet(){
        CsvReader reader = CsvUtil.getReader();
        //从文件中读取CSV数据
        CsvData data = reader.read(FileUtil.file("demo/channel.csv"));
        List<CsvRow> rows = data.getRows();

        Set<String> qmgrSet = new HashSet<>();
        for (int i = 1; i < rows.size(); i++) {
            qmgrSet.add(rows.get(i).get(0));
        }
        return qmgrSet;
    }
}
