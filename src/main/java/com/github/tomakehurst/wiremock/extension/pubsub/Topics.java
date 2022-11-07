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

public enum Topics {
  STUB_CREATE("wiremock-stub-create"),
  STUB_UPDATE("wiremock-stub-update"),
  STUB_DELETE("wiremock-stub-delete"),
  SCENARIO_SET("wiremock-scenario-set"),
  SCENARIO_RESET("wiremock-scenario-reset"),
  MAPPINGS_RESET("wiremock-mappings-reset"),
  MAPPINGS_RESET_DEFAULT("wiremock-mappings-default-reset"),
  REMOVE_SERVE_EVENT("wiremock-remove-serve-event"),
  ALL_RESET("wiremock-all-reset"),
  REQUESTS_RESET("wiremock-requests-reset");

  private final String topic;

  Topics(String topic) {
    this.topic = topic;
  }

  public String getTopic() {
    return topic;
  }
}
