package com.adwork.microservices.users.config;

import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.entity.UserRole;
import com.adwork.microservices.users.service.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;

@Configuration
public class UserAppConfig {

    @Bean
    CommandLineRunner initDatabase(IUserService service) {
        return args -> {
            if (service.countUsers() == 0) {
                service.addUser(new UserAccount("admin@adwork-ms.com", "*", UserRole.ADMIN));
                service.addUser(new UserAccount("user1@adwork-ms.com", "*", UserRole.USER));
                service.addUser(new UserAccount("manager1@adwork-ms.com", "*", UserRole.MANAGER));
                System.out.println("Users created:\n" + service.listUsers());
            }
        };
    }

    /*@Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter bean = new HibernateJpaVendorAdapter();
        bean.setDatabase(Database.H2);
        bean.setGenerateDdl(true);
        bean.setShowSql(true);
        return bean;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("jcg.zheng.demo.jpademo");
        return bean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
    */

}
