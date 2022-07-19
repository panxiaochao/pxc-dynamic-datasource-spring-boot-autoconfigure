package io.github.panxiaochao.datasource.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * (mybatis-plus实现自动填充数据)
 *
 * @author Mr_LyPxc
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    public MyMetaObjectHandler() {
    }

    /**
     * fieldName 指的是实体类的属性名,而不是数据库的字段名
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createTime", Long.class, System.currentTimeMillis());
        this.strictInsertFill(metaObject, "createtime", Long.class, System.currentTimeMillis());
        this.strictInsertFill(metaObject, "updatetime", Long.class, System.currentTimeMillis());
        this.strictInsertFill(metaObject, "updateTime", Long.class, System.currentTimeMillis());
        // 起始版本 3.3.3(推荐)
        // LocalDateTime
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "createtime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "updatetime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
        // LocalDate
        this.strictInsertFill(metaObject, "createTime", () -> LocalDate.now(), LocalDate.class);
        this.strictInsertFill(metaObject, "createtime", () -> LocalDate.now(), LocalDate.class);
        this.strictInsertFill(metaObject, "updatetime", () -> LocalDate.now(), LocalDate.class);
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDate.now(), LocalDate.class);
        // Date
        this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "createtime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "updatetime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "updateTime", () -> new Date(), Date.class);
    }

    /**
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.3(推荐)
        this.strictUpdateFill(metaObject, "updatetime", System::currentTimeMillis, Long.class);
        this.strictUpdateFill(metaObject, "updateTime", System::currentTimeMillis, Long.class);
        // 起始版本 3.3.3(推荐)
        // LocalDateTime
        this.strictUpdateFill(metaObject, "updatetime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
        // LocalDate
        this.strictUpdateFill(metaObject, "updatetime", () -> LocalDate.now(), LocalDate.class);
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDate.now(), LocalDate.class);
        // Date
        this.strictUpdateFill(metaObject, "updatetime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "updateTime", () -> new Date(), Date.class);
    }
}
