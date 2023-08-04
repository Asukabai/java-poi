package com.bai.javapoi.service.impl;

import com.bai.javapoi.mapper.UserMapper;
import com.bai.javapoi.pojo.User;
import com.bai.javapoi.service.UserService;
import com.bai.javapoi.utils.ExcelExportEngine;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UserServiceImpl  implements UserService {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ss");

    @Autowired
    private UserMapper userMapper;

    public List<User> findAll() {

        return userMapper.selectAll();
    }

    public List<User> findPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);  //开启分页
        Page<User> userPage = (Page<User>) userMapper.selectAll(); //实现查询
        return userPage.getResult();
    }

    public void downLoadXlsxWithTempalte(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        //        获取模板的路径
        File rootPath = new File(ResourceUtils.getURL("classpath:").getPath()); //SpringBoot项目获取根目录的方式
        System.out.println(rootPath);
        File templatePath = new File(rootPath.getAbsolutePath(),"/excel_template/userList.xlsx");
        //        读取模板文件产生workbook对象,这个workbook是一个有内容的工作薄
        Workbook workbook  = new XSSFWorkbook(templatePath);
        //        读取工作薄的第一个工作表，向工作表中放数据
        Sheet sheet = workbook.getSheetAt(0);
        //        获取第二个的sheet中那个单元格中的单元格样式
        CellStyle cellStyle = workbook.getSheetAt(1).getRow(0).getCell(0).getCellStyle();
        //        处理内容
        List<User> userList = this.findAll();
        int rowIndex = 2;
        Row row = null;
        Cell cell = null;
        for (User user : userList) {
            row = sheet.createRow(rowIndex);

            row.setHeightInPoints(15); //设置行高

            cell = row.createCell(0);
            cell.setCellValue(user.getId());
            cell.setCellStyle(cellStyle); //设置单元格样式

            cell = row.createCell(1);
            cell.setCellValue(user.getUserName());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(user.getPhone());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(simpleDateFormat.format(user.getHireDate()));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(user.getAddress());
            cell.setCellStyle(cellStyle);

            rowIndex++;
        }
        // 去除掉多余的 sheet
        workbook.removeSheetAt(1);

        // 导出的文件名称
        String filename="用户列表数据.xlsx";
        // 设置文件的打开方式和mime类型
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader( "Content-Disposition", "attachment;filename="  + new String(filename.getBytes(),"ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(outputStream);

    }


    public void downLoadUserInfoWithTempalte(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        //        获取模板的路径
        File rootPath = new File(ResourceUtils.getURL("classpath:").getPath()); //SpringBoot项目获取根目录的方式
        File templatePath = new File(rootPath.getAbsolutePath(),"/excel_template/userInfo.xlsx");
//        读取模板文件产生workbook对象,这个workbook是一个有内容的工作薄
        Workbook workbook  = new XSSFWorkbook(templatePath);
//        读取工作薄的第一个工作表，向工作表中放数据
        Sheet sheet = workbook.getSheetAt(0);
//        处理内容
        User user = userMapper.selectByPrimaryKey(id);
//        接下来向模板中单元格中放数据
//        用户名   第2行第2列
        sheet.getRow(1).getCell(1).setCellValue(user.getUserName());
//        手机号   第3行第2列
        sheet.getRow(2).getCell(1).setCellValue(user.getPhone());
//        生日     第4行第2列  日期转成字符串
        sheet.getRow(3).getCell(1).setCellValue
                (simpleDateFormat.format(user.getBirthday()));
//        工资 第5行第2列
        sheet.getRow(4).getCell(1).setCellValue(user.getSalary());
//        工资 第6行第2列
        sheet.getRow(5).getCell(1).setCellValue
                (simpleDateFormat.format(user.getHireDate()));
//        省份     第7行第2列
        sheet.getRow(6).getCell(1).setCellValue(user.getProvince());
//        现住址   第8行第2列
        sheet.getRow(7).getCell(1).setCellValue(user.getAddress());
//        司龄     第6行第4列暂时先不考虑

//        城市     第7行第4列
        sheet.getRow(6).getCell(3).setCellValue(user.getCity());

        // 先创建一个字节输出流
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

        // BufferedImage是一个带缓冲区图像类,主要作用是将一幅图片加载到内存中
        BufferedImage bufferImg = ImageIO.read(new File(rootPath + user.getPhoto()));
        // 把读取到图像放入到输出流中
        String extName = user.getPhoto().substring(user.getPhoto().lastIndexOf(".") + 1).toUpperCase();

        ImageIO.write(bufferImg, extName, byteArrayOut);
        // 创建一个绘图控制类，负责画图
        Drawing patriarch = sheet.createDrawingPatriarch();
        // 指定把图片放到哪个位置
        ClientAnchor anchor = new XSSFClientAnchor(3600, 3600, 0, 0, 2, 1, 4, 5);


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
        // 开始把图片写入到sheet指定的位置
        patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), format));

        // 导出的文件名称
        String filename="员工("+ user.getUserName() +")详细信息数据.xlsx";
        // 设置文件的打开方式和mime类型
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader( "Content-Disposition", "attachment;filename="  + new String(filename.getBytes(),"ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(outputStream);
    }

    public void downLoadUserInfoWithTempalte2(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //        获取模板的路径
        File rootPath = new File(ResourceUtils.getURL("classpath:").getPath()); //SpringBoot项目获取根目录的方式
        File templatePath = new File(rootPath.getAbsolutePath(),"/excel_template/userInfo2.xlsx");
        //        读取模板文件产生workbook对象,这个workbook是一个有内容的工作薄
        Workbook workbook  = new XSSFWorkbook(templatePath);
        // 查询用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 这里使用引擎直接导出
        workbook = ExcelExportEngine.writeToExcel(user,workbook,rootPath.getPath()+user.getPhoto());
        // 导出的文件名称
        String filename="用户详细信息数据.xlsx";
        // 设置文件的打开方式和mime类型
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader( "Content-Disposition", "attachment;filename="  + new String(filename.getBytes(),"ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(outputStream);
    }

    public void downLoadMillion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 创建一个空的工作薄
        Workbook workbook = new SXSSFWorkbook();
        int page = 1;
        int pageSize=200000;
        int rowIndex = 1; //每一个工作页的行数
        int num = 0; //总数据量
        Row row = null;
        Cell cell = null;
        Sheet sheet = null;
        while (true){  //不停地查询

             // 利用分页查询，不能直接一下子全部查询出来
            List<User> userList = this.findPage(page,pageSize);

            if(CollectionUtils.isEmpty(userList)){  //如果查询不到就不再查询了
                break;
            }
            // 取模
            if(num%1000000==0){  //每100W个就重新创建新的sheet和标题
                rowIndex = 1;
                // 在工作薄中创建一个工作表
                sheet = workbook.createSheet("第"+((num/1000000)+1)+"个工作表");
                // 设置列宽
                sheet.setColumnWidth(0,8*256);
                sheet.setColumnWidth(1,12*256);
                sheet.setColumnWidth(2,15*256);
                sheet.setColumnWidth(3,15*256);
                sheet.setColumnWidth(4,30*256);
                // 处理标题
                String[] titles = new String[]{"编号","姓名","手机号","入职日期","现住址"};
                //        创建标题行
                Row titleRow = sheet.createRow(0);

                for (int i = 0; i < titles.length; i++) {
                    cell = titleRow.createCell(i);
                    cell.setCellValue(titles[i]);
                }
            }

            // 处理内容

            for (User user : userList) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(0);
                cell.setCellValue(user.getId());

                cell = row.createCell(1);
                cell.setCellValue(user.getUserName());

                cell = row.createCell(2);
                cell.setCellValue(user.getPhone());

                cell = row.createCell(3);
                cell.setCellValue(simpleDateFormat.format(user.getHireDate()));

                cell = row.createCell(4);
                cell.setCellValue(user.getAddress());
                rowIndex++;
                num++;
            }
            page++;// 继续查询下一页
        }
        // 导出的文件名称
        String filename="百万数据.xlsx";
        // 设置文件的打开方式和mime类型
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader( "Content-Disposition", "attachment;filename="  + new String(filename.getBytes(),"ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(outputStream);
    }

}
