package br.com.klein.config;

import br.com.klein.model.Chave;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

@ApplicationScoped
public class RedisCache {

    private final ValueCommands<String, Chave> commands;

    public RedisCache(RedisDataSource ds) {
        this.commands = ds.value(Chave.class);
    }

    public Chave get(String key) {
        return commands.get(key);
    }
    public void set(String key, Chave cached) {
        commands.set(key, cached, new SetArgs().ex(Duration.ofMinutes(30)));
    }

    public void evict(String key) {
        commands.getdel(key);
    }

    public Chave getOrSetIfAbsent(String key, Supplier<Chave> cachedObj) {
        Chave cached = get(key);
        if (Objects.nonNull(cached)) {
            return cached;
        } else {
            Chave result = cachedObj.get();
            set(key, result);

            return result;
        }
    }
}
