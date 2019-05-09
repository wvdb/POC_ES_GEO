# Elasticsearch Spatial Queries POC

## History
This project has been designed/developed as a POC. Focus of the POC is to test some Spatial Queries against Elasticsearch (ES).<br>
For this POC, we use the RestHighLevelClient and rely entirely on REST.<br>Code has been written in Java 9. Feel free to spot the usage of JDK 9 syntax and to downgrade to Java 8 if necessary.  
You can find 3 following types of Spatial Queries: 
- retrieve objects within certain distance
- retrieve communes within rectangle/BoundingBox
- retrieve communes within a polygon (implemented as a unit test)  

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
* https://www.latlong.net/Show-Latitude-Longitude.html
* https://www.google.com/fusiontables/DataSource?docid=12Yp5H2L-W0VrHC68V4wG7DFgariZ26f5bRXIo5NB
* http://www.headwallphotonics.com/polygon-tool
* https://www.keene.edu/campus/maps/tool/
* https://medium.com/criciumadev/its-time-migrating-to-java-11-5eb3868354f9