import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class TestSimulation extends BaseSimulation {
  @Override
  protected ScenarioBuilder getScenario() {
    return scenario("scenario1")
        .feed(csv("data/patients.csv").random())
        .exec(req("find patinent", "/Patient?name=#{family}" + count))
        .exec(req("load practitioner", "/Practitioner?_count=1").check(jsonPath("$.entry[0].resource.id").saveAs("practitioner_id")))
        .exitHereIfFailed()
        .pause(3)
        .exec(req("encounters", "/Encounter?subject=#{patient_id}&_sort=-date" + count))
        .exec(req("observations", "/Observation?subject=#{patient_id}&_sort=-date" + count))
        .exec(req("medication requests", "/MedicationRequest?subject=#{patient_id}&_sort=-date" + count))
        .exec(req("claims", "/Claim?patient=#{patient_id}&_sort=-created" + count))
        .exec(req("immunizations", "/Immunization?patient=#{patient_id}&_sort=-date" + count))
        .exitHereIfFailed()
        .pause(5)
        .exec(http("create immunization")
            .post("/Immunization")
            .body(ElFileBody("data/immunization.json"))
            .check(status().is(201))
            .check(header("Location").saveAs("created_immunization"))
        )
        .exec(s -> {
          String[] parts = ((String) s.get("created_immunization")).split("/");
          s = s.set("created_immunization_id", parts[1]);
          return s;
        })
        .exitHereIfFailed()
        .pause(1)
        .exec(req("load created immunization", "/#{created_immunization}"))
        .exitHereIfFailed()
        .pause(1)
        .exec(http("update immunization")
            .put("/Immunization/#{created_immunization_id}")
            .body(ElFileBody("data/immunization.json"))
            .check(status().is(200))
        )
        .exitHereIfFailed()
        .pause(1)
        .exec(req("immunizations 2", "/Immunization?patient=#{patient_id}&_sort=-date" + count))
        ;
  }

}
