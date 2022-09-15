package io.github.panxiaochao.datasource.core.route;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@code AbstractCustomRoutingDataSource}
 * <p> 重写路由类
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public abstract class AbstractDynamicRoutingDataSource extends AbstractDataSource {

    /**
     * 连接对象
     *
     * @return
     */
    protected abstract DataSource determineTargetDataSource();

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return iface.isInstance(this) ? (T) this : determineTargetDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iFace) throws SQLException {
        return iFace.isInstance(this) || determineTargetDataSource().isWrapperFor(iFace);
    }
}
