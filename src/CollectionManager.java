import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>Класс, управляющий элементами коллекции</p>
 *
 * @author Вотинцев Евгений
 * @version 1.0
 */
public class CollectionManager {
    static boolean save = true;


    /**
     * <p>Коллекция животных</p>
     */
    private static LinkedHashSet<Animal> an = new LinkedHashSet<>();
    static Set<Animal> animals = Collections.synchronizedSet(an);
    /**
     * <p>Объект класса Date, используется для вывода даты инициализации коллекции</p>
     */
    private static Date date = new Date();

    /**
     * <p>Добавляет элемент в коллекцию</p>
     *
     * @param animal Элемент типа Animal
     * @return Результат добавления, true или false
     */
    public static boolean add(Animal animal, String login) {
        boolean result;
        animal.setOwner(login);
        result = animals.add(animal);
        List<Animal> list = new ArrayList<>();
        animals.stream().sorted().forEach(list::add);
        animals = new LinkedHashSet<>();
        animals.addAll(list);
        try {
            if (DatabaseManager.checkAnimal(animal))
                switch (animal.getClass().toString()) {
                    case "class Sova":
                        DatabaseManager.addElement((Sova) animal);
                        break;
                    case "class Pyatachok":
                        DatabaseManager.addElement((Pyatachok) animal);
                        break;
                    default:
                        DatabaseManager.addElement((WinniePooh) animal);
                        break;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка базы данных");
        }
        return result;
    }

    public static boolean add(Animal animal) {
        boolean result;
        result = animals.add(animal);
        List<Animal> list = new ArrayList<>();
        animals.stream().sorted().forEach(list::add);
        animals = new LinkedHashSet<>();
        animals.addAll(list);
        try {
            if (DatabaseManager.checkAnimal(animal))
                switch (animal.getClass().toString()) {
                    case "class Sova":
                        DatabaseManager.addElement((Sova) animal);
                        break;
                    case "class Pyatachok":
                        DatabaseManager.addElement((Pyatachok) animal);
                        break;
                    default:
                        DatabaseManager.addElement((WinniePooh) animal);
                        break;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка базы данных");
        }
        return result;
    }

    /**
     * <p>Выводит на экран все элементы коллекции</p>
     */
    public static String show() {
        String message = "";
        for (Animal current : animals) {
            message = message.concat(current.toString() + "\n");
        }
        if (animals.isEmpty()) message = "Коллекция не содержит объектов";
        return message.trim();
    }

    /**
     * <p>Удаляет элемент из коллекции по имени</p>
     *
     * @param animal Элемент типа Animal
     */
    public static boolean remove(Animal animal, String login) {
        boolean result = false;
        Iterator<Animal> iterator = animals.iterator();

        while (iterator.hasNext()) {
            Animal animal1 = iterator.next();
            if (animal1.equals(animal) && animal1.getOwner().equals(login)) {
                iterator.remove();
                try {
                    DatabaseManager.removeElement(animal);
                } catch (SQLException e) {
                    System.err.println("Ошибка базы данных");
                }
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * <p>Выводит информацию о коллекции</p>
     */
    public static String info() {
        return "Тип коллекции: linkedHashSet" + "\n" +
                "Дата инициалтзации: " + date.toString() + "\n" +
                "Количество элементов коллекции: " + animals.size();
    }

    /**
     * <p>Удаляет все элементы коллекции, превышающие заданный</p>
     *
     * @param animal Элемент типа Animal
     */
    public static String remove_greater(Animal animal, String login) {
        int size = animals.size();
        Animal an = null;
        int count = 0;
        String message = "";
        for (Iterator<Animal> iterator = animals.iterator(); iterator.hasNext(); ) {
            an = iterator.next();
            if (an.getOwner().equals(login)) {
            if (an.compareTo(animal) > 0) {
                try {
                    DatabaseManager.removeElement(an);
                } catch (SQLException e) {
                    System.err.println("Ошибка базы данных");
                }

                    iterator.remove();
                    count += 1;
                }
            }

        }
        if (size != animals.size()) message = "Удалено элементов: " + count;
        else message = "Нет элементов превышающих элемент c именем: " + "\"" + animal.getName() + "\"";
        return message;
    }

    /**
     * <p>Добавляет элемент в коллукцию, если его значение меньше, чем у минимального элемента</p>
     *
     * @param animal Элемент типа Animal
     * @return Результат добавления, true или false
     */
    public static boolean add_if_min(Animal animal, String login) {
        boolean result = false;
        for (Animal current : animals) {
            if (animal.compareTo(current) < 0) {
                result = add(animal, login);
            }
            break;
        }
        return result;
    }

    public static synchronized String save(Set<Animal> animals, String fileName) throws IOException, TransformerException, ParserConfigurationException {
        ParserXML parser = new ParserXML();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(parser.parseCollection(animals));
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(source, sr);
        String outputstring = sw.toString();
        FileWriter writer = new FileWriter(fileName);
        writer.write(outputstring);
        writer.flush();
        writer.close();
        return "Изменения сохранены";
    }


    public static String help() {
        return ("remove {element}: удалить элемент из коллекции по его значению\n" +
                "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add_if_min {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "remove_greater {element}: удалить из коллекции все элементы, превышающие заданный\n" +
                "info: вывести в стандартный поток вывода информацию о коллекции \n" +
                "import {String path}: добавить в коллекцию все данные из файла\n" +
                "add {element}: добавить новый элемент в коллекцию\n" +
                "save: сохраняет элементы коллекции в файл\n" +
                "exit: выход из клиента\n" +
                "logout: выход из учетной записи\n" +
                "Пример задания объекта: add {\"Sova\":{\"iq\":10}, \"name\":\"a\", \"weight\":50, \"height\":50, \"width\":50, \"color\":PINK}");
    }


    public static String import_(String file) {
        ParserXML parser = new ParserXML();
        String message = "";
        try {
            parser.parseXML(file);
            message = "Данные добавлены";
        } catch (SAXException | IOException e) {
            message = "Ошибка xml файла";
        }
        return message;
    }

}