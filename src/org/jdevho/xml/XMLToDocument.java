package org.jdevho.xml;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Basic example to reading simple XML into a w3c.org.Document and then
 * outputting the DOM as an (formatted) XML String.
 * 
 * @author jdevho
 * 
 */
public class XMLToDocument {

    final static Logger logger = Logger.getAnonymousLogger();

    final static String xml = "./src/test/resources/planet.xml";
    Document document;

    @Test
    public void read_test() {

        document = getDocument(xml);

        NodeList nodes = document.getElementsByTagName("name");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            logger.info(dumpNode(n));
        }
    }

    @Test
    public void write_test() {

        document = getDocument(xml);
        String s = writeDocument(document);
        logger.info(s);
    }

    private String dumpNode(Node node) {
        
        if (node == null) {
            Assert.fail("Node is null!");
        }
        
        StringBuffer buf = new StringBuffer();

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) node;
            NodeList namedList = element.getElementsByTagName("*");
            NodeList children = element.getChildNodes();
            NamedNodeMap attributes = element.getAttributes();

            buf.append("Dump Node for node: " + element.getTagName() + "\n");
            buf.append("nodename: " + element.getNodeName() + "\n");
            buf.append("nodevalue: " + element.getNodeValue() + "\n");
            buf.append("nodetype: " + element.getNodeType() + "\n");
            buf.append("\tnumber of elements: " + namedList.getLength() + "\n");
            buf.append("\tnumber of children: " + children.getLength() + "\n");
            buf.append("\tnumber of attributes: " + attributes.getLength() + "\n");
        }
        return buf.toString();
    }

    private Document getDocument(String xmlpath) {

        if (xmlpath == null) {
            Assert.fail("XML path is null!");
        }
        
        File file = new File(xmlpath);
        Assert.assertTrue(file.exists());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            Assert.fail("Failed to read XML!", e);
        }
        return document;
    }

    private String writeDocument(Document document) {
        
        if (document == null) {
            Assert.fail("Document is null!");
        }
        
        try {
            Writer writer = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}
