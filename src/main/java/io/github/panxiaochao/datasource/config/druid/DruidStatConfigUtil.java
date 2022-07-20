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

import com.alibaba.druid.filter.stat.StatFilter;
import io.github.panxiaochao.datasource.utils.DsConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Druid监控配置工具类
 *
 * @author Lypxc
 */
public final class DruidStatConfigUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidStatConfigUtil.class);

    private static final Map<String, Method> METHODS = DsConfigUtil.getSetterMethods(StatFilter.class);

    static {
        try {
            METHODS.put("dbType", StatFilter.class.getDeclaredMethod("setDbType", String.class));
        } catch (Exception ignore) {
        }
    }

    /**
     * 根据当前的配置和全局的配置生成druid防火墙配置
     *
     * @param c 当前配置
     * @param g 全局配置
     * @return StatFilter
     */
    public static StatFilter toStatFilter(Map<String, Object> c, Map<String, Object> g) {
        StatFilter filter = new StatFilter();
        Map<String, Object> map = DsConfigUtil.mergeConfig(c, g);
        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = DsConfigUtil.lineToUpper(item.getKey());
            Method method = METHODS.get(key);
            if (method != null) {
                try {
                    method.invoke(filter, item.getValue());
                } catch (Exception e) {
                    LOGGER.warn("druid stat set param {} error", key, e);
                }
            } else {
                LOGGER.warn("druid stat does not have param {}", key);
            }
        }
        return filter;
    }
}
