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
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.UUID;
import redis.clients.jedis.JedisPubSub;

public class CommandSubscriber extends JedisPubSub {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final Admin admin;

  public CommandSubscriber(Admin admin) {
    this.admin = admin;
  }

  @Override
  public void onMessage(String channel, String message) {
    Topics topic = Topics.valueOf(channel);
    switch (topic) {
      case STUB_CREATE:
        stubCreate(message);
        break;
      case STUB_UPDATE:
        stubUpdate(message);
        break;
      case STUB_DELETE:
        stubDelete(message);
        break;
      case SCENARIO_SET:
        scenarioSet(message);
        break;
      case SCENARIO_RESET:
        scenarioReset(message);
        break;
      case MAPPINGS_RESET:
        mappingsReset();
        break;
      case MAPPINGS_RESET_DEFAULT:
        resetToDefaultMappings();
        break;
      case REMOVE_SERVE_EVENT:
        removeServeEvent(message);
        break;
      case ALL_RESET:
        resetAll();
        break;
      case REQUESTS_RESET:
        resetRequests();
        break;
      default:
        break;
    }
  }

  private void scenarioReset(String message) {
    if (RedisCommandPublisher.VALUE_ALL.equals(message)) {
      admin.resetScenarios();
    } else {
      admin.resetScenario(message);
    }
  }

  private void resetRequests() {
    admin.resetRequests();
  }

  private void resetAll() {
    admin.resetAll();
  }

  private void removeServeEvent(String message) {
    admin.removeServeEvent(UUID.fromString(message));
  }

  private void resetToDefaultMappings() {
    admin.resetToDefaultMappings();
  }

  private void mappingsReset() {
    admin.resetMappings();
  }

  private void scenarioSet(String message) {
    ScenarioMessage s = getScenarioMessage(message);
    admin.setScenarioState(s.getScenarioName(), s.getScenarioState());
  }

  private void stubDelete(String message) {
    admin.removeStubMapping(UUID.fromString(message));
  }

  private void stubUpdate(String message) {
    StubMapping stub = getStubMapping(message);
    admin.editStubMapping(stub);
  }

  private void stubCreate(String message) {
    StubMapping stub = getStubMapping(message);
    admin.addStubMapping(stub);
  }

  private ScenarioMessage getScenarioMessage(String message) {
    try {
      return objectMapper.readValue(message, ScenarioMessage.class);
    } catch (JsonProcessingException e) {
      throw new JsonConversionException(e);
    }
  }

  private StubMapping getStubMapping(String message) {
    try {
      return objectMapper.readValue(message, StubMapping.class);
    } catch (JsonProcessingException e) {
      throw new JsonConversionException(e);
    }
  }
}
