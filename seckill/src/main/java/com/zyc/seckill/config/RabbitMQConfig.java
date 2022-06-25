package com.zyc.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zyc
 * @description: RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue() {
        return new Queue("queue", true);// true持久化
    }

}
