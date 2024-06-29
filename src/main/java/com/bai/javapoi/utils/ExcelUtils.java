package com.bai.javapoi.utils;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {

    /**
     * copyCellValue() 方法用于将源单元格的值复制到目标单元格中。
     *
     * @param sourceCell 是源单元格对象
     * @param destCell   是目标单元格对象
     */
    public static void copyCellValue(Cell sourceCell, Cell destCell, String type) {

        if (sourceCell == null || destCell == null) {
            return;
        }
        //当满足符合性表的条件之后执行这个判断（如何符合性表的内容为空，则不执行复制操作，直接退出）
        if ("1".equals(type)) {
            if (sourceCell.getCellTypeEnum().equals(CellType.BLANK)) {
                return;
            }
        }
        // 首先，将源单元格的单元格样式克隆到目标单元格上，然后再将源单元格的值赋给目标单元格。
        CellStyle sourceStyle = sourceCell.getCellStyle();
        CellStyle destinationStyle = destCell.getSheet().getWorkbook().createCellStyle();
        destinationStyle.cloneStyleFrom(sourceStyle);
        destCell.setCellStyle(destinationStyle);

        // 设置字体和大小
//        Font sourceFont = sourceCell.getSheet().getWorkbook().getFontAt(sourceStyle.getFontIndex());
//        Font destinationFont = destCell.getSheet().getWorkbook().createFont();
//        destinationFont.setFontName(sourceFont.getFontName());
//        destinationFont.setFontHeightInPoints(sourceFont.getFontHeightInPoints());
//        destinationStyle.setFont(destinationFont);

        // 创建新的字体样式并设置字体和大小

        Font destinationFont = destCell.getSheet().getWorkbook().createFont();
        destinationFont.setFontName("Times New Roman");
        destinationFont.setFontHeightInPoints((short) 11);
        destinationStyle.setFont(destinationFont);
        // 如果源单元格的数据类型为字符串类型，则复制字符串值；如果为布尔类型，则复制布尔值；如果为公式类型，则复制公式字符串；如果为数字类型，则复制数字值。
        if (sourceCell.getCellTypeEnum().equals(CellType.STRING)) {
            // 如果原来的单元格中含有“-”，如果不是符合性表，则将“-”换成“”，如果是符合性表就直接退出（相当于没有内容）
            if (sourceCell.getStringCellValue().equals("-")) {
                if (!"1".equals(type)) {
                    destCell.setCellValue("");
                } else {
                    return;
                }
            } else {
                destCell.setCellValue(sourceCell.getStringCellValue());
            }
        } else if (sourceCell.getCellTypeEnum().equals(CellType.BOOLEAN)) {

            destCell.setCellValue(sourceCell.getBooleanCellValue());

        } else if (sourceCell.getCellTypeEnum().equals(CellType.FORMULA)) {
            switch (sourceCell.getCachedFormulaResultTypeEnum()) {
                case BOOLEAN:
                    destCell.setCellValue(sourceCell.getBooleanCellValue());
                    break;
                case NUMERIC:
//                    double numericValue = sourceCell.getNumericCellValue();
//                    // 设置格式化规则，保留小数点后两位（因为有的公式算出来的结果可能是一个无限不循环小数）
//                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                    // 格式化数字值（destCell.setCellValue(sourceCell.getNumericCellValue());这样解决不了）
//                    String formattedValue = decimalFormat.format(numericValue);
//                    destCell.setCellValue(Double.parseDouble(formattedValue));
//                    destCell.setCellValue(numericValue);


                    double numericValue = sourceCell.getNumericCellValue();
                    String formattedValue;
                    if (String.valueOf(numericValue).length() > 3) {
                        formattedValue = String.format("%.3f", numericValue);
                    } else {
                        formattedValue = String.valueOf(numericValue);
                    }
                    destCell.setCellValue(Double.parseDouble(formattedValue));

                    break;
                case STRING:
                    if (sourceCell.getStringCellValue().equals("-")) {
                        if (!"1".equals(type)) {
                            destCell.setCellValue("");
                        } else {
                            break;
                        }
                    } else {
                        destCell.setCellValue(sourceCell.getStringCellValue());
                    }
                    break;
            }
        } else if (sourceCell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(sourceCell)) {
                destCell.setCellValue(sourceCell.getDateCellValue());
            } else {
                destCell.setCellValue(sourceCell.getNumericCellValue());
            }// 如果不是符合性表，如果源单元格是空的，则在目标单元格中写入一个空串("")。
        } else if (!"1".equals(type)) {
            if (sourceCell.getCellTypeEnum().equals(CellType.BLANK)) {
                destCell.setCellValue("");
            }
        }

        // 设置目标单元格边框
        // 设置边框样式和颜色
        // 边框宽度
        short borderWidth = 1;
        // 边框样式
        BorderStyle borderStyle = BorderStyle.THIN;
        // 边框颜色
        short borderColor = IndexedColors.BLACK.getIndex();
        // 设置所有边框的样式和颜色
        destinationStyle.setBorderTop(borderStyle);
        destinationStyle.setBorderRight(borderStyle);
        destinationStyle.setBorderBottom(borderStyle);
        destinationStyle.setBorderLeft(borderStyle);
        destinationStyle.setTopBorderColor(borderColor);
        destinationStyle.setRightBorderColor(borderColor);
        destinationStyle.setBottomBorderColor(borderColor);
        destinationStyle.setLeftBorderColor(borderColor);
        // 设置边框宽度
        destinationStyle.setBorderTop(BorderStyle.THIN);
        destinationStyle.setBorderRight(BorderStyle.THIN);
        destinationStyle.setBorderBottom(BorderStyle.THIN);
        destinationStyle.setBorderLeft(BorderStyle.THIN);
        destinationStyle.setTopBorderColor(borderColor);
        destinationStyle.setRightBorderColor(borderColor);
        destinationStyle.setBottomBorderColor(borderColor);
        destinationStyle.setLeftBorderColor(borderColor);
    }
}
