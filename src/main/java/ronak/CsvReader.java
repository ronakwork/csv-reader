package ronak;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

import de.vandermeer.asciitable.AsciiTable;

public class CsvReader {
    final private String path;
    private String delimiter = ",";
    private List<String[]> data = new ArrayList<>();
    private int features;

    public CsvReader(String path) {
        this.path = path;
    }

    public CsvReader(String path, String delimiter) {
        this.path = path;
        this.delimiter = delimiter;
    }

    // reads csv file and stores it in List<String[]> data
    public void readCSV() throws IOException, MissingFileException{
        File file = new File(path);
        if (!file.exists()) {
            throw new MissingFileException("File not found at the specified path: " + path);
        }

        String current;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((current = br.readLine()) != null) {

                if (current.trim().isEmpty()) {
                    continue;
                }
                String[] line = current.split(delimiter);

                if (features == 0) features = line.length;

                if (line.length < features) {
                    String[] newLine = new String[features];
                    System.arraycopy(line, 0, newLine, 0, line.length);
                    Arrays.fill(newLine, line.length, features, null);
                    line = newLine;
                }

                if (line.length > features) {
                    line = Arrays.copyOfRange(line, 0, features);
                }
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error, file not found. Refer to the stacktrace below");
            e.printStackTrace();
        } 
    }
    
    // return a copy of the List<String[]> data which has stored the csv content
    public List<String[]> getData() {
        return new ArrayList<>(data);
    }

    // returns the number of header fields (attributes)
    public int HeaderFields() {
        return features;
    }

    // displays the content of the csv in tabular format
    public void Display(List<String[]> data) {
        AsciiTable table = new AsciiTable();
        for (String[] CurrentStringArray : data) {
            table.addRule();
            table.addRow(CurrentStringArray);
        }

        String rendered = table.render();
        System.out.print(rendered);
    }

    //write data into a csv file 
    public void writeCSV(String TargetPath) throws IOException, MissingFileException {
        File file = new File(TargetPath);

        // checking if directory exists
        if (!file.exists()) {
            throw new MissingFileException("File not found at the specified path: " + path);
        }

        // check if data is empty
        if (data.isEmpty()) {
            System.out.println("No data to write!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TargetPath))) {
            for(String[] line : data) {
                for(int i = 0; i < line.length; i++ ) {
                    writer.write(line[i]);
                    if (i != line.length - 1) {
                        writer.write(',');
                    }
                } 
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not write into file, check stack trace below for more info");
            e.printStackTrace();
        }
    }

    // append data to an existing csv file 
    public void appendCSV(String TargetPath) throws IOException, MissingFileException {

        // checking if directory exists
        File file = new File(TargetPath);
        if (!file.exists()) {
            throw new MissingFileException("File not found at the specified path: " + path);
        }

        // check if data is empty
        if (data.isEmpty()) {
            System.out.println("No data to write!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TargetPath, true))) {
            for(String[] line : data) {
                for(int i = 0; i < line.length; i++ ) {
                    writer.write(line[i]);
                    if (i != line.length - 1) {
                        writer.write(',');
                    }
                } 
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not write into file, check stack trace below for more info");
            e.printStackTrace();
        }
    }

    // append data but without the header
    public void appendCSV(String TargetPath, boolean ExcludeHeader) throws IOException, MissingFileException {

        // checking if directory exists
        File file = new File(TargetPath);
        if (!file.exists()) {
            throw new MissingFileException("File not found at the specified path: " + path);
        }

        // check if data is empty
        if (data.isEmpty()) {
            System.out.println("No data to write!");
            return;
        }

        // create a copy of original data if the header is to be excluded
        List<String[]> CopyOfData = ExcludeHeader ? data.subList(1, data.size()) : data;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TargetPath, true))) {
            for(String[] line : CopyOfData) {
                for(int i = 0; i < line.length; i++ ) {
                    writer.write(line[i]);
                    if (i != line.length - 1) {
                        writer.write(',');
                    }
                } 
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not write into file, check stack trace below for more info");
            e.printStackTrace();
        }
    }

    public String[] getRow(int index) {
        return data.get(index);
    }
}
