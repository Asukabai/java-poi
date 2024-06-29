# java-poi-合并，单一导出
🚀 基于 springboot + vue + POI 完成对报表的导入导出（主要以模板template方式），并包含报表的相关常用操作的前后端不分离的项目。

# 开发环境：

jdk 1.8
maven 3.6.3
mysql 5.6
vue 2
springboot 2.5.4
poi 4.0.1

#### 常用功能介绍

报表[forms for reporting to the higher organizations] 简单的说：报表就是用表格、图表等格式来动态显示数据，可以用公式表示为：“报表 = 多样的格式 + 动态的数据”。


![Image text](https://raw.githubusercontent.com/Asukabai/java-poi/master/20230803162624.png)

#### 合并导出功能介绍
#### 一、需求分析：
用户将一次性上传小于或等于10个不同类别的Excel文件，最终将数据按照一定逻辑规则导出生成到一个Excel文件中。
基于此需求，主要实现批量文件的上传和导出功能。

#### 二、概要设计：
创建两个接口：uploadExcels用于批量上传文件，writeExcel用于导出文件。
将不同表的单元格映射逻辑规则存储在配置文件中。

#### 三、详细设计

##### 前端设计（基于Vue）：

准备一个下拉框，用于选择文件类别。
提供一个文件上传按钮件，可以选择并添加多个Excel文件。
点击"添加"按钮后，将选择的文件类别和对应的Excel文件保存到一个数组中。
当所有文件选择完毕后，点击"上传"按钮触发上传操作，并将类型数组和文件数据传给后端。
获取后端返回的保存地址数组，启用"导出"按钮。


##### 后端设计（基于Spring Boot）：
创建一个Controller，包含两个接口：uploadExcels和writeExcel。

###### uploadExcels接口：
接收类型数组和文件数据。 将文件保存到服务器上（文件备份），并返回保存的地址数组。

###### writeExcel接口：
接收保存地址数组和导出请求。
根据配置文件中的规则解析数据并生成导出文件。
返回生成的Excel导出文件。

##### 配置文件 (config.properties )：
在配置文件中定义不同表的单元格映射逻辑规则。
这些规则将用于解析并生成导出文件。

#### 四、使用流程：
在前端场景中，拟定用户在下拉框中每选择一个文件类别，就选择一个对应类别的要上传的excel文件，选完之后点击“添加”按钮，刚才所选择的文件类别和对应的Excel文件就保存到相应的数组中去
如此往复，当将要上传的文件都选择完后，点击"上传"。于是前端将类型数组和文件数据分别传给后端，后端将文件保存到服务器上后，返回保存的地址数组。至此，上传操作完成，同时“导出”按钮由
禁用状态恢复成正常使用状态。此时用户点击导出按钮，前端接收到上一个接口返回的地址数组后，传给后端，并且传一个响应给后端，后端根据配置文件的规则，进行解析，并根据响应，生成一个Excel
导出文件。

#### 五、注意事项
不能一次性上传大于40M的单个Excel文件，否则将无法解析，出现OOM异常，解决方法是更换框架，此框架本身存在一些问题



