# postal-code-challenge
=======================

**Prerequisites**

  Be sure to have installed in your system
    - java 8
    - maven
    - git
    - nodejs
    - bower

**Usage**

  - cd into the project directory
  - run "npm install"
  - run "bower install"
  - run "mvn spring-boot:run" for development mode. The application will run at 'localhost:7070'
  - run "mvn -Pprod spring-boot:run" for production mode. The application will run at 'localhost:8080'

**DataBase Configuration**

  The application runs with an in memory H2 database. if yo wish to use a my sql database go to src/main/resources/config, open the file application-dev.yml or application-prod.yml and change the following properties
  
    dataSourceClassName: com.mysql.jdbc.jdbc2.optional.MysqlDataSource
    url: jdbc:h2:mem:jhipster;DB_CLOSE_DELAY=-1
    databaseName: jdbc:mysql://<server>:<port>/<schema>
    username: <username>
    password: <password>
