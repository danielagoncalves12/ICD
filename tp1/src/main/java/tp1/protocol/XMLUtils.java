package tp1.protocol;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils {

	public static String documentToString(Document doc) {
			
	    DOMSource domSource   = new DOMSource(doc);
	    StringWriter writer   = new StringWriter();
	    StreamResult result   = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();

	    try {
		    Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	    return writer.toString();
	}
	
	public static Document stringToDocument(String xmlString) {
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Parser that produces DOM object trees from XML content	       
	    DocumentBuilder builder = null; 									   // API to obtain DOM Document instance
	    
	    try {
	      builder = factory.newDocumentBuilder(); 	                           // Create DocumentBuilder with default configuration
	      return builder.parse(new InputSource(new StringReader(xmlString)));  // Parse the content to Document object
	    } 
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return null;
	}
	
	public static boolean validate(String inputXml, String schemaLocation) throws SAXException, IOException {
	
		// build the schema
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File(schemaLocation);
		Schema schema = factory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		
		// create a source from a string
		Source source = new StreamSource(new StringReader(inputXml));
		
		// check input
		boolean isValid = true;
		try  {
			validator.validate(source);
		} 
		catch (SAXException e) {
			System.err.println("Not valid");
			isValid = false;
		}
		
		return isValid;
	}
}
