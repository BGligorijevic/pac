package com.prodyna.voting.datagenerator;

import com.prodyna.voting.Application;
import com.prodyna.voting.sampledata.DataGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Meant to be used when testing UI manually.
 * Remove ignore annotation to run the tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ManualDataGenerator {

    @Autowired
    private DataGenerator dataGenerator;

    @Test
    @Ignore
    public void generateTestData() {
        dataGenerator.regenerateTestData();
    }

    @Test
    @Ignore
    public void removeAllTestData() {
        dataGenerator.removeAllTestData();
    }

    @Test
    @Ignore
    public void removeVotes() {
        dataGenerator.removeVotes();
    }
}
