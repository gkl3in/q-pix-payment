package br.com.klein.config;

import br.com.klein.model.Chave;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

@ApplicationScoped
public class RedisCache {

    private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(30);
    private final ValueCommands<String, Chave> commands;

    public RedisCache(RedisDataSource redisDataSource) {
        validateDataSource(redisDataSource);
        this.commands = redisDataSource.value(Chave.class);
    }

    public Optional<Chave> get(String key) {
        validateKey(key);
        return Optional.ofNullable(commands.get(key));
    }

    public void set(String key, Chave value) {
        set(key, value, DEFAULT_EXPIRATION);
    }

    public void set(String key, Chave value, Duration expiration) {
        validateKey(key);
        validateValue(value);
        validateExpiration(expiration);
        
        commands.set(key, value, new SetArgs().ex(expiration));
    }

    public Optional<Chave> evict(String key) {
        validateKey(key);
        return Optional.ofNullable(commands.getdel(key));
    }

    public Chave getOrSetIfAbsent(String key, Supplier<Chave> supplier) {
        validateKey(key);
        validateSupplier(supplier);

        return get(key).orElseGet(() -> {
            Chave value = supplier.get();
            validateValue(value);
            set(key, value);
            return value;
        });
    }

    private void validateDataSource(RedisDataSource redisDataSource) {
        if (redisDataSource == null) {
            throw new IllegalArgumentException("Redis data source cannot be null");
        }
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }

    private void validateValue(Chave value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
    }

    private void validateExpiration(Duration expiration) {
        if (expiration == null || expiration.isNegative() || expiration.isZero()) {
            throw new IllegalArgumentException("Expiration must be a positive duration");
        }
    }

    private void validateSupplier(Supplier<Chave> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
    }
}