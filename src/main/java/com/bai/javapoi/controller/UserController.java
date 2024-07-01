package com.bai.javapoi.controller;

import com.bai.javapoi.pojo.User;
import com.bai.javapoi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/findPage")
    public List<User>  findPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer pageSize){
        return userService.findPage(page,pageSize);
    }

    /**
     *  利用模板导出 Excel（不带图片——员工总体信息表）
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/downLoadXlsxByPoi",name = "使用POI下载高版本")
    public void downLoadXlsx(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //       userService.downLoadXlsx(response);
        userService.downLoadXlsxWithTempalte(request,response); //下载的excel带样式
    }

    /**
     * 利用模板导出 Excel（带图片——员工个人信息表）
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/download",name = "导出用户详细信息")
    public void downLoadUserInfoWithTempalte2(Long id,HttpServletRequest request,HttpServletResponse response) throws Exception{
        userService.downLoadUserInfoWithTempalte(id,request,response);
    }

    /**
     * 百万数据的导出 1、使用高版本的Excel ；2、使用sax 方式解析Excel（XML）
     * 限制 : 不能使用模板；不能使用太多的样式
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/downLoadMillion",name = "导出用户百万数据的导出")
    public void downLoadMillion(Long id,HttpServletRequest request,HttpServletResponse response) throws Exception{
        userService.downLoadMillion(request,response);
    }
}
