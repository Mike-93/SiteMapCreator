import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите адрес сайта для создания его карты: ");
        String url = scanner.nextLine();

        System.out.println("Создание карты сайта...");

        LinkSetExecutor linkSetExecutor = new LinkSetExecutor(url);
        String sitemap = new ForkJoinPool().invoke(linkSetExecutor);
        FileWriter fileWriter = new FileWriter();
        fileWriter.writeFiles(sitemap);

        System.out.println("Карта создана!");
    }
}
