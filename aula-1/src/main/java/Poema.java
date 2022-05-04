import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Eng. Porfírio Filipe
 *
 */

/**
 * Classe para manipulação de poemas
 */

public class Poema {
	static String contexto = "WebContent\\xml\\";
	Document D = null; // representa a arvore DOM com o poema

	/**
	 * @param XMLdoc - documento XML
	 */
	public Poema(String XMLdoc) {
		File f = new File(contexto);
		if(!(f.exists() && f.isDirectory())) { 
			contexto = "src\\main\\webapp\\xml\\";
		}
		XMLdoc = contexto + XMLdoc;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		// check if factory is ignoring whitespaces in elements
	    System.out.println("Ignora elementos com espaços: " + docBuilderFactory.isIgnoringElementContentWhitespace());
		// docBuilderFactory.setValidating(true); // ativa o DTD
		docBuilderFactory.setIgnoringComments(true);
		// check if factory is ignoring comments
	    System.out.println("Ignora comentários: " + docBuilderFactory.isIgnoringComments());
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			File sourceFile = new File(XMLdoc);
			D = docBuilder.parse(sourceFile);
		} catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
	}

	/**
	 * menu com as opções pedidas no enunciado
	 */
	public void menu() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Menu Poema ***");
			System.out.println("1 - Apresenta o poema na sua forma escrita clássica.");
			System.out.println("2 – Classifica as estrofes quanto ao número de versos.");
			System.out.println("3 – Acrescenta um verso no fim da estrofe indicada.");
			System.out.println("4 – Remove uma determinada estrofe.");
			System.out.println("5 – Indica os versos que contêm determinada palavra.");
			System.out.println("0 - Termina!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				apresenta();
				break;
			case '2':
				classifica();
				break;
			case '3':
				System.out.println("Indique o numero da estrofe:");
				short i = sc.nextShort();
				sc.nextLine();
				System.out.println("Escreva o verso:");
				String verso = sc.nextLine();
				if (acrescenta(i, verso))
					apresenta();
				else
					System.out.println("Não acrescentou!");
				break;
			case '4':
				System.out.println("Indique o numero da estrofe a remover:");
				short r = sc.nextShort();
				sc.nextLine();
				if (remove(r)) {
					apresenta();
					System.out.println("Removeu a " + r + "ª estrofe.");
				}
				else
					System.out.println("Não removeu!");
				break;
			case '5':
				System.out.println("Escreva a palavra:");
				String palavra = sc.nextLine();
				indica(palavra);
				break;
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}

	/**
	 * @param palavra - palavra a procurar
	 * @param verso - frase que corresponde a um verso
	 * @return - true se encontrou a palavra no verso (está presente)
	 */
	private boolean estaPresente(String palavra, String verso) {
		StringTokenizer st = new StringTokenizer(verso);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.compareToIgnoreCase(palavra) == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + ",") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + ".") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + ":") == 0)
				return true;
			/*if (token.compareToIgnoreCase(palavra + "...") == 0)
				return true;*/
			if (token.compareToIgnoreCase(palavra + "!") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + "?") == 0)
				return true;
			if (token.compareToIgnoreCase("-" + palavra) == 0)
				return true;
			if (token.compareToIgnoreCase("(" + palavra + ")") == 0 || token.compareToIgnoreCase("(" + palavra) == 0
					|| token.compareToIgnoreCase(palavra + ")") == 0)
				return true;
			if (token.compareToIgnoreCase("'" + palavra + "'") == 0 || token.compareToIgnoreCase("'" + palavra) == 0
					|| token.compareToIgnoreCase(palavra + "'") == 0)
				return true;
			if (token.compareToIgnoreCase("\"" + palavra + "\"") == 0 || token.compareToIgnoreCase("\"" + palavra) == 0
					|| token.compareToIgnoreCase(palavra + "\"") == 0)
				return true;
		}
		return false;
	}
	
	private String escreveExtenso(short numero) {
		switch (numero) {
		case 1:
			return "Monástico";
		case 2:
			return "Dístico ou parelha";
		case 3:
			return "Terceto";
		case 4:
			return "Quadra";
		case 5:
			return "Quintilha";
		case 6:
			return "Sextilha";
		case 7:
			return "Sétima";
		case 8:
			return "Oitava";
		case 9:
			return "Nona";
		case 10:
			return "Décima";
		default:
			return "Irregular ("+numero+")";
		}
	}

	/**
	 * Inicia o percurso em profundidade na árvore DOM
	 */
	public void traverse() {
		System.out.println("Percurso em profundidade realizado pelo traverse:");
		traverse(D.getDocumentElement());
	}

	public void traverse(Node node) {
		System.out.println("node: " + node.getNodeName() + "(" + node.getNodeValue() + ")");
		if (node.hasChildNodes()) {
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				traverse(child);
			}
		}
	}

	public void apresenta() {
		Element root = D.getDocumentElement();
		NodeList titulos = root.getElementsByTagName("título");
		if(titulos.getLength()==1) {
			Element titulo = (Element) titulos.item(0);
			System.out.println("Título: " + titulo.getTextContent());
			System.out.println();
		}
		else {
			System.out.println("Tem de existir um único título no poema!");
			return;
		}
		NodeList estrofes = root.getElementsByTagName("estrofe");
		for (int e = 0; e < estrofes.getLength(); e++) {
			Element estrofe = (Element) estrofes.item(e);
			NodeList versos = estrofe.getElementsByTagName("verso");
			for (int i = 0; i < versos.getLength(); i++)
				System.out.println(versos.item(i).getTextContent());
			System.out.println();
		} // vários autores
		NodeList autores = root.getElementsByTagName("autor");
		if(autores.getLength()>0) 
			for (int e = 0; e < autores.getLength(); e++) {
				Element autor = (Element) autores.item(e);
				System.out.println("Autor: " + autor.getTextContent());
			}
		else 
			System.out.println("Tem de existir pelo menos um autor no poema!");	
	}

	public void classifica() {
		System.out.println("Classificação das estrofes quanto à quantidade de versos:");
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		for (int e = 0; e < estrofes.getLength(); e++) {
			Element estrofe = (Element) estrofes.item(e);
			NodeList versos = estrofe.getElementsByTagName("verso");
			System.out.println(e + 1 + "ª estrofe ("+versos.getLength()+"): "+
			escreveExtenso((short)versos.getLength())+" ");
		}
	}

	public boolean acrescenta(short numEstrofe, String verso) {
		System.out.println("Acrescenta o verso \"" + verso + "\" à estrofe " + numEstrofe + ".");
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		if (numEstrofe>0 && numEstrofe <= estrofes.getLength()) {
			Element estrofe = (Element) estrofes.item(numEstrofe - 1);
			Element vers = D.createElement("verso");
			vers.setTextContent(verso);
			estrofe.appendChild(vers);
			return true;
		}
		return false;
	}

	public boolean remove(short numEstrofe) {
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		if (numEstrofe>0 && numEstrofe <= estrofes.getLength()) {
			Element estrofe = (Element) estrofes.item(numEstrofe - 1);
			estrofe.getParentNode().removeChild(estrofe);
			return true;
		}
		return false;
	}

	public void indica(String palavra) {
		System.out.println("Indica os versos com a  palavra \"" + palavra + "\"");
		Element root = D.getDocumentElement();
		NodeList versos = root.getElementsByTagName("verso");
		for (int i = 0; i < versos.getLength(); i++)
			if (estaPresente(palavra, versos.item(i).getTextContent()))
				System.out.println(versos.item(i).getTextContent());
	}

	public static void main(String[] args) {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current absolute path is: " + s);
		File f = new File(contexto);
		if(!(f.exists() && f.isDirectory())) { 
			contexto = "src\\main\\webapp\\xml\\";
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("Indique o nome do ficheiro (em " + contexto + ") com o poema (ex: poema.xml):");
		String poemaFileName = sc.nextLine();
		if (poemaFileName.length() == 0) {
			poemaFileName = "poema.xml";
			System.out.println("Foi assumido o poema representado em: " + contexto + poemaFileName);
		}
		Poema pm = new Poema(poemaFileName);
		pm.traverse();
		pm.menu();
		sc.close();
	}
}
