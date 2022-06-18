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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
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

    public static List<Employee> parseXML(String fileNameXML) throws RuntimeException {
        List<Employee> list = new ArrayList<>();
        try{
            File file = new File(fileNameXML);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength() ; i++) {

                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()){

                    Element employee = (Element) node;
                    NodeList fElId = employee.getElementsByTagName("id");
                    Element fElement = (Element) fElId.item(0);
                    NodeList fValue = fElement.getChildNodes();
                    String iD = fValue.item(0).getNodeValue();

                    Element employee1 = (Element) node;
                    NodeList sElFirstName = employee1.getElementsByTagName("firstName");
                    Element sElement = (Element) sElFirstName.item(0);
                    NodeList sValue = sElement.getChildNodes();
                    String firstName = sValue.item(0).getNodeValue();

                    Element employee2 = (Element) node;
                    NodeList thElLastName = employee2.getElementsByTagName("lastName");
                    Element thElement = (Element) thElLastName.item(0);
                    NodeList thValue = thElement.getChildNodes();
                    String lastName = thValue.item(0).getNodeValue();

                    Element employee3 = (Element) node;
                    NodeList fCountry = employee3.getElementsByTagName("country");
                    Element fourCountry = (Element) fCountry.item(0);
                    NodeList fourValue = fourCountry.getChildNodes();
                    String country = fourValue.item(0).getNodeValue();

                    Element employee4 = (Element) node;
                    NodeList fAge = employee4.getElementsByTagName("age");
                    Element fiveAge = (Element) fAge.item(0);
                    NodeList fiveValue = fiveAge.getChildNodes();
                    String age = fiveValue.item(0).getNodeValue();

                    Employee employee5 = new Employee( Long.parseLong(iD),firstName,lastName,country,Integer.parseInt(age));
                    list.add(employee5);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
}
