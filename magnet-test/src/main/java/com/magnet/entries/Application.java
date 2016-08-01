package com.magnet.entries;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by vladimir on 7/11/16.
 *
 * Main application class. Loads Spring application context and executes the tasks in the following order:
 * - Populate the database with numbers,
 * - Read from database and store numbers into xml file,
 * - Perform xslt transformation on file from previous step,
 * - Reduce file from previous step to sum of the numbers, which it holds,
 * - Print the sum.
 */
public class Application {
    public static void main(String[] args) {
        // Initialize the context
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
        // Work with a database
        PostgresDBManager postgresDBManager = context.getBean(PostgresDBManager.class);
        postgresDBManager.writeNumbersToDB();
        NumberProducer producer = postgresDBManager.readNumbersFromDB();
        // Write numbers from the database to an xml file
        NumXmlWriter writer = context.getBean(NumXmlWriter.class);
        writer.setProducer(producer);
        writer.writeXml();
        // Transform xml with xslt
        NumXmlTransformer transformer = context.getBean(NumXmlTransformer.class);
        transformer.transformXml();
        // Parse and reduce xml file
        NumXmlReducer reducer = context.getBean(NumXmlReducer.class);
        long sum = reducer.reduce();
        System.out.printf("Sum of values in the resulting xml file: %d%n", sum);
    }
}
