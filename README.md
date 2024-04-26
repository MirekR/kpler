**Prerequisites**
  - Install [Java 17](https://www.oracle.com/java/technologies/downloads/)
  - Install [Maven](https://maven.apache.org/install.html)
  - Install [Docker](https://docs.docker.com/desktop/install/mac-install/)

**Local build and start up**

***Building service***
```
mvn clean install
```

***Starting service***
```
java -jar target/kpler-*.jar
```

***Swagger UI***

To see available endpoints, you can navigate in your browser to [Swagger UI](http://localhost:8080/swagger-ui/index.html#/)

***Requesting JSON and XML responses***

By default, the service returns data in JSON format, to request in XML format, provide request header ```Accept=application/xml```

***Dockerisation***

Once the app is build, you can create docker image by running command:
```docker build --tag=kpler:latest .  ```

Then you can start dockerised version by
```docker run -p 8888:8080 kpler:latest```

Then verify by browsing
```http://localhost:8888/swagger-ui/index.html```

Happy dockering :) 

**Discussion and consideration**

***Logging and monitoring:***
Service is using standard Logger to log into the console, it's up to the environment to decide where logs are hosted.
It can be provided directly by hosting provider (like CloudWatch on AWS) or monitored via Elastic Search.

To log incoming requests and the responses, we annotate each method with ``@LoggableRequest``, see example in the ``KplerController.java``.
This is picked up by LoggingAspect that logs incoming parameters and logs response.


For information about memory usage, request and response times, service implements [Micrometer Prometeus](https://docs.micrometer.io/micrometer/reference/implementations/prometheus.html), 
that could be plugged into Grafana or similar monitoring system.
You can see raw data available via [url](http://localhost:8080/actuator/prometheus), interesting section worth noting is "# TYPE http_server_requests_seconds summary"
displaying number of requests per endpoints, average times and http code responses. 

***Correlation ids:***
Requests and responses should implement "correlationId" field for traceability across systems. At the moment there are no downstream 
services but correlationIds are, to display the idea, used in the request/reponse in the ingest.

***Upload performance:***
 - Single Record upload / streaming: Service allows to push single record in order to ensure as much "up-to-date"
   data as possible. This relies on upstream services to push data as they come. It is allowed via standard rest
   endpoint, in the future we could add functionality to consume data from Kafka or any other messaging platform
   that would be supported by upstream services
 - Bulk upload: At the moment, service implements simple ingest endpoint (POST at http://localhost:8080/kpler) accepting JSON body.
Assuming the data set will grow, this might become too time-consuming, for example when using API Gateway in AWS max 
request timeout is 30s potentionally causing issues. 
This could be mitigated using async processing. We could introduce endpoint POST /kpler/upload returning upload url, for 
example presign url for AWS s3 bucket - depends on the technology available with callback capabilities.

Proposed request structure
```
  {
    "callback": {
      "url": string
    }
  }
```
Proposed response structure
```
{
  "correlationId": string,
  "uploadUrl": string,
  "method": string
}
```

Proposed callback response structure
```
  {
    "correlationId": string,
    "status": enum(OK, ERROR),
    "errors": {}
  }
```

**Quality**

***Unit testing:***
The service code is unit tested, for ensuring quality part of the ``mvn clean install``, jacoco reporting runs
generating results in the ``/target/site/jacoco`` folder. Results can be view in browser and asserted in 
deployment pipelines to ensure code is tested. 

***Integration testing / BDD style:***
On top of the unit tests, there should set of integration tests making sure components are autowiring correctly and 
endpoints are able to request and response to requests. 
Implemented as IT with notes in reference to the Cucumber style, that could be improvement to write them in this style
around scenarios - see ``KplerApplicationIT``
Example: 
```
  Scenario: Sunny upload and data query
  When Ingest endpoint is requested correlationId "correlationId" with data
  |mmis|timestamp|||||
  Then Service should process data and respone with correlationId "correlationId"
  Then I reuqest full data set matching
  |data example||||
  Then I request data for mmsi
  |data example||||
```

**Others**

***User Rate Limiting:***
Service itself doesn't enforce rate limiting, this is done via nginx for greater flexibility and avoiding in the 
future implementing same limiting over different services provided by the company. At the moment it has
simple setup on limiting per ip address but in the future this could be extended via Lua scripting language 
to add checks and limits per logged in user and their purchased plans. 

To start nginx: ```docker run -it -p 666:666 -v $(pwd)/nginx/nginx.local:/etc/nginx/nginx.conf nginx ``` 
and access service via ```http://localhost:666```

***Data consistency / ids***
On insert to the database, id per position record is generated as combination of the mmsi, stationId and timestamp.
This allows multiple processing without overhead of checking existence of the record. If the record exists, 
it overrides data with the latest information.