package com.bai.javapoi.service;


import com.bai.javapoi.pojo.User;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {


    List<User> findPage(Integer page, Integer pageSize);

    void downLoadXlsxWithTempalte(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException;

    void downLoadUserInfoWithTempalte(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException;

    void downLoadUserInfoWithTempalte2(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void downLoadMillion(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
