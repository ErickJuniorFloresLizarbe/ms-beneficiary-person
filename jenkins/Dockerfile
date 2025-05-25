# Usa una imagen base de Jenkins
FROM jenkins/jenkins:lts

# Configurar el usuario root para instalar herramientas
USER root

# Instalar JDK 17 y Maven 3.8.1
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    && apt-get clean;

# Configurar el entorno JAVA_HOME y MAVEN_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV MAVEN_HOME=/usr/share/maven
ENV PATH="${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${PATH}"

# Volver a Jenkins como usuario por defecto
USER jenkins

# Expone el puerto en el que Jenkins funciona
EXPOSE 8080

# Ejecutar Jenkins cuando se inicie el contenedor
ENTRYPOINT ["/usr/bin/tini", "--", "/usr/local/bin/jenkins.sh"]

#docker build -t jenkins-maven-jdk17 .
#docker run -d -p 8080:8080 --name jenkins-maven-jdk17 jenkins-maven-jdk17
#docker exec -it jenkins-maven-jdk17 cat /var/jenkins_home/secrets/initialAdminPassword

#docker start jenkins-maven-jdk17
