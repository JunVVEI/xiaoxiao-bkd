<!-- PROJECT LOGO -->
<br />

<p align="center">
  <a href="https://github.com/shaojintian/Best_README_template/">
    <img src="imgs/basicprofile.jpg" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">xiaoxiao-bkd 紫荆校校</h3>
  <p align="center">
  项目后简要描述
  <br />
</p>

</p>

### 上手指南

* 拉取代码
* 将项目添加为maven项目并导入依赖
* 安装Nacos(2.1.1)，配置MySQL为数据源Nacos的持久化方式，其中[nacos.sql](docs%2Fnacos%2Fnacos.sql) 为相关表结构的创建语句
* 打开nacos管理页面，新建一个命名空间，名称为**dev**，将配置[nacos_config.zip](docs%2Fnacos%2Fnacos_config.zip)导入到**dev**命名空间中
* 安装MySQL(5.7.18)、Redis(6.2.6)，括号内为推荐版本
* 根据[xiaoxiao.sql](docs%2Fsql%2Fxiaoxiao.sql)创建项目业务相关的表
* 修改项目中每个模块配置文件中的nacos的server-addr配置（改为自己安装的那个地址）
* 根据以上安装的数据库地址、端口与密码，修改nacos中**dev**命名空间中的相关配置

### 文件目录说明

**TODO**

### 开发的架构

**TODO**

### 部署

**TODO**

### 使用到的框架

**TODO**

### 贡献者

**TODO**

#### 如何参与项目

**TODO**

### 版本控制

该项目使用Git进行版本管理。您可以在repository参看当前可用版本。

### 作者

**TODO**

### 版权说明

**TODO**

### 鸣谢

**TODO**