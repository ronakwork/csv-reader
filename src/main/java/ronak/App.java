package ronak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) throws IOException, MissingFileException
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the path of your file");
        String path = sc.next();
        
        CsvReader csv = new CsvReader(path, ";");
        csv.readCSV();
        List<String[]> data = new ArrayList<>();
        data = csv.getData();

        csv.Display(data);
        System.out.println();

        System.out.println("Please enter the target path for your file");
        String targetPath = sc.next();
        sc.close();

        csv.writeCSV(targetPath);
    }
}

