package com.bai.javapoi.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ExcelExportEngine {

    private static final SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

    public  static Workbook writeToExcel(Object object, Workbook workbook,String photoPath) throws Exception{
        // 先把bean转成map
        Map<String, Object> map = EntityUtils.entityToMap(object);
        // 循环遍历每一对数据,把日期型的转成字符串，方便导出
        for (String key : map.keySet()) {
            Object vlaue = map.get(key);
            if(vlaue instanceof Date){
                System.out.println(sdf.format(vlaue));
                map.put(key,sdf.format(vlaue));
            }
        }
        // 获取第一个sheet，整体的思路是循环100个行的100个单元格
        Sheet sheet = workbook.getSheetAt(0);
        Cell cell =null;
        Row row = null;
        for (int i = 0; i < 100; i++) {
            row = sheet.getRow(i); //获取到空行为止
            if(row==null){
                break;
            }else{
                for (int j = 0; j < 100; j++) {
                    cell = row.getCell(j);//获取到空单元格不处理 ，不能写
                    if(cell!=null){
                        writeCell(cell,map); //开始向单元格中写内容
                    }
                }
            }
        }

        if(StringUtils.isNotBlank(photoPath)){

            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//        BufferedImage是一个带缓冲区图像类,主要作用是将一幅图片加载到内存中
            BufferedImage bufferImg = ImageIO.read(new File(photoPath));


            String extName = photoPath.substring(photoPath.lastIndexOf(".") + 1).toUpperCase();

            ImageIO.write(bufferImg, extName, byteArrayOut);

            Drawing patriarch = sheet.createDrawingPatriarch();

            Sheet sheet2 = workbook.getSheetAt(1);
            row = sheet2.getRow(0);
            int col1 = ((Double) row.getCell(0).getNumericCellValue()).intValue();
            int row1 = ((Double) row.getCell(1).getNumericCellValue()).intValue();
            int col2 = ((Double) row.getCell(2).getNumericCellValue()).intValue();
            int row2 = ((Double) row.getCell(3).getNumericCellValue()).intValue();
            // 锚点，固定点
            ClientAnchor anchor = new XSSFClientAnchor(30000, 30000, 0, 0,  col1, row1, col2, row2);


            int  format = 0;

            switch(extName){
                case"JPG":{
                    format = XSSFWorkbook.PICTURE_TYPE_JPEG;
                }
                case"JPEG":{
                    format = XSSFWorkbook.PICTURE_TYPE_JPEG;
                }
                case"PNG":{
                    format = XSSFWorkbook.PICTURE_TYPE_PNG;
                }
            }

            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), format));

            workbook.removeSheetAt(1);
        }

        return workbook;
    }
    // 比较单元格中的值，是否和 map 中的 key 一致，如果一致，则向单元格中放入 map 这个key对应的值
    private static void writeCell(Cell cell, Map<String, Object> map) {
        CellType cellType = cell.getCellType();
        switch (cellType){
            case FORMULA:{  //如果是公式就直接放行了
                break;
            }default:{
                String cellValue = cell.getStringCellValue();
                //就是判断一下获取到单元格中的值是否和map中的key保持一致
                if(StringUtils.isNotBlank(cellValue)){
                    for (String key : map.keySet()) {
                        if(key.equals(cellValue)){
                            cell.setCellValue(map.get(key).toString());
                        }
                    }
                }
            }
        }
    }
}
