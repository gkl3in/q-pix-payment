package br.com.klein.config;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

import br.com.klein.model.LinhaDigitavel;

@ApplicationScoped
public class LinhaDigitavelCache {

    private final ValueCommands<String, LinhaDigitavel> commands;

    public LinhaDigitavelCache(RedisDataSource ds) {
        this.commands = ds.value(LinhaDigitavel.class);
    }

    public LinhaDigitavel get(String key) {
        return commands.get(key);
    }
    public void set(String key, LinhaDigitavel linhaDigitavel) {
        commands.set(key, linhaDigitavel, new SetArgs().ex(Duration.ofMinutes(30)));
    }

    public LinhaDigitavel getOrSetIfAbsent(String key,
                                  Supplier<LinhaDigitavel> cachedObj) {

        var cached = get(key);
        if (Objects.nonNull(cached)) {
            return cached;
        } else {
            var result = cachedObj.get();
            set(key, result);
            return result;
        }
    }
}
