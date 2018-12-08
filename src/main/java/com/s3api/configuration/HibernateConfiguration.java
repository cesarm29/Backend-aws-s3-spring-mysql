/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.configuration;

import java.util.Properties;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author cesar
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.s3api.configuration"})
public class HibernateConfiguration {
    
    enum HibernateDataConfiguration {

        /**
         * Dialecto para Hibernate.
         */
        HIBERNATE_DIALECT("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect"),
        /**
         * Mostrar consultas SQL en consola.
         */
        HIBERNATE_SHOW_SQL("hibernate.show_sql", "false"),
        /**
         * Formateo de consultas SQL.
         */
        HIBERNATE_FORMAT_SQL("hibernate.format_sql", "false");

        /**
         * Llave de la enumeraci&oacute;n.
         */
        private final String configKey;
        /**
         * Valor de la enumeraci&oacute;n.
         */
        private final String configValue;

        private HibernateDataConfiguration(String configKey, String configValue) {
            this.configKey = configKey;
            this.configValue = configValue;
        }

        public String getConfigKey() {
            return configKey;
        }

        public String getConfigValue() {
            return configValue;
        }
    };
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
    
    @Bean(destroyMethod = "")
    public DataSource dataSource() throws NamingException {
        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        return dsLookup.getDataSource("jdbc/s3");
    }
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(HibernateDataConfiguration.HIBERNATE_DIALECT.getConfigKey(), HibernateDataConfiguration.HIBERNATE_DIALECT.getConfigValue());
        properties.put(HibernateDataConfiguration.HIBERNATE_SHOW_SQL.getConfigKey(), HibernateDataConfiguration.HIBERNATE_SHOW_SQL.getConfigValue());
        properties.put(HibernateDataConfiguration.HIBERNATE_FORMAT_SQL.getConfigKey(), HibernateDataConfiguration.HIBERNATE_FORMAT_SQL.getConfigValue());
        return properties;
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }
}
