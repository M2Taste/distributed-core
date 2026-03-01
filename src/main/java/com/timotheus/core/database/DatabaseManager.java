package com.timotheus.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(String host, int port, String db, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" + host + ":" + port + "/" + db +
                        "?useSSL=false&serverTimezone=UTC"
        );
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("DistributedCore-Pool");

        this.dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void shutdown() {
        dataSource.close();
    }
}