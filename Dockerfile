# Build em duas etapas: compila o backend com Maven e roda com um JRE enxuto.
# Ajuste as tags de imagem conforme a versão de Java 25 disponível no seu ambiente.
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY backend ./backend
COPY frontend ./frontend
WORKDIR /workspace/backend
RUN mvn -q -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
