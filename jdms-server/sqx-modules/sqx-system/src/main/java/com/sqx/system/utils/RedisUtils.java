package com.sqx.system.utils;

import com.google.gson.Gson;
import com.sqx.common.redis.utils.RedisKeyPrefix;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtils {

    @Autowired
    private RedissonClient redissonClient;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//    @Autowired
//    private ValueOperations<String, String> valueOperations;
//    @Autowired
//    private HashOperations<String, String, Object> hashOperations;
//    @Autowired
//    private ListOperations<String, Object> listOperations;
//    @Autowired
//    private SetOperations<String, Object> setOperations;
//    @Autowired
//    private ZSetOperations<String, Object> zSetOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    private final static Gson Gson = new Gson();

    public void set(String key, Object value, long expire) {
        RBucket<String> bucket = redissonClient.getBucket(RedisKeyPrefix.key(key));
        String jsonValue = toJson(value);
//        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            bucket.set(jsonValue, expire, TimeUnit.SECONDS);
        } else {
            bucket.set(jsonValue);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        RBucket<String> bucket = redissonClient.getBucket(RedisKeyPrefix.key(key));
        String value = bucket.get();
        if (value == null) {
            return null;
        }
        // 如果需要刷新过期时间
        if (expire != NOT_EXPIRE) {
            bucket.expire(expire, TimeUnit.SECONDS);
        }
        return fromJson(value, clazz);
    }


    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        RBucket<String> bucket = redissonClient.getBucket(RedisKeyPrefix.key(key));
        String value = bucket.get();
        if (value != null && expire != NOT_EXPIRE) {
            bucket.expire(expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redissonClient.getKeys().delete(RedisKeyPrefix.key(key));
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return Gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return Gson.fromJson(json, clazz);
    }
}
