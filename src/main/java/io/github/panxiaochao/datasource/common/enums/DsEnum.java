package io.github.panxiaochao.datasource.common.enums;

import lombok.Getter;

/**
 * @author Lypxc
 * @since 2021/12/25 11:57
 */
@Getter
public enum DsEnum {
    /**
     * 数据源源定义
     */
    MASTER("master"), DB1("db1"), DB2("db2"), DB3("db3"), DB4("db4"), DB5("db5");

    private final String name;

    DsEnum(String name) {
        this.name = name;
    }
}
