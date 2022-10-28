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

import com.github.tomakehurst.wiremock.common.InvalidInputException;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.common.Notifier;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.UUID;
import redis.clients.jedis.JedisPubSub;

public class CommandSubscriber extends JedisPubSub {

  private final Admin admin;
  private final Notifier notifier;

  public CommandSubscriber(Admin admin, Notifier notifier) {
    this.admin = admin;
    this.notifier = notifier;
  }

  @Override
  public void onMessage(String channel, String message) {
    Topics topic = Topics.valueOf(channel);
    switch (topic) {
      case STUB_CREATE:
        notifier.info(String.format("Received a message at channel %s.", channel));
        stubCreate(message);
        break;
      case STUB_UPDATE:
        notifier.info(String.format("Received a message at channel %s.", channel));
        stubUpdate(message);
        break;
      case STUB_DELETE:
        notifier.info(
            String.format("Received a message at channel %s. Message: %s", channel, message));
        stubDelete(message);
        break;
      case SCENARIO_SET:
        notifier.importantInfo(
            String.format("Received a message at channel %s. Message: %s", channel, message));
        scenarioSet(message);
        break;
      case SCENARIO_RESET:
        notifier.importantInfo(
            String.format("Received a message at channel %s. Message: %s", channel, message));
        scenarioReset(message);
        break;
      case MAPPINGS_RESET_DEFAULT:
        notifier.info(
            String.format("Received a message at channel %s. Message: %s", channel, message));
        resetToDefaultMappings();
        break;
      default:
        notifier.info(
            String.format(
                "Received a message at an unknown channel %s. Message: %s", channel, message));
        break;
    }
  }

  private void scenarioReset(String message) {
    if (RedisCommandPublisher.VALUE_ALL.equals(message)) {
      admin.resetScenariosExecute();
    } else {
      admin.resetScenarioExecute(message);
    }
  }

  private void resetToDefaultMappings() {
    admin.resetToDefaultMappingsExecute();
  }

  private void scenarioSet(String message) {
    ScenarioMessage s = getScenarioMessage(message);
    try {
      admin.setScenarioStateExecute(s.getScenarioName(), s.getScenarioState());
    } catch (InvalidInputException ignored) {
    }
  }

  private void stubDelete(String message) {
    admin.removeStubMappingExecute(UUID.fromString(message));
  }

  private void stubUpdate(String message) {
    StubMapping stub = getStubMapping(message);
    admin.editStubMappingExecute(stub);
  }

  private void stubCreate(String message) {
    StubMapping stub = getStubMapping(message);
    admin.addStubMappingExecute(stub);
  }

  private ScenarioMessage getScenarioMessage(String message) {
    return Json.read(message, ScenarioMessage.class);
  }

  private StubMapping getStubMapping(String message) {
    return Json.read(message, StubMapping.class);
  }
}
