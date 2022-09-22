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
package io.github.panxiaochao.datasource.config.druid;

import com.alibaba.druid.filter.logging.LogFilter;
import io.github.panxiaochao.datasource.utils.DsConfigUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Druid日志配置工具类
 *
 * @author Lypxc
 */
public final class DruidLogConfigUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidLogConfigUtil.class);

    private static final Map<String, Method> METHODS = DsConfigUtil.getSetterMethods(LogFilter.class);

    /**
     * 根据当前的配置和全局的配置生成druid的日志filter
     *
     * @param c 当前配置
     * @param g 全局配置
     * @return 日志filter
     */
    public static LogFilter initFilter(Class<? extends LogFilter> clazz, Map<String, Object> c, Map<String, Object> g) {
        try {
            LogFilter filter = clazz.newInstance();
            Map<String, Object> params = DsConfigUtil.mergeConfig(c, g);
            for (Map.Entry<String, Object> item : params.entrySet()) {
                String key = DsConfigUtil.lineToUpper(item.getKey());
                Method method = METHODS.get(key);
                if (method != null) {
                    try {
                        method.invoke(filter, item.getValue());
                    } catch (Exception e) {
                        LOGGER.warn("druid {} set param {} error", clazz.getName(), key, e);
                    }
                } else {
                    LOGGER.warn("druid {} does not have param {}", clazz.getName(), key);
                }
            }
            return filter;
        } catch (Exception e) {
            return null;
        }
    }
}
