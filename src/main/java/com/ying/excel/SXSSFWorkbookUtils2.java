package com.ying.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

public class SXSSFWorkbookUtils2 {
    public static void main(String[] args) {
        getExcelExport();
    }

    /**
     * excel导出
     */
    public static void getExcelExport() {
        Timestamp nowTimestamp = new Timestamp(new Date().getTime());
        SXSSFWorkbook workBook = new SXSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        SXSSFSheet sheet = workBook.createSheet("SXSSFWorkbook导出");       //创建Excel工作表（页签）
        int[] width = {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000};
        for (int i = 0; i < width.length; i++) {
            sheet.setColumnWidth(i, width[i]);                       //设置列宽
        }

        //excel列
        String[] head = {"列1", "列2", "列3", "列4", "列5", "列6", "列7", "列8"};
        SXSSFRow title = sheet.createRow(0);                            //创建标题行
        title.createCell(0).setCellValue("SXSSFWorkbook导出测试");        //给标题行单元格赋值
        //合并单元格          构造参数依次为起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        getTitleStyle(workBook, title);                   //创建并初始化标题样式
        InitExcelHead(workBook, sheet, head);             //初始化抬头和样式
        setExcelValue(workBook, sheet, head);              //excel内容赋值
        excelExport(workBook);                            //导出处理

        Timestamp nowTimestamp1 = new Timestamp(new Date().getTime());
        System.err.println("------------100W数据导出耗时：\n" + (nowTimestamp1.getTime() - nowTimestamp.getTime()) / 1000);
    }

    /**
     * 标题样式
     *
     * @param workbook 创建并初始化标题样式
     */
    public static void getTitleStyle(SXSSFWorkbook workbook, Row title) {
        CellStyle style = workbook.createCellStyle();              // 创建样式
        style.setAlignment(HorizontalAlignment.CENTER);            // 字体居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        Font font = workbook.createFont();                         // 创建字体样式
        font.setFontName("宋体");                                   // 字体
        font.setFontHeightInPoints((short) 16);                    // 字体大小
        font.setBold(true);                                     // 加粗
        style.setFont(font);                          //给样式指定字体
        title.getCell(0).setCellStyle(style);        //给标题设置样式
    }

    /**
     * 初始化Excel表头
     *
     * @param workBook
     * @param sheet
     * @param head
     * @return
     */
    private static Row InitExcelHead(SXSSFWorkbook workBook, SXSSFSheet sheet, String[] head) {
        SXSSFRow row = sheet.createRow(1);
        CellStyle style = getHeaderStyle(workBook);             //获取表头样式
        for (int i = 0; i < head.length; i++) {
            row.createCell(i).setCellValue(head[i]);
            row.getCell(i).setCellStyle(style);                 //设置标题样式
        }
        return row;
    }

    /**
     * excel正文内容的填充
     *
     * @param workBook
     * @param sheet
     * @param head
     */
    private static void setExcelValue(SXSSFWorkbook workBook, SXSSFSheet sheet, String[] head) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 1000000; i++) {
            SXSSFRow row = sheet.createRow(i + 2);
            for (int j = 0; j < head.length; j++) {
                buffer.append("数据行" + (i + 1));
                buffer.append("列" + (j + 1));
                row.createCell(j).setCellValue(buffer.toString());
                buffer.delete(0, buffer.length());
            }
        }
    }

    /**
     * excel导出类
     *
     * @param sxssfWorkbook
     */
    private static void excelExport(SXSSFWorkbook sxssfWorkbook) {
        String filePath = getSavePath();  //获取文件保存路径
        if (filePath == null) {
            return;
        }
        String srcFile = filePath + "/222.xlsx";
        BufferedOutputStream outputStream = null;
        try {
            File file = new File(srcFile);
            if (file.exists()) {  //当文件已存在时
                //删除原Excel      打开新导出的Excel时，最好刷新下当前文件夹，以免重复操作有时出现缓存。
                file.delete();
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件保存路径
     *
     * @return
     */
    private static String getSavePath() {
        // 选择保存路径
        String selectPath = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置只能选择目录
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectPath = chooser.getSelectedFile().getPath();
        }
        return selectPath;
    }

    /**
     * 获取Excel表头样式(第二行)
     *
     * @param workbook
     * @return
     */
    public static CellStyle getHeaderStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.MEDIUM);  //下边框
        style.setBorderLeft(BorderStyle.MEDIUM);    //左边框
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setAlignment(HorizontalAlignment.CENTER);      //居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        style.setTopBorderColor(IndexedColors.BLACK.index);     //上边框颜色
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        Font font = workbook.createFont();               // 创建字体样式
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);              // 字体大小
        font.setBold(true);                                  // 加粗
        style.setFont(font);                                 //给样式指定字体
        return style;
    }
}