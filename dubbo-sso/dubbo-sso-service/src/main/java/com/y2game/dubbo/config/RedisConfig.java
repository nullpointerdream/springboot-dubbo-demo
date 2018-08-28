package com.y2game.dubbo.config;

import com.y2game.common.jedis.JedisClientPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {

    @Value("${redis.maxIdle}")
    private Integer maxIdle;

    @Value("${redis.maxTotal}")
    private Integer maxTotal;

    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;


    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.cluster.max-redirects}")
    private Integer mmaxRedirectsac;


    @Value("${redis.hostName}")
    private String redis_ip;
    @Value("${redis.port}")
    private Integer redis_port;
    @Value("${redis.timeout}")
    private Integer timeout;

    private JedisPool jedisPool;
    private JedisClientPool jedisClientPool;


    @Bean(name = "jedisPool")
    public JedisPool jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        jedisPool=new JedisPool(jedisPoolConfig,redis_ip,redis_port,timeout);
        return jedisPool;
    }


    @Bean
    @DependsOn("jedisPool")
    public JedisClientPool jedisClientPoolConfig() {
        jedisClientPool = new JedisClientPool();
        jedisClientPool.setJedisPool(jedisPool);
        return jedisClientPool;
    }

}
