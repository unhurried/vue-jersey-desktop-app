# vue-jersey-desktop-app

An example of a desktop app built with [Vue.js](https://vuejs.org/index.html) (frontend) and [Jersey](https://eclipse-ee4j.github.io/jersey/) (backend).

## How to Start Development

### Frontend (Vue.js)

```shell
# Run the application in development mode.
npm run serve

# Build application for production.
# (Resources will be stored in "backend/src/main/resources/static".)
npm run build
```

### Backend (Jersey with Spring Boot)

```shell
# Run the application in development mode.
# (The application will start on port 8080.)
gradlew bootRun -Penv_profile=development

# Build an executable jar file.
# (Generated jar file will be stored in "backend/build/libs".)
gradlew bootJar
```

