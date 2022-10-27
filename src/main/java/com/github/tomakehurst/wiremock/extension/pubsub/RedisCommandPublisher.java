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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.UUID;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCommandPublisher implements CommandPublisher {

  static final String VALUE_ALL = "_*****all*****_";

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final JedisPool jedis;

  public RedisCommandPublisher(JedisPool jedis) {
    this.jedis = jedis;
  }

  @Override
  public void addStubMapping(StubMapping stubMapping) {
    String message = covertToString(stubMapping);
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.STUB_CREATE.getTopic(), message);
    }
  }

  @Override
  public void editStubMapping(StubMapping stubMapping) {
    String message = covertToString(stubMapping);
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.STUB_UPDATE.getTopic(), message);
    }
  }

  @Override
  public void resetAll() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.ALL_RESET.getTopic(), VALUE_ALL);
    }
  }

  @Override
  public void resetRequests() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.REQUESTS_RESET.getTopic(), VALUE_ALL);
    }
  }

  @Override
  public void resetToDefaultMappings() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.MAPPINGS_RESET_DEFAULT.getTopic(), VALUE_ALL);
    }
  }

  @Override
  public void resetScenario(String name) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.SCENARIO_RESET.getTopic(), name);
    }
  }

  @Override
  public void resetScenarios() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.SCENARIO_RESET.getTopic(), VALUE_ALL);
    }
  }

  @Override
  public void resetMappings() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.MAPPINGS_RESET.getTopic(), VALUE_ALL);
    }
  }

  @Override
  public void removeServeEvent(UUID eventId) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.REMOVE_SERVE_EVENT.getTopic(), eventId.toString());
    }
  }

  @Override
  public void removeStubMapping(UUID id) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.STUB_DELETE.getTopic(), id.toString());
    }
  }

  @Override
  public void setScenarioState(String name, String state) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.SCENARIO_SET.getTopic(), covertToString(new ScenarioMessage(name, state)));
    }
  }

  private String covertToString(Object stubMapping) {
    try {
      return objectMapper.writeValueAsString(stubMapping);
    } catch (JsonProcessingException e) {
      throw new JsonConversionException(e);
    }
  }
}
