//package com.xiaotian.demo;
//
//import cn.tongdun.fusion.FusionTestUtil;
//import cn.tongdun.fusion.FusionUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
///**
// * @author xiaotian
// * @description
// * @date 2022-07-08 17:13
// */
//@RestController
//@RequestMapping("/test")
//public class CsvController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CsvController.class);
//
//    static {
//        FusionUtil.init("/tmp/native/");
//    }
//
//    @GetMapping("/csv")
//    public String testCsv() throws IOException {
//        FileReader reader2 = new FileReader("/root/testCpp/22222.csv");
//        FileReader reader3 = new FileReader("/root/testCpp/33333.csv");
//        BufferedReader br2 = new BufferedReader(reader2);
//        BufferedReader br3 = new BufferedReader(reader3);
//        int index = 0;
//        String line2 = null;
//        String line3 = null;
//        while ((line2 = br2.readLine() )!= null &&  (line3 = br3.readLine() ) != null) {
//            index++;
//            if (index == 1) continue;
//            String[] split2 = line2.split(",", -1);
//            String[] split3 = line3.split(",", -1);
//            if (split2.length != split3.length) {
//                //System.out.println("第"+ index+ "行数据不一致");
//                LOGGER.info("第"+ index+ "行数据不一致");
//            }
//            for (int i = 0; i < split2.length; i++) {
//                if (!split2[i].equals(split3[i])) {
//                    //System.out.println("第"+ index+ "行数据不一致");
//                    LOGGER.info("第"+ index+ "行数据不一致");
//                }
//            }
//        }
//        return "ok";
//    }
//
//    @GetMapping("/valid")
//    public String validResult(String pathone) throws IOException {
//        FileReader fileReader = new FileReader(pathone);
//        BufferedReader reader = new BufferedReader(fileReader);
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            String[] split = line.split(",");
//            //LOGGER.info("the first row: {}, the second row:{}", split[0], split[1]);
//            if (!split[1].equals(split[2])) {
//                LOGGER.error("test_id = {}行数据不一致，请检查", split[0]);
//            }
//        }
//        return "ok";
//    }
//
//    @GetMapping("/test01")
//    public String csv2(String pathOne, String pathTwo) {
//        FusionTestUtil.exportCSVFile(pathOne, pathTwo);
//        return "ok";
//    }
//
//    @GetMapping("/status")
//    public Integer testStatus() {
//        return FusionTestUtil.monitorStatus();
//    }
//}
