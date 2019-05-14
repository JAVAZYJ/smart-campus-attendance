package com.lxzh.smart.campus.attendance.modular;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;

import java.util.ArrayList;

public class CodeGenerateTest {

    public static void main(String[] args) {
        GenerateParams generateParams = new GenerateParams();
        generateParams.setAuthor("zyj");//作者
        generateParams.setOutputDirectory("D:/Develop/IntelliJ IDEA 2019.1.1/workspaces/smart-campus-attendance/smart-campus-attendance-app/src/main/java");//项目的绝对路径一直到src/main/java为止
        generateParams.setJdbcDriver("com.mysql.jdbc.Driver");//我使用的5.8以上的版本所有要用这个Driver
        generateParams.setJdbcUserName("root");//自己的数据库账号
        generateParams.setJdbcPassword("root");//自己的数据库密码
        generateParams.setJdbcUrl("jdbc:mysql://localhost:3306/attendance?characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        generateParams.setParentPackage("com.lxzh.smart.campus.attendance.modular");//生成存放的目录
        generateParams.setGeneratorInterface(true);
        //generateParams.setIncludeTables(new String[]{"areas","cities","provinces"});//对应数据库表名进行生成
        generateParams.setIncludeTables(new String[]{"attendance_teacher_record","attendance_teacher_history"});
        generateParams.setEntityName("%s");
        SimpleGenerator.doGeneration(generateParams);
    }
    @Data
    public static class GenerateParams {

        //生成代码里，注释的作者
        private String author = "zyj";

        //代码生成输出的目录，可为项目路径的相对路径
        private String outputDirectory = "temp";

        //jdbc驱动
        private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

        //数据库连接地址
        private String jdbcUrl = "jdbc:mysql://localhost/attendance?serverTimezone=GMT%2B8&autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC";

        //数据库账号
        private String jdbcUserName = "root";

        //数据库密码
        private String jdbcPassword = "root";

        //去掉表的前缀
        private String[] removeTablePrefix = {"xx_"};

        //代码生成包含的表，可为空，为空默认生成所有
        private String[] includeTables;

        //代码生成的类的父包名称
        private String parentPackage = "com.lxzh.smart.campus.attendance.modular";

        //service是否生成接口，这个根据自己项目情况决定
        private Boolean generatorInterface = false;

        private String entityName;

    }

    public static class SimpleGenerator {

        public static void doGeneration(GenerateParams generateParams) {

            AutoGenerator mpg = new AutoGenerator();

            // 全局配置
            GlobalConfig gc = new GlobalConfig();
            gc.setOutputDir(generateParams.getOutputDirectory());
            gc.setFileOverride(true);
            gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
            gc.setBaseResultMap(true);// XML ResultMap
            gc.setBaseColumnList(true);// XML columList
            gc.setEnableCache(false);
            gc.setOpen(false);
            gc.setAuthor(generateParams.getAuthor());
            gc.setEntityName(generateParams.getEntityName());

            // 自定义文件命名，注意 %s 会自动填充表实体属性！
            if (generateParams.getGeneratorInterface()) {
                gc.setServiceName("%sService");
                gc.setServiceImplName("%sServiceImpl");
            } else {
                gc.setServiceName("%sService");
                gc.setServiceImplName("%sService");
            }
            mpg.setGlobalConfig(gc);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setDbType(DbType.MYSQL);
            dsc.setDriverName(generateParams.getJdbcDriver());
            dsc.setUrl(generateParams.getJdbcUrl());
            dsc.setUsername(generateParams.getJdbcUserName());
            dsc.setPassword(generateParams.getJdbcPassword());
            mpg.setDataSource(dsc);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            strategy.setCapitalMode(false);

            strategy.setEntityTableFieldAnnotationEnable(true);

            // 此处可以移除表前缀表前缀
            strategy.setTablePrefix(generateParams.getRemoveTablePrefix());

            // 表名生成策略
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);

            // 需要生成的表
            strategy.setInclude(generateParams.getIncludeTables());

            // 公共字段填充
            ArrayList<TableFill> tableFills = new ArrayList<>();
            tableFills.add(new TableFill("CREATE_TIME", FieldFill.INSERT));
            tableFills.add(new TableFill("UPDATE_TIME", FieldFill.UPDATE));
            tableFills.add(new TableFill("CREATE_USER", FieldFill.INSERT));
            tableFills.add(new TableFill("UPDATE_USER", FieldFill.UPDATE));
            strategy.setTableFillList(tableFills);

            mpg.setStrategy(strategy);

            // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
            // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
            TemplateConfig tc = new TemplateConfig();
            tc.setController(null);

            if (!generateParams.getGeneratorInterface()) {
                tc.setService(null);
                tc.setServiceImpl("/templates/NoneInterfaceServiceImpl.java");
            }

            //如上任何一个模块如果设置 空 OR Null 将不生成该模块。
            mpg.setTemplate(tc);

            // 包配置
            PackageConfig pc = new PackageConfig();
            pc.setParent(generateParams.getParentPackage());
            pc.setModuleName("");
            pc.setXml("mapper.mapping");

            if (generateParams.getGeneratorInterface()) {
                pc.setServiceImpl("service.impl");
                pc.setService("service");
            } else {
                pc.setServiceImpl("service");
                pc.setService("service");
            }

            mpg.setPackageInfo(pc);


            // 执行生成
            mpg.execute();
        }

        public static void main(String[] args) {
            cn.stylefeng.roses.kernel.generator.SimpleGenerator.doGeneration(new cn.stylefeng.roses.kernel.generator.GenerateParams());
        }

    }
}
