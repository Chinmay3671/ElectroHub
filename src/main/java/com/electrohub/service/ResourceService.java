package com.electrohub.service;

import com.electrohub.config.ResourceConfig;
import com.electrohub.dto.ResourceLinkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service class for serving external resource links and Google Drive documentation URLs.
 * Demonstrates Spring Core Constructor Injection from ResourceConfig bean.
 */
@Service
public class ResourceService {

    private final ResourceConfig resourceConfig;

    // Constructor-based Dependency Injection (Spring Core IoC)
    @Autowired
    public ResourceService(ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
    }

    /**
     * Returns the Embedded C coding and debugging platform URL.
     */
    public ResourceLinkDto getCCompilerResource() {
        return new ResourceLinkDto(
                "c-compiler",
                "Embedded C Online Compiler & Debugger",
                "Online compiler and debugger for Embedded C programming and testing microcontroller algorithms.",
                resourceConfig.getCCompilerUrl()
        );
    }

    /**
     * Returns the Electronics simulation platform URL (Wokwi).
     */
    public ResourceLinkDto getSimulationResource() {
        return new ResourceLinkDto(
                "simulation",
                "Electronics Simulation Platform (Wokwi)",
                "Full-featured browser simulator for Arduino, ESP32, and Raspberry Pi Pico electronics projects.",
                resourceConfig.getSimulationUrl()
        );
    }

    /**
     * Returns the Circuit simulation platform URL (Tinkercad Circuits).
     */
    public ResourceLinkDto getCircuitSimulationResource() {
        return new ResourceLinkDto(
                "circuit-simulation",
                "Autodesk Tinkercad Circuits Simulation",
                "Beginner-friendly online 3D circuit breadboarding, schematic testing, and simulation tool.",
                resourceConfig.getCircuitSimulationUrl()
        );
    }

    /**
     * Returns the Component information encyclopedia URL (Wikipedia).
     */
    public ResourceLinkDto getComponentInfoResource() {
        return new ResourceLinkDto(
                "component-info",
                "Electronic Component Information & Specifications",
                "Reference encyclopedia for pinouts, datasheets, operational theories, and component overviews.",
                resourceConfig.getComponentInfoUrl()
        );
    }

    /**
     * Returns the Project Source Code Google Drive shareable link.
     */
    public ResourceLinkDto getProjectFilesResource() {
        return new ResourceLinkDto(
                "project-files",
                "ElectroHub Project Source Code & Assets (Google Drive)",
                "Google Drive shareable folder containing project source archives, database scripts, and diagrams.",
                resourceConfig.getProjectFilesUrl()
        );
    }

    /**
     * Returns the Project Documentation Google Drive shareable link.
     */
    public ResourceLinkDto getDocumentationResource() {
        return new ResourceLinkDto(
                "documentation",
                "ElectroHub Project Report & Documentation (Google Drive)",
                "Google Drive link to full project synopsis, system architecture diagrams, and viva notes.",
                resourceConfig.getDocumentationUrl()
        );
    }

    /**
     * Returns all external resources as a consolidated list.
     */
    public List<ResourceLinkDto> getAllResources() {
        return Arrays.asList(
                getCCompilerResource(),
                getSimulationResource(),
                getCircuitSimulationResource(),
                getComponentInfoResource(),
                getProjectFilesResource(),
                getDocumentationResource()
        );
    }
}
