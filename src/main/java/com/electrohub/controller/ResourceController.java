package com.electrohub.controller;

import com.electrohub.dto.ResourceLinkDto;
import com.electrohub.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for External Educational and Simulation Resources.
 * Base path: /api/resources
 * Demonstrates Spring MVC endpoints and Constructor-based Dependency Injection.
 */
@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    private final ResourceService resourceService;

    // Constructor-based Dependency Injection (Spring Core IoC)
    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * GET /api/resources — Retrieve all external resource links.
     */
    @GetMapping
    public ResponseEntity<List<ResourceLinkDto>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    /**
     * GET /api/resources/c-compiler — Embedded C coding and debugging platform.
     */
    @GetMapping("/c-compiler")
    public ResponseEntity<ResourceLinkDto> getCCompilerResource() {
        return ResponseEntity.ok(resourceService.getCCompilerResource());
    }

    /**
     * GET /api/resources/simulation — Electronics simulation platform (Wokwi).
     */
    @GetMapping("/simulation")
    public ResponseEntity<ResourceLinkDto> getSimulationResource() {
        return ResponseEntity.ok(resourceService.getSimulationResource());
    }

    /**
     * GET /api/resources/circuit-simulation — Circuit simulation platform (Tinkercad Circuits).
     */
    @GetMapping("/circuit-simulation")
    public ResponseEntity<ResourceLinkDto> getCircuitSimulationResource() {
        return ResponseEntity.ok(resourceService.getCircuitSimulationResource());
    }

    /**
     * GET /api/resources/component-info — Electronic component information and specs (Wikipedia).
     */
    @GetMapping("/component-info")
    public ResponseEntity<ResourceLinkDto> getComponentInfoResource() {
        return ResponseEntity.ok(resourceService.getComponentInfoResource());
    }

    /**
     * GET /api/resources/project-files — Project source code shareable Google Drive link.
     */
    @GetMapping("/project-files")
    public ResponseEntity<ResourceLinkDto> getProjectFilesResource() {
        return ResponseEntity.ok(resourceService.getProjectFilesResource());
    }

    /**
     * GET /api/resources/documentation — Project report and documentation Google Drive link.
     */
    @GetMapping("/documentation")
    public ResponseEntity<ResourceLinkDto> getDocumentationResource() {
        return ResponseEntity.ok(resourceService.getDocumentationResource());
    }
}
