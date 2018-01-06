package com.jandrews.temp.jpa;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class PersistenceLifecycleManager {
    private static final String DATASOURCE_CONTEXT = "java:comp/env/jdbc/MyDB";

    private Logger log = LoggerFactory.getLogger(this.getClass());

    Flyway flyway=new Flyway();
    EntityManagerFactory entityManagerFactory;

    PersistenceLifecycleManager() {
        log.info("PersistenceLifecycleManager instantiated");
    }

    @Produces
    public EntityManagerFactory createEntityManagerFactory() {
        log.trace("createEntityManagerFactory()");
        if (entityManagerFactory == null) {
            log.info("initializing JPA data source");
            entityManagerFactory = Persistence.createEntityManagerFactory("com.mobileiron.metrics");
        }
        return entityManagerFactory;
    }

    public void closeEntityManagerFactory(@Disposes EntityManagerFactory emFactory) {
        log.trace("closeEntityManagerFactory(...)");
        emFactory.close();
        if (entityManagerFactory == emFactory) {
            entityManagerFactory = null;
        }
    }

    @Produces
    private EntityManager createEntityManager(EntityManagerFactory emFactory) {
        log.trace("createEntityManager(...)");
        return emFactory.createEntityManager();
    }

    void closeEntityManager(@Disposes EntityManager em) {
        log.trace("closeEntityManager(...)");
        em.close();
    }

    @PostConstruct
    public void initialize() {
        try {
            log.info("upgrading database (if required)");
            updateDatabaseConfiguration();
        } catch (Exception e) {
            log.error("Unable to initialize JPA", e);
        }
    }

    private void updateDatabaseConfiguration() {
        flyway.setDataSource(getDataSource());
        flyway.migrate();
    }

    private DataSource getDataSource() {
        DataSource result = null;
        try {
            Context initialContext = new InitialContext();
            log.info("found the following namespaces in initialContext: {}", initialContext.getNameInNamespace());

            result = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
            if (result == null) {
                log.error("Failed to lookup datasource.", new IllegalStateException());
            }
        } catch (NamingException e) {
            log.error("Cannot get connection: ", e);
        }
        return result;
    }
}
