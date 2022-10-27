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

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.UUID;

public class NoOpCommandPublisher implements CommandPublisher {

  @Override
  public void addStubMapping(StubMapping stubMapping) {}

  @Override
  public void editStubMapping(StubMapping stubMapping) {}

  @Override
  public void resetAll() {}

  @Override
  public void resetRequests() {}

  @Override
  public void resetToDefaultMappings() {}

  @Override
  public void resetScenario(String name) {}

  @Override
  public void resetScenarios() {}

  @Override
  public void resetMappings() {}

  @Override
  public void removeServeEvent(UUID eventId) {}

  @Override
  public void removeStubMapping(UUID id) {}

  @Override
  public void setScenarioState(String name, String state) {}
}
