package zagurskiy.fit.bstu.todolist.repository;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import lombok.SneakyThrows;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class XMLHelper {
    private static final String FILE_NAME = "categories.xml";

    public static void writeXML(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);

        if (file.exists()) {
            return;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Categories");
            doc.appendChild(root);

            for (ActivityType t : ActivityType.values()) {
                Element Details = doc.createElement("Category");
                root.appendChild(Details);

                Element title = doc.createElement("Title");
                title.appendChild(doc.createTextNode(t.toString()));
                Details.appendChild(title);

                Element category = doc.createElement("Value");
                category.appendChild(doc.createTextNode(t.getDisplayName()));
                Details.appendChild(category);
            }

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer tran = tranFactory.newTransformer();
            DOMSource src = new DOMSource(doc);

            FileOutputStream fos = new FileOutputStream(file);

            StreamResult result = new StreamResult(fos);
            tran.transform(src, result);

            fos.close();
        } catch (Exception e) {
        }
    }

    @SneakyThrows
    public static String getTitleByCategory(Context context, String title) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(file);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression xpeTitle = xpath.compile("/Categories/Category[Title='" + title + "']/Value/text()");
        NodeList nodeTitle = (NodeList) xpeTitle.evaluate(doc, XPathConstants.NODESET);

        return nodeTitle.item(0).getNodeValue();
    }


    public static void isExist(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {

        }
    }
}
