# Event Tracking Service

## History

The Event Tracking Service is mainly a data collection service in the Media Decisioning Engine (MDE).
It's main purpose is to collect media viewing impressions from a southbound component. This could be a client-facing API component (cfr. Yelo API) or a (sub)component of the COTS Manifest Manipulator. Additionally it also allows to retrieve the collected impression data via a REST service API.<br><br>
Initially, it was decided to persist events in an RDBMS (MySQL) but it has been decided to use Elastic Search in stead.<br>
This version of Event Tracking Service has been tested against ES 6.1.1.<br><br>
Please use the ES Head Chrome plugin (or Curl) to query ES.<br><br>

## Running the project

### Running application locally
Boot a recent version of ES (preferably version 6.1.1)<br>
mvn spring-boot:run -Dserver.port=8081

### Swagger
http://localhost:8081/swagger-ui.html

### Actuator
Base uri to access actuator endpoint: http://localhost:8081/admin.<br>

### TODO
* For the time being, the unit tests have been written as a kind of integration test (end to end) and an available ES
is a prerequisite to run the tests successfully.

### Websites of interest
* http://jwtbuilder.jamiekurtz.com/
* https://www.epochconverter.com/
* http://www.baeldung.com/spring-boot-info-actuator-custom
* https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html
* https://www.elastic.co/guide/en/elasticsearch/reference/6.0/breaking-changes-6.0.html
* https://www.elastic.co/cloud
