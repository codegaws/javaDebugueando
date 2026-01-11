package com.debugeandoideas.gadgetplus;

import com.debugeandoideas.gadgetplus.services.CatalogBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GadgetPlusApplication_Batch {//implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication_Batch.class, args);
    }

 /*   @Autowired
    private CatalogBatch catalogBatch;

    @Override
    public void run(String... args) throws Exception {
        this.catalogBatch.insertBatch();

        Thread.sleep(1000);

        this.catalogBatch.delteBatch();

    }*/
}


