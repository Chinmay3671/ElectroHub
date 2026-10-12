package com.electrohub.config;

import com.electrohub.entity.Product;
import com.electrohub.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Seeds initial electronics products into MySQL on startup if database is empty.
 * Demonstrates Spring Core CommandLineRunner and Constructor Injection.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Seeding initial electronic products into ElectroHub database...");

            List<Product> sampleProducts = Arrays.asList(
                    new Product("Arduino Uno R3", "Microcontrollers", 25.99,
                            "ATmega328P microcontroller development board with 14 digital I/O pins and 6 analog inputs."),
                    new Product("Raspberry Pi 4 Model B (4GB)", "Single Board Computers", 75.00,
                            "Quad-core 64-bit ARM Cortex-A72 processor, 4GB LPDDR4 RAM, dual 4K micro-HDMI display outputs."),
                    new Product("ESP32 NodeMCU Module", "IoT & Wireless", 8.50,
                            "Dual-core 240MHz Wi-Fi and Bluetooth BLE IoT microcontroller development board."),
                    new Product("MB102 Solderless Breadboard", "Prototyping", 4.99,
                            "Standard 830 tie-points solderless breadboard with dual power distribution rails."),
                    new Product("HC-SR04 Ultrasonic Sensor", "Sensors", 3.25,
                            "High accuracy non-contact ultrasonic distance measuring sensor (2cm to 400cm range)."),
                    new Product("SG90 Micro Servo Motor (9g)", "Motors & Actuators", 2.99,
                            "Compact 180-degree rotation micro servo motor suitable for robotics and hobby projects.")
            );

            productRepository.saveAll(sampleProducts);
            log.info("Successfully seeded {} products into MySQL.", sampleProducts.size());
        } else {
            log.info("Products already present in database (count: {}). Skipping seed.", productRepository.count());
        }
    }
}
