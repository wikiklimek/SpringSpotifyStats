package com.spotify.wrapped;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractIntegrationTest {

    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgres;

    @ServiceConnection
    protected static final MongoDBContainer mongo;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16-alpine");
        postgres.start();

        mongo = new MongoDBContainer("mongo:7.0");
        mongo.start();
    }
}