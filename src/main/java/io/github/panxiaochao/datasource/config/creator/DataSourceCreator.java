/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.panxiaochao.datasource.config.creator;

import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;

import javax.sql.DataSource;

/**
 * {@code DataSourceBuilder}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public interface DataSourceCreator {


    /**
     * 构建DataSource
     *
     * @param dataSourceProperty
     * @return
     */
    DataSource buildDataSource(DataSourceProperty dataSourceProperty);


    /**
     * 是否支持
     *
     * @param dataSourceProperty
     * @return
     */
    boolean support(DataSourceProperty dataSourceProperty);
}
