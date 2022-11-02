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

import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.UUID;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCommandPublisher implements CommandPublisher {

  static final String VALUE_ALL = "_*****all*****_";

  private static final int DELAY_IN_MILLIS = 200;

  private final JedisPool jedis;

  public RedisCommandPublisher(JedisPool jedis) {
    this.jedis = jedis;
  }

  @Override
  public boolean isNoOp() {
    return false;
  }

  @Override
  public void addStubMapping(StubMapping stubMapping) {
    String message = covertToString(stubMapping);
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.STUB_CREATE.toString(), message);
    }
    pause(8);
  }

  @Override
  public void editStubMapping(StubMapping stubMapping) {
    String message = covertToString(stubMapping);
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.STUB_UPDATE.toString(), message);
    }
    pause(8);
  }

  @Override
  public void resetAll() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.ALL_RESET.toString(), VALUE_ALL);
    }
    pause();
  }

  @Override
  public void resetRequests() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.REQUESTS_RESET.toString(), VALUE_ALL);
    }
    pause();
  }

  @Override
  public void resetToDefaultMappings() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.MAPPINGS_RESET_DEFAULT.toString(), VALUE_ALL);
    }
    pause();
  }

  @Override
  public void resetScenario(String name) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.SCENARIO_RESET.toString(), name);
    }
    pause();
  }

  @Override
  public void resetScenarios() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.SCENARIO_RESET.toString(), VALUE_ALL);
    }
    pause();
  }

  @Override
  public void resetMappings() {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.MAPPINGS_RESET.toString(), VALUE_ALL);
    }
    pause();
  }

  @Override
  public void removeServeEvent(UUID eventId) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.REMOVE_SERVE_EVENT.toString(), eventId.toString());
    }
    pause();
  }

  @Override
  public void removeStubMapping(UUID id) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.STUB_DELETE.toString(), id.toString());
    }
    pause();
  }

  @Override
  public void setScenarioState(String name, String state) {
    try (Jedis j = jedis.getResource()) {
      j.publish(Topics.SCENARIO_SET.toString(), covertToString(new ScenarioMessage(name, state)));
    }
    pause(3);
  }

  private String covertToString(Object object) {
    return Json.writeMin(object);
  }

  /** Allow propagating */
  private void pause() {
    pause(1);
  }

  private void pause(long multiplier) {
    try {
      Thread.sleep(DELAY_IN_MILLIS * multiplier);
    } catch (InterruptedException ignored) {
    }
  }
}
