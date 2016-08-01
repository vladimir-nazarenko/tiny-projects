package com.magnet.entries;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by vladimir on 7/16/16.
 *
 * Writes numbers from NumberProducer to xml file.
 * Configured via DI.
 */
public final class NumXmlWriter {
    public void setProducer(NumberProducer producer) {
        this.producer = producer;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private NumberProducer producer;
    private String fileName;

    /**
     * Iterates over injected NumberProducer object and forms an xml document from its content.
     */
    public void writeXml() {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = null;
        try {
            writer = xof.createXMLStreamWriter(
                            new FileWriter(fileName)
            );
            writer.writeStartDocument("utf-8", "1.0");
            writer.writeStartElement("entries");
            while (producer.hasNext()) {
                writer.writeStartElement("entry");
                    writer.writeStartElement("field");
                        writer.writeCharacters(Integer.toString(producer.getNextInt()));
                    writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndDocument();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();       }
        }
    }
}
