package com.skt.classic.web.template.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class
        })
})
public class PageableInterceptor implements Interceptor {
    private final int MAPPED_STATEMENT_INDEX = 0;
    private final int PARAMETER_INDEX = 1;
    private final int ROWBOUNDS_INDEX = 2;

    private final Logger logger = LoggerFactory.getLogger(PageableInterceptor.class);

    private Dialect dialect;

    @Override
    public Object intercept(final Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        final Object parameter = queryArgs[PARAMETER_INDEX];
        boolean basePageable = isBasePageable(parameter),
                mapperPageable = isMapperPageable(parameter);

        if( basePageable || mapperPageable) {
            PageableParameterHandler<?> parameterHandler = basePageable ? new BaseParameterHandler(parameter): new MapperParameterHandler((ParamMap<?>)parameter);
            Object originParameter = parameterHandler.getOriginParameter();
            Pageable pageable = parameterHandler.getPageable();

            final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
            final BoundSql boundSql = ms.getBoundSql(originParameter);

            String sql = StringUtils.trim(boundSql.getSql()).replaceFirst(";$", StringUtils.EMPTY);
            int total = queryTotal(sql, ms, boundSql);
            PageableHolder.setCount(total);
            String pagingSQL = dialect.getPagingSQL(sql, pageable);

            queryArgs[PARAMETER_INDEX] = originParameter;
            queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms, boundSql, pagingSQL);
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);

        }

        return invocation.proceed();
    }

    private boolean isBasePageable(final Object parameter) {
        if(parameter != null) {
            if(parameter instanceof Pageable || parameter instanceof PageableParameter) {
                return true;
            }
        }

        return false;
    }

    private boolean isMapperPageable(final Object parameter) {
        if(parameter != null) {
            if(parameter instanceof ParamMap) {
                ParamMap<?> paramMap = (ParamMap<?>) parameter;
                @SuppressWarnings("unchecked")
                Collection<Object> values = (Collection<Object>) paramMap.values();
                return values.stream().anyMatch(o -> o instanceof Pageable);
            }
        }

        return false;
    }

    @Override
    public Object plugin(final Object target) {
        if(Executor.class.isAssignableFrom(target.getClass())) {
            return Plugin.wrap(target, this);
        }

        return target;
    }

    @Override
    public void setProperties(final Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        try {
            Dialect dialect = (Dialect) Class.forName(dialectClass).newInstance();
            setDialect(dialect);
        } catch (Exception e) {
            throw new IllegalArgumentException("dialectClass를 찾을수 없습니다. - " + dialectClass, e);
        }
    }

    public Dialect getDialect() {
        return this.dialect;
    }

    public void setDialect(final Dialect dialect) {
        this.dialect = dialect;
    }

    private Object copyFromNewSql(final MappedStatement ms, final BoundSql boundSql, final String sql) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
        return copyFromMappedStatement(ms, new BoundSqlSource(newBoundSql));
    }

    private Object copyFromMappedStatement(final MappedStatement ms, final SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());

        String[] keys = ms.getKeyProperties();
        if(keys != null) {
            Stream.of(keys)
                    .reduce((t, s) -> t += "," + s)
                    .ifPresent(k -> builder.keyProperty(k));
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());

        return builder.build();
    }

    class BoundSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSource(final BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(final Object parameterObject) {
            return boundSql;
        }

    }

    private int queryTotal(final String sql, final MappedStatement mappedStatement, final BoundSql boundSql) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totalCount = 0;

        try {
            conn = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            String countSql = this.dialect.getCountSQL(sql);
            if(logger.isDebugEnabled()) {
                logger.debug("countSQL: {} => {}", mappedStatement.getId(), countSql);
            }

            stmt = conn.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(stmt, mappedStatement, countBoundSql, boundSql.getParameterObject());

            rs = stmt.executeQuery();
            if(rs.next()) {
                totalCount = rs.getInt(1);
            }
        }finally {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        return totalCount;
    }

    private void setParameters(final PreparedStatement ps, final MappedStatement mappedStatement, final BoundSql boundSql, final Object parameter) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameter, boundSql);
        parameterHandler.setParameters(ps);
    }

    private BoundSql copyFromBoundSql(final MappedStatement ms, final BoundSql boundSql, final String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        boundSql.getParameterMappings().stream()
                .filter(mapping -> boundSql.hasAdditionalParameter(mapping.getProperty()))
                .forEach(mapping -> {
                    String prop = mapping.getProperty();
                    newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                });

        return newBoundSql;
    }
}
