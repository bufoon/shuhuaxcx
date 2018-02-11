package com.rttx.commons.config;

import com.rttx.commons.config.prop.AsyncProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/9 18:03
 * @Desc: as follows.
 */

@Configuration
@ConditionalOnProperty(name = "rttx", havingValue = "async")
@EnableAsync
public class AsynConfig {

    @Bean
    @ConfigurationProperties(prefix = "rttx.async")
    public AsyncProperties asyncProperties(){
        return new AsyncProperties();
    }

    @Bean
    public Executor rttxAsync(AsyncProperties asyncProperties){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(asyncProperties.isAllowCoreThreadTimeOut());
        executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
