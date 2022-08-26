package edu.iu.uits.lms.ltitoolmanager.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration("LtiToolManagerDbConfig")
@EnableJpaRepositories(
        entityManagerFactoryRef = "LtiToolManagerEntityMgrFactory",
        transactionManagerRef = "LtiToolManagerTransactionMgr",
        basePackages = {"edu.iu.uits.lms.ltitoolmanager.repositories"})

@EnableTransactionManagement
public class PostgresDBConfig {

    @Primary
    @Bean(name = "LtiToolManagerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "LtiToolManagerEntityMgrFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean ltiToolManagerEntityMgrFactory(
            final EntityManagerFactoryBuilder builder,
            @Qualifier("LtiToolManagerDataSource") final DataSource dataSource) {
        // dynamically setting up the hibernate properties for each of the datasource.
        final Map<String, String> properties = new HashMap<>();
        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("edu.iu.uits.lms.ltitoolmanager.model")
                .build();
    }

    @Bean(name = "LtiToolManagerTransactionMgr")
    @Primary
    public PlatformTransactionManager ltiToolManagerTransactionMgr(
            @Qualifier("LtiToolManagerEntityMgrFactory") final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
