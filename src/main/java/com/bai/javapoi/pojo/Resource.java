package com.bai.javapoi.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 员工领取的办公用品记录表
 */
@Data
@Table(name = "tb_resource")
public class Resource {
  @Id
  private Long id;            //主键
  private String name;        //用品名称
  private Double price;       //价格
  private Long userId;        //员工id
  private Boolean needReturn; //是否需要归还
  private String photo;        //照片
}
