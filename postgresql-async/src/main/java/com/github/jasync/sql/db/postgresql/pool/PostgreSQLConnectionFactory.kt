package com.github.jasync.sql.db.postgresql.pool

import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.pool.ConnectionFactory
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import com.github.jasync.sql.db.toDebugString
import mu.KotlinLogging
import java.util.concurrent.CompletableFuture

private val logger = KotlinLogging.logger {}

/**
 *
 * Object responsible for creating connection instances.
 * @param configurationProvider a ConfigurationProvider that provides a valid configuration.
 */
class PostgreSQLConnectionFactory(val configuration: Configuration) : ConnectionFactory<PostgreSQLConnection>() {

    override fun create(): CompletableFuture<PostgreSQLConnection> {
        return configuration.resolveCredentials()
            .thenCompose { credentials ->
                val completeConfiguration = configuration.copy(username = credentials.username, password = credentials.password)

                logger.debug {
                    "Creating PostgreSQL connection with configuration ${completeConfiguration.toDebugString()}"
                }
                val connection = PostgreSQLConnection(completeConfiguration)
                connection.connect()
            }
            .toCompletableFuture()
    }
}
