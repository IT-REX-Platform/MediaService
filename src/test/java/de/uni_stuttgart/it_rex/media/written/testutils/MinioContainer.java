package de.uni_stuttgart.it_rex.media.written.testutils;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class MinioContainer {

  private Integer minioMappedPort;
  private String minioMappedHost;
  private String minioUrl;
  private DockerComposeContainer container;
  private final Integer MINIO_PORT = 9000;

  public MinioContainer() {
    this.container = new DockerComposeContainer(new File("src/test/resources/docker/minio.yml")).
        withExposedService("minio", MINIO_PORT).withLocalCompose(true);
    start();
  }

  public String getMinioUrl() {
    return minioUrl;
  }

  public void start() {
    container.start();
    this.minioMappedPort = container.getServicePort("minio", MINIO_PORT);
    this.minioMappedHost = container.getServiceHost("minio", MINIO_PORT);
    this.minioUrl = String.format("http://%s:%d", minioMappedHost, minioMappedPort);
  }

  public void stop() {
    container.stop();
  }
}

