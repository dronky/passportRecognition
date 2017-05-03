import com.asprise.ocr.Ocr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Формат паспорта http://inosmi.ru/infographic/20110701/171472534.html
        String[] data = new String[2]; //Переменная для хранения данных паспорта.
        boolean valid = false;
        boolean parsed = false;
        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
        String s = ocr.recognize(new File[]{new File("input02.jpg")},
                Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT); // PLAINTEXT | XML | PDF | RTF
        //System.out.println("Result: " + s);
        ocr.stopEngine();
        //PNRUS - только для внутренних российских паспортов
        if (s.contains("PNRUS")) {
            valid = true;
            String[] tempData = s.split("\n");
            for (int i = 0; i < tempData.length; i++) {
                if (tempData[i].contains("PNRUS")) {
                    data[0] = tempData[i];
                    data[1] = tempData[i + 1];
                }
            }
        }
        if (valid) {
            System.out.println(data[0]);
            System.out.println(data[1]);
            String name = "";
            String lastName = "";
            String otchestvo = "";
            String seria;
            String number;
            String born;
            String sex;
            String[] nlo = data[0].split("<<");
            int lnEnd = data[0].indexOf("<<");
            lastName = data[0].substring(5, lnEnd); //PNRUS=5
            data[0] = data[0].substring(lnEnd + 2); //затираем фамилию
            int nameEnd = data[0].indexOf("<");
            name = data[0].substring(0, nameEnd); //затираем имя
            data[0] = data[0].substring(nameEnd + 1);
            if (!data[0].substring(0, 2).equals("<<")) {
                int otEnd = data[0].indexOf("<");
                otchestvo = data[0].substring(0, otEnd);
            }
            System.out.println("Last name: " + lastName);
            System.out.println("name: " + name);
            System.out.println("otchestvo: " + otchestvo);
        }
    }
}
