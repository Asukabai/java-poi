package com.bai.javapoi.service.impl;


import com.bai.javapoi.pojo.dto.RespondDto;
import com.bai.javapoi.service.FileService;
import com.bai.javapoi.task.ConfigCheck;
import com.bai.javapoi.task.ConfigCheck2;
import com.bai.javapoi.task.ConfigCheck3;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.bai.javapoi.utils.ExcelUtils.copyCellValue;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private ConfigCheck configCheck;
    @Resource
    private ConfigCheck2 configCheck2;
    @Resource
    private ConfigCheck3 configCheck3;

    @Value("${absolutePath}")
    private String absolutePath;

//    private  final String  XXX="x,ccc";
    /**
     * 实现多文件一次性上传功能【上传多个Excel文件，并获取每个上传文件的类型】
     *
     * 0、实现上传的文件保存到本地
     * 1、将每个文件的名字以文件类型的名字进行重命名，并且保存到指定文件路径下，并且将文件文件路径以数组的形式返回给前端
     *
     * @param files
     * @param types
     * @return
     */

    /**
     *
     * 实际上，MultipartFile 接口中的 InputStream 对象应该由 Spring 框架管理，不需要手动关闭。在 Spring 中，MultipartFile 的资源管理是由框架自动处理的，所以不需要手动关闭输入流。
     */

    @Override
    public RespondDto upLoadFiles(MultipartFile[] files, String[] types) {
        List<String> fileUrls = new ArrayList<>();
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = current.format(formatter);
        // 加上三位随机数
        Random random = new Random();
        int randomNum = random.nextInt(999);
        // 将每次上传的Excel文件放到指定的文件夹下
        File dir = new File(absolutePath + File.separator + "uploadFile" + File.separator + formattedDate + randomNum);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 存储已上传的type类型
        Set<String> uploadedTypes = new HashSet<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String type = types[i];
            // 判断文件或类型不能为空,不能上传空文件
            if (file.isEmpty() || StringUtils.isEmpty(type)) {
                return new RespondDto<>("文件或类型不能为空，请重新上传！");
            }
            String originalFileName = file.getOriginalFilename();
            // 判断文件后缀名是否符合要求
            if (!originalFileName.endsWith("xlsx")) {
                log.error(originalFileName + "不是.xlsx后缀的Excel文件!");
                throw new RuntimeException("仅支持.xlsx后缀的Excel文件!");
            }
            // 检查type是否已存在
            if (uploadedTypes.contains(type)) {
                return new RespondDto<>("上传文件的type类型重复，请重新上传！");
            }
            uploadedTypes.add(type);
            // 获取文件名和扩展名
            String fileExt = originalFileName.substring(originalFileName.lastIndexOf("."));
            File fileToSave = new File(dir.getAbsolutePath() + File.separator + type + fileExt);
            FileOutputStream os = null;
            try {
                //文件写入
                os = new FileOutputStream(fileToSave);
                IOUtils.copy(file.getInputStream(),os);
                String fileUrl = fileToSave.getAbsolutePath();
                fileUrls.add(fileUrl);
                log.info("【源文件】" + originalFileName + "已保存到本地，【文件名】是 " + type + " 【存放路径是】 " + fileToSave.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                return new RespondDto<>("文件上传失败！");
            } finally {
                if (os != null) {
                    IOUtils.closeQuietly(os);
                }
            }
        }
        return new RespondDto<>(fileUrls);
    }



    /**
     * 实现多文件解析并导出生成测试报告功能
     *
     * 0、将源文件路径遍历出来，每次都放到输入流中，同时去遍历配置文件，将类型名称与文件名称符合的配置文件读取出来
     * 1、进行copy操作，最后将源文件路径遍历完后将目标Excel进行保存
     */
    @Override
    public RespondDto writeExcel(HttpServletResponse response, String[] fileUrls) throws IOException, URISyntaxException {
        FileInputStream destinationFileInputStream = null;
        XSSFWorkbook destinationWorkbook = null;
        try {
            String excelFilePath = absolutePath + File.separator + "template.xlsx";
            destinationFileInputStream = new FileInputStream(excelFilePath);
            destinationWorkbook = new XSSFWorkbook(destinationFileInputStream);

            List<String> errorMessages = new ArrayList<>();
            for (String url : fileUrls) {
                processFile(url, destinationWorkbook, configCheck.getConfig(), errorMessages);
                checkFile(url, destinationWorkbook, configCheck3.getConfig(), errorMessages);
                processFile2(url, destinationWorkbook, configCheck2.getConfig(), errorMessages);
            }
            destinationWorkbook.setForceFormulaRecalculation(true);
            destinationWorkbook.write(response.getOutputStream());
            if (errorMessages.isEmpty()) {
                return new RespondDto<>("Excel导出成功!");
            } else {
                String errorInfo = String.join("\n", errorMessages);
                return new RespondDto<>("Excel导出过程中发生错误:\n" + errorInfo);
            }
        } finally {
            if (destinationWorkbook != null) {
                destinationWorkbook.close();
            }
            if (destinationFileInputStream != null) {
                destinationFileInputStream.close();
            }
        }
    }

    private void checkFile(String fileUrl, Workbook destinationWorkbook, Properties configProperties3, List<String> errorMessages) throws IOException {
        List<String> sets = Arrays.asList(StringUtils.split(","));
        if (fileUrl != null && sets.contains(fileUrl)) {
            // 以流的形式进行读取每个源文件
            FileInputStream sourceFileInputStream = null;
            XSSFWorkbook sourceWorkbook1 = null;
            try {
                sourceFileInputStream = new FileInputStream(fileUrl);
                sourceWorkbook1 = new XSSFWorkbook(sourceFileInputStream);
                Sheet sheet = sourceWorkbook1.getSheet("主特性");
                // 获取第10行，索引从0开始
                Row row = sheet.getRow(9);
                // 获取AP列（索引为41）的单元格
                Cell cell = row.getCell(41);
                if (cell != null && Objects.equals(cell.getCellTypeEnum(), CellType.STRING)) {
                    String value = cell.getStringCellValue();
                    if (!value.equals("DC")) {
                        return; // 如果是"DC"，直接退出方法
                    }
                } else {
                    return; // 如果单元格为空或者不是字符串类型，直接退出方法
                }
                // TODO: 在这里编写处理AP10为"DC"的情况下的后续操作代码
                // 执行后续操作
                File file = new File(fileUrl);
                //5、使用String的substring()方法截取文件名字部分
                String fileNameWithExtension = file.getName();
                String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."));
                //定义一个源map,存储原excel中的sheet数据,充当一个缓存
                Map<String, Sheet> sheetMap = new HashMap<>();
                //将类型名和文件名符合的配置文件内容进行解析读取
                for (String key : configProperties3.stringPropertyNames()) {
                    //如果配置文件中含有与文件名对应的文件类型字段，就进行摘取
                    if (key.contains(fileName)) {
                        String[] srcArray = org.springframework.util.StringUtils.tokenizeToStringArray(key, ".");
                        String[] destArray = org.springframework.util.StringUtils.tokenizeToStringArray(configProperties3.getProperty(key), ".");
                        Sheet sourceSheet;
                        if (sheetMap.containsKey(fileName + srcArray[1])) {
                            sourceSheet = sheetMap.get(fileName + srcArray[1]);
                        } else {
                            sourceSheet = sourceWorkbook1.getSheet(srcArray[1]);
                            sheetMap.put(fileName + srcArray[1], sourceSheet);
                        }
                        if (sourceSheet == null) {
                            String errorMessage = "源文件路径：" + fileUrl + " 中的 sheet [" + srcArray[1] + "]不存在";
                            errorMessages.add(errorMessage);
                            log.error(errorMessage);
                            break;
                        }
                        CellReference sourceCellRef = new CellReference(srcArray[2]);
                        if (sourceCellRef == null) {
                            String errorMessage = "源文件路径：" + fileUrl + " 中的cell[" + srcArray[2] + "]引用无效";
                            errorMessages.add(errorMessage);
                            log.error(errorMessage);
                            break;
                        }
                        Row sourceRow = sourceSheet.getRow(sourceCellRef.getRow());
                        Cell sourceCell = null;
                        if (sourceRow != null) {
                            sourceCell = sourceRow.getCell(sourceCellRef.getCol());
                        }
                        Sheet destinationSheets = destinationWorkbook.getSheet(destArray[0]);
                        CellReference destCellRef = new CellReference(destArray[1]);
                        Row destRow = destinationSheets.getRow(destCellRef.getRow());
                        if (destRow == null) {
                            destRow = destinationSheets.createRow(destCellRef.getRow());
                        }
                        Cell destCell = destRow.getCell(destCellRef.getCol());
                        if (destCell == null) {
                            Cell destCells = destRow.createCell(destCellRef.getCol());
                            copyCellValue(sourceCell, destCells, "1");
                        } else {
                            // 执行 copy 方法
                            copyCellValue(sourceCell, destCell, "1");
                        }
                    }
                }
            }finally {

                if (sourceFileInputStream != null) {
                    sourceFileInputStream.close();
                }
                if (sourceWorkbook1 != null) {
                    sourceWorkbook1.close();
                }
            }
        }
    }

    private void processFile(String fileUrl, Workbook destinationWorkbook, Properties configProperties, List<String> errorMessages) throws IOException {
        FileInputStream sourceFileInputStream = null;
        XSSFWorkbook sourceWorkbook = null;
        try {
            sourceFileInputStream = new FileInputStream(fileUrl);
            sourceWorkbook = new XSSFWorkbook(sourceFileInputStream);

            File file = new File(fileUrl);
            String fileNameWithExtension = file.getName();
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."));

            Map<String, Sheet> sheetMap = new HashMap<>();

            for (String key : configProperties.stringPropertyNames()) {
                if (key.contains(fileName)) {
                    String[] srcArray = org.springframework.util.StringUtils.tokenizeToStringArray(key, ".");
                    String[] destArray = org.springframework.util.StringUtils.tokenizeToStringArray(configProperties.getProperty(key), ".");
                    Sheet sourceSheet;
                    if (sheetMap.containsKey(fileName + srcArray[1])) {
                        sourceSheet = sheetMap.get(fileName + srcArray[1]);
                    } else {
                        sourceSheet = sourceWorkbook.getSheet(srcArray[1]);
                        sheetMap.put(fileName + srcArray[1], sourceSheet);
                    }
                    if (sourceSheet == null) {
                        String errorMessage = "源文件路径：" + fileUrl + " 中的 sheet [" + srcArray[1] + "]不存在";
                        errorMessages.add(errorMessage);
                        log.error(errorMessage);
                        break;
                    }
                    CellReference sourceCellRef = new CellReference(srcArray[2]);
                    if (sourceCellRef == null) {
                        String errorMessage = "源文件路径：" + fileUrl + " 中的cell[" + srcArray[2] + "]引用无效";
                        errorMessages.add(errorMessage);
                        log.error(errorMessage);
                        break;
                    }
                    Row sourceRow = sourceSheet.getRow(sourceCellRef.getRow());
                    Cell sourceCell = null;
                    if (sourceRow != null) {
                        sourceCell = sourceRow.getCell(sourceCellRef.getCol());
                    }
                    Sheet destinationSheet = destinationWorkbook.getSheet(destArray[0]);
                    CellReference destCellRef = new CellReference(destArray[1]);
                    Row destRow = destinationSheet.getRow(destCellRef.getRow());
                    if (destRow == null) {
                        destRow = destinationSheet.createRow(destCellRef.getRow());
                    }
                    Cell destCell = destRow.createCell(destCellRef.getCol());

                    copyCellValue(sourceCell, destCell, "0");
                }
            }
        } finally {

            if (sourceFileInputStream != null) {
                sourceFileInputStream.close();
            }
            if (sourceWorkbook != null) {
                sourceWorkbook.close();
            }
        }
    }
    private void processFile2(String fileUrl, Workbook destinationWorkbook, Properties configProperties2, List<String> errorMessages) throws IOException {
        if (fileUrl != null && fileUrl.contains("符合性")) {

            //4、以流的形式进行读取每个源文件
            FileInputStream sourceFileInputStream = null;
            XSSFWorkbook sourceWorkbook2 = null;
            try{
                sourceFileInputStream = new FileInputStream(fileUrl);
                sourceWorkbook2 = new XSSFWorkbook(sourceFileInputStream);
                File file = new File(fileUrl);
                //5、使用String的substring()方法截取文件名字部分
                String fileNameWithExtension = file.getName();
                String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."));
                //定义一个源map,存储原excel中的sheet数据,充当一个缓存
                Map<String, Sheet> sheetMap = new HashMap<>();
                //将类型名和文件名符合的配置文件内容进行解析读取
                for (String key : configProperties2.stringPropertyNames()) {
                    //如果配置文件中含有与文件名对应的文件类型字段，就进行摘取
                    if (key.contains(fileName)) {
                        String[] srcArray = org.springframework.util.StringUtils.tokenizeToStringArray(key, ".");
                        String[] destArray = org.springframework.util.StringUtils.tokenizeToStringArray(configProperties2.getProperty(key), ".");
                        Sheet sourceSheet;
                        if (sheetMap.containsKey(fileName + srcArray[1])) {
                            sourceSheet = sheetMap.get(fileName + srcArray[1]);
                        } else {
                            sourceSheet = sourceWorkbook2.getSheet(srcArray[1]);
                            sheetMap.put(fileName + srcArray[1], sourceSheet);
                        }
                        if (sourceSheet == null) {
                            String errorMessage = "源文件路径：" + fileUrl + " 中的 sheet [" + srcArray[1] + "]不存在";
                            errorMessages.add(errorMessage);
                            log.error(errorMessage);
                            break;
                        }
                        CellReference sourceCellRef = new CellReference(srcArray[2]);
                        if (sourceCellRef == null) {
                            String errorMessage = "源文件路径：" + fileUrl + " 中的cell[" + srcArray[2] + "]引用无效";
                            errorMessages.add(errorMessage);
                            log.error(errorMessage);
                            break;
                        }
                        Row sourceRow = sourceSheet.getRow(sourceCellRef.getRow());
                        Cell sourceCell = null;
                        if (sourceRow != null) {
                            sourceCell = sourceRow.getCell(sourceCellRef.getCol());
                        }
                        Sheet destinationSheets = destinationWorkbook.getSheet(destArray[0]);
                        CellReference destCellRef = new CellReference(destArray[1]);
                        Row destRow = destinationSheets.getRow(destCellRef.getRow());
                        if (destRow == null) {
                            destRow = destinationSheets.createRow(destCellRef.getRow());
                        }
                        Cell destCell = destRow.getCell(destCellRef.getCol());
                        if(destCell == null){
                            Cell destCells = destRow.createCell(destCellRef.getCol());
                            copyCellValue(sourceCell, destCells,"1");
                        }else{
                            // 执行 copy 方法
                            copyCellValue(sourceCell, destCell,"1");
                        }
                    }
                }
            }finally {

                if (sourceFileInputStream != null) {
                    sourceFileInputStream.close();
                }
                if (sourceWorkbook2 != null) {
                    sourceWorkbook2.close();
                }
            }
        }else {
            // 配置文件为空，不读取配置文件
            log.info("未发现符合性表文件！");
        }
    }
}