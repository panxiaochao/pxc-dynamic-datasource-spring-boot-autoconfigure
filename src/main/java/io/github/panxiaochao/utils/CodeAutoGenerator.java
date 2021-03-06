package io.github.panxiaochao.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lypxc
 */
public class CodeAutoGenerator {
    private String url;
    private String driverName;
    private String username;
    private String password;
    private String author = "pxc creator";
    private String packageName;
    private String moduleName;
    private String tableName;
    private DbType dbType = DbType.MYSQL;
    private IdType idType = IdType.AUTO;
    private List<String> tableFillInsertField;
    private List<String> tableFillInsertOrUpdateField;

    public CodeAutoGenerator(String url, String driverName, String username,
                             String password, String author, String packageName,
                             String moduleName, String tableName, DbType dbType, IdType idType,
                             List<String> tableFillInsertField, List<String> tableFillInsertOrUpdateField) {
        this.url = url;
        this.driverName = driverName;
        this.username = username;
        this.password = password;
        this.author = author;
        this.packageName = packageName;
        this.moduleName = moduleName;
        this.tableName = tableName;
        this.dbType = dbType;
        this.idType = idType;
        this.tableFillInsertField = tableFillInsertField;
        this.tableFillInsertOrUpdateField = tableFillInsertOrUpdateField;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public List<String> getTableFillInsertField() {
        return tableFillInsertField;
    }

    public void setTableFillInsertField(List<String> tableFillInsertField) {
        this.tableFillInsertField = tableFillInsertField;
    }

    public List<String> getTableFillInsertOrUpdateField() {
        return tableFillInsertOrUpdateField;
    }

    public void setTableFillInsertOrUpdateField(List<String> tableFillInsertOrUpdateField) {
        this.tableFillInsertOrUpdateField = tableFillInsertOrUpdateField;
    }

    //// ?????????????????????
    //private static final String JDBC_URL = "jdbc:mysql://localhost:3306/fund?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    //private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    //private static final String USERNAME = "root";
    //private static final String PASSWORD = "123456";
    //
    //// ?????????????????????
    //private static final String AUTHOR = "Lypxc";
    //// ????????????????????????
    //private static final String PACKAGE_NAME = "io.github.panxiaochao.web";
    //private static final String PACKAGE_NAME_PATH = "io/github/panxiaochao";

    // ????????????
    private static final String ENTITY_TEMPLATE = "templates/entity.java.ftl";
    private static final String XML_TEMPLATE = "templates/mapper.xml.ftl";
    private static final String MAPPER_TEMPLATE = "templates/mapper.java.ftl";
    private static final String SERVICE_TEMPLATE = "templates/service.java.ftl";
    private static final String SERVICE_IMPL_TEMPLATE = "templates/serviceImpl.java.ftl";
    private static final String CONTROLLER_TEMPLATE = "templates/controller.java.ftl";

    public void generator() {
        // ????????????
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor(author);
        globalConfig.setOpen(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        // gc.setSwagger2(true); ???????????? Swagger2 ??????
        globalConfig.setFileOverride(true);
        // ????????????
        globalConfig.setIdType(idType);
        System.out.println("***????????????***");

        // ???????????????
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setUrl(url);
        // dsc.setSchemaName("public");
        dataSource.setDriverName(driverName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDbType(dbType);
        System.out.println("***???????????????***");

        // ?????????
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        packageConfig.setParent(packageName);
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        packageConfig.setService("service");
        packageConfig.setMapper("mapper");
        System.out.println("***?????????***");

        // ???????????????
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        System.out.println("***???????????????***");

        // ????????????????????? freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // ????????????????????? velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // ?????????????????????
        List<FileOutConfig> focList = new ArrayList<>();
        // ?????????????????????????????????
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // ???????????????????????? ??? ????????? Entity ????????????????????????????????? xml ????????????????????????????????????
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper"
                        + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        System.out.println("***?????????????????????***");

        // ????????????
        TemplateConfig templateConfig = new TemplateConfig();
        // ???????????????????????????
        // ?????????????????????????????????????????????null??????????????????????????????
        // templateConfig.setEntity("...");
        // templateConfig.setService(null);
        // templateConfig.setController(null);
        // templateConfig.setServiceImpl(null);
        // ?????????????????????????????? copy ?????? mybatis-plus/src/main/resources/templates ?????????????????????
        // ????????????????????? src/main/resources/templates ?????????, ???????????????????????????????????????
        // ??????????????????????????? ???????????????????????????????????? ???????????????????????? ??????????????????????????????
        // ?????????github???????????????
        /** https://github.com/baomidou/mybatis-plus/tree/3.0/mybatis-plus-generator/src/main/resources/templates */
        // templateConfig.setEntity("/templates/entity.java");
        templateConfig.setXml(null);
        System.out.println("***????????????***");

        // ????????????
        StrategyConfig strategy = new StrategyConfig();
        // ??????
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // ??????
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // strategy.setEntityLombokModel(true);
        // RestController??????
        strategy.setRestControllerStyle(true);
        // ????????????Entity
        // strategy.setSuperEntityClass("com.springboot.ssm.common.BaseEntity");
        // ??????????????????????????????
        // strategy.setSuperEntityColumns("id");
        // ????????????Controller
        // strategy.setSuperControllerClass("com.springboot.ssm.common.BaseController");
        // ?????????????????????????????????
        strategy.setInclude(tableName.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        // strategy.setTablePrefix(scanner("????????????") + "_");
        // ??????????????????????????????
        List<TableFill> tableFillList = new ArrayList<>();
        if (tableFillInsertField != null && !tableFillInsertField.isEmpty()) {
            for (String name : tableFillInsertField) {
                tableFillList.add(new TableFill(name, FieldFill.INSERT));
            }
        }
        if (tableFillInsertOrUpdateField != null && !tableFillInsertOrUpdateField.isEmpty()) {
            for (String name : tableFillInsertOrUpdateField) {
                tableFillList.add(new TableFill(name, FieldFill.INSERT_UPDATE));
            }
        }
        strategy.setTableFillList(tableFillList);
        System.out.println("***????????????***");

        // ======= ???????????? =======
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSource);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.setStrategy(strategy);
        autoGenerator.setCfg(cfg);
        autoGenerator.setTemplate(templateConfig);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
        System.out.println("***????????????***");
    }
}
