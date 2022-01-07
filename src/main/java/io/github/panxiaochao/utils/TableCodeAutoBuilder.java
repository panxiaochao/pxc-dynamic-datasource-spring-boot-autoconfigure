package io.github.panxiaochao.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import org.springframework.util.Assert;

import java.util.List;

/**
 * {@code TableCodeAutoBuilder}
 * <p>代码自动生成器
 *
 * @author Lypxc
 * @since 2022/1/6
 */
public class TableCodeAutoBuilder {
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

    public TableCodeAutoBuilder url(String url) {
        Assert.notNull(url, "url is not null!");
        this.url = url;
        return this;
    }

    public TableCodeAutoBuilder driverName(String driverName) {
        Assert.notNull(driverName, "driverName is not null!");
        this.driverName = driverName;
        return this;
    }

    public TableCodeAutoBuilder username(String username) {
        Assert.notNull(username, "username is not null!");
        this.username = username;
        return this;
    }

    public TableCodeAutoBuilder password(String password) {
        Assert.notNull(password, "password is not null!");
        this.password = password;
        return this;
    }

    public TableCodeAutoBuilder author(String author) {
        this.author = author;
        return this;
    }

    public TableCodeAutoBuilder packageName(String packageName) {
        Assert.notNull(packageName, "packageName is not null!");
        this.packageName = packageName;
        return this;
    }

    public TableCodeAutoBuilder moduleName(String moduleName) {
        Assert.notNull(moduleName, "moduleName is not null!");
        this.moduleName = moduleName;
        return this;
    }

    public TableCodeAutoBuilder tableName(String tableName) {
        Assert.notNull(tableName, "tableName is not null!");
        this.tableName = tableName;
        return this;
    }

    public TableCodeAutoBuilder dbType(DbType dbType) {
        this.dbType = dbType;
        return this;
    }

    /**
     * 主键类型：
     * AUTO(0):"数据库ID自增"
     * NONE(1):"未设置主键类型"
     * INPUT(2):"用户输入ID (该类型可以通过自己注册自动填充插件进行填充)"
     * ASSIGN_ID(3):"全局唯一ID (idWorker)
     * ASSIGN_UUID(4):全局唯一ID (UUID)
     *
     * @param idType idType
     * @return TableCodeAutoBuilder
     */
    public TableCodeAutoBuilder idType(IdType idType) {
        this.idType = idType;
        return this;
    }

    public TableCodeAutoBuilder tableFillInsertField(List<String> tableFillInsertField) {
        this.tableFillInsertField = tableFillInsertField;
        return this;
    }

    public TableCodeAutoBuilder tableFillInsertOrUpdateField(List<String> tableFillInsertOrUpdateField) {
        this.tableFillInsertOrUpdateField = tableFillInsertOrUpdateField;
        return this;
    }

    public void build() {
        new CodeAutoGenerator(url, driverName, username,
                password, author, packageName,
                moduleName, tableName, dbType, idType,
                tableFillInsertField, tableFillInsertOrUpdateField).generator();
    }

}
