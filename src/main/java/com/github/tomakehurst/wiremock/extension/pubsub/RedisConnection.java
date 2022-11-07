/*
 * Copyright (C) 2022 CWT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.extension.pubsub;

import java.time.Duration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

  private static volatile JedisPool jedisInstance;

  private RedisConnection() {}

  public static JedisPool getJedisInstance(String host, int port) {
    if (jedisInstance == null) {
      final JedisPoolConfig poolConfig = new JedisPoolConfig();
      poolConfig.setMaxTotal(10);
      poolConfig.setMaxIdle(8);
      poolConfig.setMinIdle(4);
      poolConfig.setTestOnCreate(true);
      poolConfig.setTestOnBorrow(true);
      poolConfig.setTestOnReturn(true);
      poolConfig.setTestWhileIdle(true);
      poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60));
      poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
      poolConfig.setNumTestsPerEvictionRun(3);
      poolConfig.setBlockWhenExhausted(true);

      synchronized (JedisPool.class) {
        if (jedisInstance == null) {
          jedisInstance = new JedisPool(poolConfig, host, port);
        }
      }
    }
    return jedisInstance;
  }
}
