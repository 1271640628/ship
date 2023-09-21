package com.sugar.basedb;

import com.sugar.server.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ConfigCache implements Job {

    public static Map<String, List<List<Object>>> configMap = new LinkedHashMap();


    public static List<Object> getConfigId(String name, int id) {
        List<List<Object>> list = configMap.get(name.toLowerCase());
        List<List<Object>> data = list.stream().filter(l -> Integer.parseInt((String) l.get(0)) == id).collect(Collectors.toList());
        if (data.size()==0) {
            log.error("没在{}中找到{}", name, id);
            return null;
        }
        return data.get(0);
    }

    public static List<List<Object>> getConfigList(String name) {
        List<List<Object>> list = configMap.get(name.toLowerCase());
       return list;
    }

    public static Object getGameData(int id) {
        List<Object> list = getConfigId("gameData", id);
        return list.get(1);
    }


    public static void init() {
        String filePath = Option.i().excel;
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains("~$")) continue;
            List<List<Object>> list = excel(filePath + "/" + file.getName());
            if (list == null) log.error("excel文件读取错误：{}" + file.getName());
            String fileNmae = file.getName();
            log.info("{}文件读取完成", fileNmae);
            configMap.put(fileNmae.split("\\.")[0].toLowerCase(), list);
        }
    }

    public static List<List<Object>> excel(String filePath) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        wb = readExcel(filePath);
        if (wb != null) {
            //只读取第一个页签
            sheet = wb.getSheetAt(0);
            List<List<Object>> data = new LinkedList<>();

            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                row = sheet.getRow(rowNum);
                if (rowNum <= 2) continue;
                List<Object> rowList = new ArrayList<>();
                for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                    Cell cell = sheet.getRow(rowNum).getCell(cellNum);
                    rowList.add(getStringCellValue(cell));
                }
                data.add(rowList);
            }
            return data;
        }
        return null;
    }


    //判断文件格式
    private static Workbook readExcel(String filePath) {
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));

        try {
            @SuppressWarnings("resource") InputStream is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("deprecation")
    public static String getStringCellValue(Cell cell) {
        String cellvalue = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellvalue = String.valueOf(cell.getDateCellValue());
                } else {
                    cellvalue = String.valueOf(new DecimalFormat("0").format(cell.getNumericCellValue()));
                }
                break;
            case STRING:
                cellvalue = cell.getStringCellValue();
                break;
            default:
                cellvalue = "";
                break;
        }

        return cellvalue;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        init();
    }
}
