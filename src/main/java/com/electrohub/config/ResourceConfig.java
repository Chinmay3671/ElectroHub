package com.electrohub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for external resource links and Google Drive shareable URLs.
 * Values are injected from application.properties via Spring Core @Value.
 */
@Configuration
public class ResourceConfig {

    @Value("${resources.c-compiler:https://www.onlinegdb.com/online_c_compiler}")
    private String cCompilerUrl;

    @Value("${resources.simulation:https://wokwi.com/}")
    private String simulationUrl;

    @Value("${resources.circuit-simulation:https://www.tinkercad.com/circuits}")
    private String circuitSimulationUrl;

    @Value("${resources.component-info:https://www.wikipedia.org/}")
    private String componentInfoUrl;

    @Value("${resources.project-files:https://drive.google.com/drive/folders/sample-electrohub-project-source}")
    private String projectFilesUrl;

    @Value("${resources.documentation:https://drive.google.com/file/d/sample-electrohub-project-report-pdf/view}")
    private String documentationUrl;

    public String getCCompilerUrl() {
        return cCompilerUrl;
    }

    public String getSimulationUrl() {
        return simulationUrl;
    }

    public String getCircuitSimulationUrl() {
        return circuitSimulationUrl;
    }

    public String getComponentInfoUrl() {
        return componentInfoUrl;
    }

    public String getProjectFilesUrl() {
        return projectFilesUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }
}
