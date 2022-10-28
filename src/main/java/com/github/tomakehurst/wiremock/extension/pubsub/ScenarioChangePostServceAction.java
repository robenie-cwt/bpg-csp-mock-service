package com.github.tomakehurst.wiremock.extension.pubsub;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

public class ScenarioChangePostServceAction extends PostServeAction {

  private final CommandPublisher publisher;

  public ScenarioChangePostServceAction(CommandPublisher publisher) {
    super();
    this.publisher = publisher;
  }

  @Override
  public String getName() {
    return "scenario-post-serve";
  }

  @Override
  public void doGlobalAction(ServeEvent serveEvent, Admin admin) {
    if (!serveEvent.getWasMatched()) {
      return;
    }

    String scenarioName = serveEvent.getStubMapping().getScenarioName();
    if (scenarioName == null || scenarioName.isEmpty()) {
      return;
    }

    admin.getAllScenarios().getScenarios().stream()
        .filter(s -> s.getName().equals(scenarioName))
        .map(Scenario::getState)
        .findAny()
        .ifPresent(state -> setState(scenarioName, state));
  }

  private void setState(String scenarioName, String state) {
    publisher.setScenarioState(scenarioName, state);
  }
}
