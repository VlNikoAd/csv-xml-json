import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String fileNameCSV = "data.csv";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName2 = "JsonData.json";
        List<Employee> list = parseCSV(columnMapping, fileNameCSV);
        String json = listToJson(list);
        writeString(json, fileName2);

        String fileNameXML = "dataXML.xml";
        String fileName3 = "JsonDataXml.json";
        List<Employee> list1 = parseXML(fileNameXML);
        String jSon2 = listToJson(list1);
        writeString(jSon2,fileName3);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> bean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            list = bean.parse();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        System.out.println(gson.toJson(list));
        return json;
    }

    private static void writeString(String json, String fileName2) {
        try (FileWriter fileWriter = new FileWriter(fileName2)) {
            fileWriter.write(json);
            fileWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Employee> parseXML(String fileNameXML) throws RuntimeException, ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileNameXML));

        Node staff = doc.getDocumentElement();
        NodeList employee = staff.getChildNodes();
        for (int i = 0; i < employee.getLength(); i++) {
            Node node = employee.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                list.add(new Employee
                        (Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent()),
                                element.getElementsByTagName("firstName").item(0).getTextContent(),
                                element.getElementsByTagName("lastName").item(0).getTextContent(),
                                element.getElementsByTagName("country").item(0).getTextContent(),
                                Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent())));
            }
        }
        return list;
    }
}
