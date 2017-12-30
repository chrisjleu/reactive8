# Reactive8
An [opnionated](https://stackoverflow.com/questions/802050/what-is-opinionated-software#802064) starter kit for building reactive web applications with microservices.

### What you get
1. [Vert.x](http://vertx.io) on the backend.
1. [SPA](https://en.wikipedia.org/wiki/Single-page_application) with Angular on the frontend.
1. Maven to build, package and run as an executable (fat) JAR.

### Coming soon
1. Reactive frontend with websockets

## Getting started
1. Install Java 8.
1. Checkout the code and `cd` to the checkout directory.
1. `./mvnw clean package`
1. `java -jar server/target/server-1.0.0-SNAPSHOT.jar`
1. [localhost:8080](http://localhost:8080)

## IDE
Running within an IDE is usually a matter of indicating the class with the `main` method. 
Vert.x comes with the convenience class `io.vertx.core.Launcher` that can be used to start the application.

The following program arguments are required:

`run io.reactive8.server.MainVerticle -conf server/conf.json`

## Other start/stop options
### List all started Vertx applications
`java -jar server/target/server-1.0.0-SNAPSHOT.jar stop`

Pass the "app ID" (UUID) as an argument to `stop` to stop an application.

### Start the application in debug mode
`mvn vertx:debug -f server/pom.xml`

Connect to this with an IDE debugger on the default port (5005).

## Notes
* One of the goals for this starter application was to have an easy way to build and package the entire application.
There is some challenge there in combining the build and packaging of the frontend application with the backend.
This is achieved thanks to [Apache Maven](https://maven.apache.org/) and specifically a combination of the 
`vertx-maven-plugin` and `frontend-maven-plugin` plugins (see references below).

The trick is to ensure `frontend-maven-plugin` packages up the client/frontend application in accordance with the
 [WebJars](http://www.webjars.org/) convention since `vertx-maven-plugin` is looking out for these types of jars to 
 unpack to a location that Vert.x can serve up at runtime.

The following stanza in `client/pom.xml` is crucial for two reasons:

```xml
    <execution>
        <id>yarn build</id>
        <goals>
            <goal>yarn</goal>
        </goals>
        <phase>generate-resources</phase>
        <configuration>
            <arguments>build --prod --base-href /assets/${project.name}/ -op dist/META-INF/resources/webjars/${project.name}/${project.parent.version}</arguments>
        </configuration>
    </execution>
```

1. `-op` ensures the resulting JAR conforms to the WebJars convention of having all "assets" in `/META-INF/resources`. In fact `vertx-maven-plugin` imposes the strict requirement that only files matching the following pattern are unpacked to [Vertx's web root](http://vertx.io/docs/vertx-web/java/#_serving_static_resources): `".*META-INF/resources/webjars/([^/]+)/([^/]+)/.*"` See the [source code](https://github.com/fabric8io/vertx-maven-plugin/blob/master/src/main/java/io/fabric8/vertx/maven/plugin/utils/WebJars.java) if curious.
1. `--base-href` ensures that CSS Javascript and other assets are referenced correctly from `index.html`. Later it was necessary to configure correctly Vertx's web root to expect where to find `index.html` and other static static resources.

## References
* [A minimalist guide to building spring boot angular 5 applications](https://shekhargulati.com/2017/11/08/a-minimalist-guide-to-building-spring-boot-angular-5-applications/): 
Much of the initial setup work was adapted from an attempt to do something similar with Angular and Spring Boot in this article.
* [Building a real-time web app with Angular/Ngrx and Vert.x](https://medium.com/@benorama/building-a-realtime-web-app-with-angular-ngrx-and-vert-x-a5381c0397a1) follows a similar directory layout with the split between client/server (frontend/backend) modules. The article is less instructive but there is a code repository for reference.
* [vertx-maven-plugin](https://mvnrepository.com/artifact/io.fabric8/vertx-maven-plugin) (VMP) and [documentation](https://vmp.fabric8.io/).
* [frontend-maven-plugin](https://mvnrepository.com/artifact/com.github.eirslett/frontend-maven-plugin) and [documentation on Github](https://github.com/eirslett/frontend-maven-plugin).
