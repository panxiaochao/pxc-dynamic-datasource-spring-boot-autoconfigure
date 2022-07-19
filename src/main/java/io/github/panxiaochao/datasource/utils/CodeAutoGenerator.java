package io.github.panxiaochao.datasource.utils;//package io.github.panxiaochao.db.utils;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.annotation.FieldFill;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.po.TableFill;
//import com.baomidou.mybatisplus.generator.config.po.TableInfo;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Lypxc
// */
//public class CodeAutoGenerator {
//    private String url;
//    private String driverName;
//    private String username;
//    private String password;
//    private String author = "pxc creator";
//    private String packageName;
//    private String moduleName;
//    private String tableName;
//    private DbType dbType = DbType.MYSQL;
//    private IdType idType = IdType.AUTO;
//    private List<String> tableFillInsertField;
//    private List<String> tableFillInsertOrUpdateField;
//    private List<FileOutConfig> fileOutConfigList;
//
//    public CodeAutoGenerator(String url, String driverName, String username,
//                             String password, String author, String packageName,
//                             String moduleName, String tableName, DbType dbType, IdType idType,
//                             List<String> tableFillInsertField, List<String> tableFillInsertOrUpdateField,
//                             List<FileOutConfig> fileOutConfigList) {
//        this.url = url;
//        this.driverName = driverName;
//        this.username = username;
//        this.password = password;
//        this.author = author;
//        this.packageName = packageName;
//        this.moduleName = moduleName;
//        this.tableName = tableName;
//        this.dbType = dbType;
//        this.idType = idType;
//        this.tableFillInsertField = tableFillInsertField;
//        this.tableFillInsertOrUpdateField = tableFillInsertOrUpdateField;
//        this.fileOutConfigList = fileOutConfigList;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getDriverName() {
//        return driverName;
//    }
//
//    public void setDriverName(String driverName) {
//        this.driverName = driverName;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getPackageName() {
//        return packageName;
//    }
//
//    public void setPackageName(String packageName) {
//        this.packageName = packageName;
//    }
//
//    public String getModuleName() {
//        return moduleName;
//    }
//
//    public void setModuleName(String moduleName) {
//        this.moduleName = moduleName;
//    }
//
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(String tableName) {
//        this.tableName = tableName;
//    }
//
//    public DbType getDbType() {
//        return dbType;
//    }
//
//    public void setDbType(DbType dbType) {
//        this.dbType = dbType;
//    }
//
//    public IdType getIdType() {
//        return idType;
//    }
//
//    public void setIdType(IdType idType) {
//        this.idType = idType;
//    }
//
//    public List<String> getTableFillInsertField() {
//        return tableFillInsertField;
//    }
//
//    public void setTableFillInsertField(List<String> tableFillInsertField) {
//        this.tableFillInsertField = tableFillInsertField;
//    }
//
//    public List<String> getTableFillInsertOrUpdateField() {
//        return tableFillInsertOrUpdateField;
//    }
//
//    public void setTableFillInsertOrUpdateField(List<String> tableFillInsertOrUpdateField) {
//        this.tableFillInsertOrUpdateField = tableFillInsertOrUpdateField;
//    }
//
//    public List<FileOutConfig> getFileOutConfigList() {
//        return fileOutConfigList;
//    }
//
//    public void setFileOutConfigList(List<FileOutConfig> fileOutConfigList) {
//        this.fileOutConfigList = fileOutConfigList;
//    }
//
//    private static final String ENTITY_TEMPLATE = "/templates/entity.java.ftl";
//    private static final String XML_TEMPLATE = "/templates/mapper.xml.ftl";
//    private static final String MAPPER_TEMPLATE = "/templates/mapper.java.ftl";
//    private static final String SERVICE_TEMPLATE = "/templates/service.java.ftl";
//    private static final String SERVICE_IMPL_TEMPLATE = "/templates/serviceImpl.java.ftl";
//    private static final String CONTROLLER_TEMPLATE = "/templates/controller.java.ftl";
//
//    public void generator() {
//        // 全局配置
//        GlobalConfig globalConfig = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        globalConfig.setOutputDir(projectPath + "/src/main/java");
//        globalConfig.setAuthor(author);
//        globalConfig.setOpen(false);
//        globalConfig.setBaseResultMap(true);
//        globalConfig.setBaseColumnList(true);
//        // gc.setSwagger2(true); 实体属性 Swagger2 注解
//        globalConfig.setFileOverride(true);
//        // 主键自增
//        globalConfig.setIdType(idType);
//        System.out.println("***全局配置***");
//
//        // 数据源配置
//        DataSourceConfig dataSource = new DataSourceConfig();
//        dataSource.setUrl(url);
//        // dsc.setSchemaName("public");
//        dataSource.setDriverName(driverName);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setDbType(dbType);
//        System.out.println("***数据源配置***");
//
//        // 包配置
//        PackageConfig packageConfig = new PackageConfig();
//        packageConfig.setModuleName(moduleName);
//        packageConfig.setParent(packageName);
//        packageConfig.setController("controller");
//        packageConfig.setEntity("entity");
//        packageConfig.setService("service");
//        packageConfig.setMapper("mapper");
//        System.out.println("***包配置***");
//
//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };
//        System.out.println("***自定义配置***");
//
//        // 自定义输出配置
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(XML_TEMPLATE) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                String packageNamePath = packageName.substring(packageName.lastIndexOf(".") + 1);
//                return projectPath + "/src/main/resources/mapper/"
//                        + packageNamePath + "/"
//                        + packageConfig.getModuleName() + "/"
//                        + tableInfo.getEntityName() + "Mapper"
//                        + StringPool.DOT_XML;
//            }
//        });
//
//        // controller文件输出
//        focList.add(new FileOutConfig(CONTROLLER_TEMPLATE) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return projectPath + "/src/main/java/"
//                        + packageName.replaceAll("\\.", "/") + "/"
//                        + packageConfig.getModuleName() + "/"
//                        + packageConfig.getController() + "/"
//                        + tableInfo.getControllerName()
//                        + StringPool.DOT_JAVA;
//            }
//        });
//
//        cfg.setFileOutConfigList(focList);
//        System.out.println("***自定义输出配置***");
//
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
//        // 配置自定义输出模板
//        // 不需要其他的类型时，直接设置为null就不会成对应的模版了
//        // templateConfig.setEntity("...");
//        // templateConfig.setService(null);
//        // templateConfig.setController(null);
//        // templateConfig.setServiceImpl(null);
//        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
//        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也
//        // 可以自定义模板名称 只要放到目录下，名字不变 就会采用这个模版 下面这句有没有无所谓
//        // 模版去github上看地址：
//        /** https://github.com/baomidou/mybatis-plus/tree/3.0/mybatis-plus-generator/src/main/resources/templates */
//        // templateConfig.setEntity("/templates/entity.java");
//        templateConfig.setXml(null);
//        System.out.println("***模板配置***");
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        // 表名
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        // 字段
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        // strategy.setEntityLombokModel(true);
//        // RestController控制
//        strategy.setRestControllerStyle(true);
//        // 公共父类Entity
//        // strategy.setSuperEntityClass("com.springboot.ssm.common.BaseEntity");
//        // 写于父类中的公共字段
//        // strategy.setSuperEntityColumns("id");
//        // 公共父类Controller
//        // strategy.setSuperControllerClass("com.springboot.ssm.common.BaseController");
//        // 表名，多个英文逗号分割
//        strategy.setInclude(tableName.split(","));
//        strategy.setControllerMappingHyphenStyle(true);
//        // strategy.setTablePrefix(scanner("表名前缀") + "_");
//        // 写于父类中的公共字段
//        List<TableFill> tableFillList = new ArrayList<>();
//        if (tableFillInsertField != null && !tableFillInsertField.isEmpty()) {
//            for (String name : tableFillInsertField) {
//                tableFillList.add(new TableFill(name, FieldFill.INSERT));
//            }
//        }
//        if (tableFillInsertOrUpdateField != null && !tableFillInsertOrUpdateField.isEmpty()) {
//            for (String name : tableFillInsertOrUpdateField) {
//                tableFillList.add(new TableFill(name, FieldFill.INSERT_UPDATE));
//            }
//        }
//        strategy.setTableFillList(tableFillList);
//        System.out.println("***策略配置***");
//
//        // ======= 执行生成 =======
//        AutoGenerator autoGenerator = new AutoGenerator();
//        autoGenerator.setGlobalConfig(globalConfig);
//        autoGenerator.setDataSource(dataSource);
//        autoGenerator.setPackageInfo(packageConfig);
//        autoGenerator.setStrategy(strategy);
//        autoGenerator.setCfg(cfg);
//        autoGenerator.setTemplate(templateConfig);
//        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
//        autoGenerator.execute();
//        System.out.println("***执行成功***");
//    }
//}
