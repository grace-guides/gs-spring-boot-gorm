[![Main branch build status](https://github.com/grace-guides/gs-spring-boot-gorm/workflows/Grace%20CI/badge.svg?style=flat)](https://github.com/grace-guides/gs-spring-boot-gorm/actions?query=workflow%3A%Grace+CI%22)
[![Apache 2.0 license](https://img.shields.io/badge/License-APACHE%202.0-green.svg?logo=APACHE&style=flat)](https://opensource.org/licenses/Apache-2.0)
[![Grace on X](https://img.shields.io/twitter/follow/graceframework?style=social)](https://twitter.com/graceframework)

[![Groovy Version](https://img.shields.io/badge/Groovy-4.0.23-blue?style=flat&color=4298b8)](https://groovy-lang.org/releasenotes/groovy-4.0.html)
[![Grace Version](https://img.shields.io/badge/Grace-2023.1.0-blue?style=flat&color=f49b06)](https://github.com/graceframework/grace-framework/releases/tag/v2023.1.0-M3)
[![Spring Boot Version](https://img.shields.io/badge/Spring_Boot-3.3.5-blue?style=flat&color=6db33f)](https://github.com/spring-projects/spring-boot/releases)


# Spring Boot with Grace GORM

Learn how to build a Spring Boot application using GORM.

### Versions

* Spring Boot 3.3.5
* Grace Framework 2023.1.0-M3

### Adding Grace Dependencies

```gradle
plugins {
	id 'groovy'
	id 'org.springframework.boot'
	id 'io.spring.dependency-management'
	id 'org.graceframework.grace-core'
}

dependencies {
	implementation 'org.apache.groovy:groovy'
	implementation 'org.graceframework:grace-boot'
	implementation 'org.graceframework:grace-plugin-core'
	implementation 'org.graceframework:grace-plugin-datasource'
	implementation 'org.graceframework.plugins:hibernate'
	runtimeOnly 'com.h2database:h2'
}
```

### Configure Hibernate Dialect and Datasource

Update `src/main/resources/application.yml`,

```yml
hibernate:
    show_sql: true
    dialect:  org.hibernate.dialect.H2Dialect
dataSource:
    dbCreate: create-drop
    pooled: true
    jmxExport: true
    driverClassName: org.h2.Driver
    url: jdbc:h2:file:./build/boot_dev;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
```

### Creating Post Domain and GORM Service

Creating `Post.groovy` in `app/domain`,

```groovy
class Post {

    String title

    static constraints = {
        title(blank: false, nullable: false, maxSize: 255)
    }

    String toString() {
        "#$id $title"
    }
}
```

Creating `PostService.groovy` in `app/services`,

```groovy
@grails.gorm.services.Service(Post)
interface PostService {

    List<Post> findAll()

    Post save(Post post)

}
```

Now, GORM will generate `$PostServiceImplementation` for your application,
then you can use it in your Spring Boot application just like in Grace,

```groovy
// Post methods
new Post(title: 'Grace 2022.2.8 is released').save()
Post.list()
Post.where()

// PostService methods
postService.findAll()
```

### Running the App

Staring the App by execute the following Gradle task,

```bash
./gradlew bootRun
```

In the `src/main/groovy/grace/guides/GraceApplication`, remember that, exculding the AutoConfigurations, `HibernateJpaAutoConfiguration`, `DataSourceAutoConfiguration`, `TransactionAutoConfiguration` in `GraceApplication`.

```groovy
@SpringBootApplication(exclude = [HibernateJpaAutoConfiguration, DataSourceAutoConfiguration, TransactionAutoConfiguration])
class GraceApplication implements CommandLineRunner {

	@Autowired
	BootStrap bootStrap

	@Autowired
	PostService postService

	static void main(String[] args) {
		SpringApplication.run(GraceApplication, args)
	}

	@Override
	void run(String... args) throws Exception {
		println 'Prepare Sample Data '.padRight(94, '>')
		bootStrap.init()

		println()
		println 'Load All Posts '.padRight(94, '>')
		postService.findAll().each {
			println it 
		}

	}

}
```

### Running the App

```bash
~/gs-spring-boot-gorm ./gradlew bootRun

> Task :bootRun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.5)

2024-11-05T12:14:13.189+08:00  INFO 18006 --- [  restartedMain] grace.guides.GraceApplication            : Starting GraceApplication using Java 17.0.12 with PID 18006 (/Users/rain/Development/github/grace/grace-guides/gs-spring-boot-gorm/build/classes/groovy/main started by rain in /Users/rain/Development/github/grace/grace-guides/gs-spring-boot-gorm)
2024-11-05T12:14:13.190+08:00  INFO 18006 --- [  restartedMain] grace.guides.GraceApplication            : No active profile set, falling back to 1 default profile: "default"
2024-11-05T12:14:13.209+08:00  INFO 18006 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2024-11-05T12:14:13.209+08:00  INFO 18006 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2024-11-05T12:14:13.631+08:00  INFO 18006 --- [  restartedMain] g.plugins.DefaultGrailsPluginManager     : Total 3 plugins loaded successfully, take in 37 ms
2024-11-05T12:14:13.850+08:00  INFO 18006 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2024-11-05T12:14:13.857+08:00  INFO 18006 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-11-05T12:14:13.858+08:00  INFO 18006 --- [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
2024-11-05T12:14:13.893+08:00  INFO 18006 --- [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-11-05T12:14:13.893+08:00  INFO 18006 --- [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 684 ms
2024-11-05T12:14:14.058+08:00  INFO 18006 --- [  restartedMain] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:file:./build/boot_dev'
2024-11-05T12:14:14.205+08:00  INFO 18006 --- [  restartedMain] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.6.15.Final
2024-11-05T12:14:14.308+08:00  INFO 18006 --- [  restartedMain] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
2024-11-05T12:14:14.350+08:00  INFO 18006 --- [  restartedMain] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
Hibernate: drop table if exists post CASCADE
Hibernate: create table post (id bigint generated by default as identity, version bigint not null, title varchar(255) not null, primary key (id))
2024-11-05T12:14:14.725+08:00  WARN 18006 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2024-11-05T12:14:14.902+08:00  WARN 18006 --- [  restartedMain] .b.a.g.t.GroovyTemplateAutoConfiguration : Cannot find template location: classpath:/templates/ (please add some templates, check your Groovy configuration, or set spring.groovy.template.check-template-location=false)
2024-11-05T12:14:14.949+08:00  INFO 18006 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2024-11-05T12:14:14.956+08:00  INFO 18006 --- [  restartedMain] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2024-11-05T12:14:14.995+08:00  INFO 18006 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2024-11-05T12:14:15.006+08:00  INFO 18006 --- [  restartedMain] grace.guides.GraceApplication            : Started GraceApplication in 1.989 seconds (process running for 2.332)
2024-11-05T12:14:15.015+08:00 DEBUG 18006 --- [  restartedMain] PluginsInfoApplicationContextInitializer :
----------------------------------------------------------------------------------------------
Order      Plugin Name                              Plugin Version                     Enabled
----------------------------------------------------------------------------------------------
    1      Core                                     2023.1.0-M3                              Y
    2      DataSource                               2023.1.0-M3                              Y
    3      Hibernate                                2023.1.0-M3                              Y
----------------------------------------------------------------------------------------------

Prepare Sample Data >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)

Load All Posts >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Hibernate: select this_.id as id1_0_0_, this_.version as version2_0_0_, this_.title as title3_0_0_ from post this_
#1 Grace 2022.2.0 is released
#2 Grace 2022.2.1 is released
#3 Grace 2022.2.2 is released
#4 Grace 2022.2.3 is released
#5 Grace 2022.2.4 is released
#6 Grace 2022.2.5 is released
#7 Grace 2022.2.6 is released
#8 Grace 2022.2.7 is released
#9 Grace 2022.2.8 is released
#10 What's new in Grace 2023.0.0?
```

# Links

* [Grace Framework](https://github.com/graceframework/grace-framework)
* [Spring Boot with Plugins](https://github.com/grace-guides/gs-spring-boot)
* [Spring Boot with GSP](https://github.com/grace-guides/gs-spring-boot-gsp)
