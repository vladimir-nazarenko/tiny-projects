package com.magnet.entries;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by vladimir on 7/17/16.
 *
 * Counts the sum of the numbers in the xml file.
 * Configured via DI.
 */
public class NumXmlReducer {
    public void setFinalXmlFileName(String finalXmlFileName) {
        this.finalXmlFileName = finalXmlFileName;
    }

    private String finalXmlFileName;

    /**
     * Calculates the sum of the numbers in the file. Overflow may occur if sum is greater than 2^63 - 1.
     * @return sum of the number in the xml file.
     */
    public long reduce() {
        try (FileInputStream inputStream = new FileInputStream(finalXmlFileName)){
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            EntryHandler handler = new EntryHandler();
            saxParser.parse(inputStream, handler);
            return handler.getSum();
        } catch (SAXException | IOException |ParserConfigurationException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class EntryHandler extends DefaultHandler {
        // NOTE: may be overflown
        // supposed to store sum of 1 + ... + 2^31 (~10^9)
        // use BigDecimal if overflow may occur.
        private long sum = 0;

        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) {
            if (qName.equals("entry")) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("field")) {
                        sum += Integer.parseInt(attributes.getValue(i));
                    }
                }
            }
        }

        public long getSum() {
            return sum;
        }
    }
}
