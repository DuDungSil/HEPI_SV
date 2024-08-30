package org.hepi.hepi_sv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HepiSvApplication {
    private static final Logger logger = LoggerFactory.getLogger(HepiSvApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HepiSvApplication.class, args);
        logger.info("==================== START HEPI SERVER ====================");

    }

}
