package com.github.tomakehurst.wiremock.extension.pubsub;

import com.github.tomakehurst.wiremock.common.DataTruncationSettings;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.core.StubServer;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilter;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.ResponseRenderer;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;
import com.github.tomakehurst.wiremock.http.StubResponseRenderer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.RequestJournal;

import java.util.List;
import java.util.Map;

public class ScenarioStateStubRequestHandler extends StubRequestHandler {

  private final CommandPublisher publisher;

  public ScenarioStateStubRequestHandler(StubServer stubServer,
                                         ResponseRenderer responseRenderer,
                                         Admin admin,
                                         Map<String, PostServeAction> postServeActions,
                                         RequestJournal requestJournal,
                                         List<RequestFilter> requestFilters,
                                         boolean loggingDisabled,
                                         DataTruncationSettings dataTruncationSettings,
                                         CommandPublisher publisher) {
    super(stubServer,responseRenderer,admin,postServeActions,requestJournal,requestFilters, loggingDisabled,
            dataTruncationSettings);
    this.publisher=publisher;
  }

  @Override
  protected void beforeResponseSent(ServeEvent serveEvent, Response response) {
    if (!serveEvent.getWasMatched()) {
      super.beforeResponseSent(serveEvent, response);
      return;
    }

    String scenarioName = serveEvent.getStubMapping().getScenarioName();
    if (scenarioName == null || scenarioName.isEmpty()) {
      super.beforeResponseSent(serveEvent, response);
      return;
    }

    admin.getAllScenarios().getScenarios().stream()
            .filter(s -> s.getName().equals(scenarioName))
            .map(Scenario::getState)
            .findAny()
            .ifPresent(state -> setState(scenarioName, state));
    super.beforeResponseSent(serveEvent, response);
  }

  private void setState(String scenarioName, String state) {
    publisher.setScenarioState(scenarioName, state);
  }
}
