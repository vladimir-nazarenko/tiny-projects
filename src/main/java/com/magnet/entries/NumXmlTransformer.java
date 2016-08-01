package com.magnet.entries;

import org.springframework.core.io.Resource;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by vladimir on 7/16/16.
 *
 * Transforms xml file using xslt.
 * Configured via DI.
 */
public class NumXmlTransformer {
    private String dataFileName;
    private String outputFileName;
    private InputStream styleInputStream;

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public void setStyleResource(String path) {
        styleInputStream = getClass().getResourceAsStream(path);
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void transformXml() {
        try (FileReader data   = new FileReader(dataFileName);
             FileWriter result = new FileWriter(outputFileName)){
            TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
            Source xsltSource = new StreamSource(styleInputStream);
            Source dataSource = new StreamSource(data);
            Transformer transformer = factory.newTransformer(xsltSource);

            transformer.transform(dataSource, new StreamResult(result));

        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (styleInputStream != null)
                    styleInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
