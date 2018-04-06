# Elasticsearch GEO POC

## History
This project has been designed/developed as a POC. Focus of the POC is to test a number of GEO related queries against Elasticsearch (ES).

## Running the project

### Running application locally
Boot a recent version of ES (preferably version 6.1.1)<br>
mvn spring-boot:run -Dserver.port=8090

### Swagger
http://localhost:8090/swagger-ui.html

### Actuator
Base uri to access actuator endpoint: http://localhost:8090/admin.<br>

### JMX / Jolokia
Base uri to access Jolokia endpoint: http://localhost:8090/admin/jolokia.<br>

### Websites of interest
* https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.1/java-rest-high.html
* https://jolokia.org/reference/html/protocol.html
* https://www.elastic.co/elasticon/conf/2018/sf/the-state-of-geo-in-elasticsearch
* https://www.elastic.co/elasticon/conf/2017/sf/what-is-evolving-in-elasticsearch