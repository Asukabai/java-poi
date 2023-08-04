package com.bai.javapoi.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 员工
 */
@Data
@Table(name="tb_user2")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;         //主键
    private String userName; //员工名
    private String phone;    //手机号
    private String province; //省份名
    private String city;     //城市名
    private Integer salary;   // 工资
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date hireDate; // 入职日期
    private String deptId;   //部门id
    private Date birthday; //出生日期
    private String photo;    //一寸照片
    private String address;  //现在居住地址

    private List<Resource> resourceList; //办公用品

}
