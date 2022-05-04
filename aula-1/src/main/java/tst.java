import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class tst {

	public static void traverse(Node node) {
		System.out.println("node: "
		+ node.getNodeName() 
		+ "(" + node.getNodeValue() + ")");
		if (node.hasChildNodes()) {
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				traverse(child);
			}
		}
	}

	public static void main(String[] args) {
		String contexto = "src\\main\\webapp\\xml\\";
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder=null;
		Document Doc=null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Doc = docBuilder.parse(contexto+"poema.xml");
		} catch (ParserConfigurationException| SAXException | IOException e) {
			e.printStackTrace();
			return;
		} 
		System.out.println("Percurso em profundidade realizado pelo traverse:");
		tst.traverse(Doc.getDocumentElement());
	}

}
