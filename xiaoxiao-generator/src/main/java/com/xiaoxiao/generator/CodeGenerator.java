package com.xiaoxiao.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

public class CodeGenerator {
    public static void main(String[] args) {

        String dbUrl, username, password, domain;
        try {
            Properties properties = new Properties();
            properties.load(CodeGenerator.class.getClassLoader().getResourceAsStream("config.properties"));

            dbUrl = properties.getProperty("dbUrl");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            domain = properties.getProperty("domain");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("=====================数据库配置=======================");

        System.out.print("请输入 目标模块: ");
        final String module = scan.nextLine();
        final String javaOutPutPath = System.getProperty("user.dir") + "/" + module + "/src/main/java";
        final String xmlOutPutPath = System.getProperty("user.dir") + "/" + module + "/src/main/resources/mapper";
        final String urlPrefix = module.split("-")[1];

        System.out.print("请输入 作者: ");
        String author = scan.nextLine();

        System.out.print("请输入 需要生成的表，多个表按空格分开: ");
        String[] tables = scan.nextLine().split(" ");

        System.out.print("请输入 需要过滤的表前缀，多个表前缀按空格分开: ");
        String[] tablePrefixes = scan.nextLine().split(" ");

        // 1、配置数据源
        FastAutoGenerator.create(dbUrl, username, password)
                // 2、全局配置
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者名
                            .outputDir(javaOutPutPath) // 设置输出路径：项目的 java 目录下
                            .commentDate("yyyy-MM-dd hh:mm:ss") // 注释日期
                            .dateType(DateType.SQL_PACK) // 定义生成的实体类中日期的类型 TIME_PACK=LocalDateTime;ONLY_DATE=Date;
                            .fileOverride() // 覆盖之前的文件
                            .disableOpenDir(); // 禁止打开输出目录，默认打开
                })
                // 3、包配置
                .packageConfig(builder -> {
                    builder.parent(domain) // 设置父包名
                            .moduleName(urlPrefix) // 设置模块包名
                            .entity("model.entity") // pojo实体类包名
                            .service("service") // Service包名
                            .serviceImpl("service.serviceImpl") // ServiceImpl包名
                            .mapper("mapper") // Mapper包名
                            .xml("mapper") // Mapper XML包名
                            .controller("controller") // Controller包名
                            .other("utils") // 自定义文件包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, xmlOutPutPath)); // 配置 mapper.xml 路径信息：项目的 resources 目录下
                })
                // 4、策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的数据表名
                            .addTablePrefix(tablePrefixes) // 设置过滤表前缀

                            .mapperBuilder() // 4.1、Mapper策略配置
                            .superClass(BaseMapper.class) // 设置父类
                            .formatMapperFileName("%sMapper") // 格式化 mapper 文件名称
                            .enableMapperAnnotation() // 开启 @Mapper 注解
                            .formatXmlFileName("%sMapper") // 格式化 Xml 文件名称

                            .serviceBuilder() // 4.2、service 策略配置
                            .formatServiceFileName("%sService") // 格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                            .formatServiceImplFileName("%sServiceImpl") // 格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl

                            .entityBuilder() // 4.3、实体类策略配置
                            .enableLombok() // 开启 Lombok
                            .logicDeleteColumnName("is_delete") // 逻辑删除字段名
                            .naming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略：下划线转驼峰命名
                            .columnNaming(NamingStrategy.underline_to_camel) // 数据库表字段映射到实体的命名策略：下划线转驼峰命
                            .addTableFills( //添加表字段填充，
                                    new Column("create_time", FieldFill.INSERT), // "create_time"字段自动填充为插入时间
                                    new Column("update_time", FieldFill.INSERT_UPDATE) // "update_time"字段自动填充为插入修改时间
                            ).enableTableFieldAnnotation() // 开启生成实体时生成字段注解

                            // 4.4、Controller策略配置
                            .controllerBuilder().formatFileName("%sController") // 格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
                            .enableRestStyle(); // 开启生成 @RestController 控制器
                })
                // 5、设置模板引擎
                .templateEngine(new VelocityTemplateEngine())
                // 6、 设置模板
                .templateConfig(builder -> {
                    builder.disable(TemplateType.ENTITY) // 禁用模板	TemplateType.ENTITY
                            .entity("/templates/entity.java") // 实体模板
                            .service("/templates/service.java") // service
                            .serviceImpl("/templates/serviceImpl.java") // serviceImpl模板
                            .mapper("/templates/mapper.java") // mapper模板
                            .mapperXml("/templates/mapper.xml") // mapper模板
                            .controller("/templates/controller.java") // controller模板
                            .build();
                })
                // 6、执行
                .execute();

    }

}