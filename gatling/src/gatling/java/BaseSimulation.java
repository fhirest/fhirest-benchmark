import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public abstract class BaseSimulation extends Simulation {
  protected Integer timeFactor = Integer.valueOf(System.getProperty("timeFactor"));
  protected Integer userFactor = Integer.valueOf(System.getProperty("userFactor"));
  protected String fhirUrl = System.getProperty("fhirUrl");
  protected String pageSize = System.getProperty("pageSize");
  protected String count = "&_count=10";

  protected HttpProtocolBuilder httpProtocol = http
      .baseUrl(fhirUrl)
      .acceptHeader("application/json")
      .contentTypeHeader("application/json");

  {
    ScenarioBuilder scn = getScenario();
    setUp(scn.injectOpen(rampUsers(userFactor).during(timeFactor * 60))).protocols(httpProtocol);
//    setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }

  protected abstract ScenarioBuilder getScenario();

  protected HttpRequestActionBuilder req(String name, String url) {
    return http(name).get(url).check(status().is(200));
  }
}
