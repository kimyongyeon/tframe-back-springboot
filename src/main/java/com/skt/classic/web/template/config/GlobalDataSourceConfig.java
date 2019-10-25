package com.skt.classic.web.template.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class GlobalDataSourceConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    /** @description 데이터소스 빈 정의 */
    @Bean(name = "datasource2")
    @ConfigurationProperties(prefix = "skt.multi-datasource.datasource2")
    public DataSource dataSource2(DataSourceProperties properties) {
        return (DataSource) properties.initializeDataSourceBuilder().type(org.apache.commons.dbcp2.BasicDataSource.class).build();
    }

    /** @description myBatis 속성 빈 정의 */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate2(@Qualifier("datasource2") DataSource datasource2) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setTypeAliasesPackage("com.skt.classic.web.template.dto");
        factory.setConfigLocation(resourceLoader.getResource("classpath:persistence/mybatis/main/mybatis-config.xml"));
        factory.setDataSource(datasource2);
        factory.setVfs(SpringBootVFS.class);
        return new SqlSessionTemplate(factory.getObject());
    }

    /** @description 트랜잭션 빈 정의 */
    @Bean
    public DataSourceTransactionManager transactionManager2(@Qualifier("datasource2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
