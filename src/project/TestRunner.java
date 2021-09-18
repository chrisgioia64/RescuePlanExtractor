package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import project.extractor.EntityReportExtractor;
import project.extractor.TemplateExtractor;
import project.model.EntityLineItem;
import project.model.EntityReport;
import project.model.FinancialCategory;
import project.model.Template;

public class TestRunner {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ConfigConstants.TEMPLATE_TEXT));
            TemplateExtractor templateExtractor = new TemplateExtractor(reader);
            templateExtractor.readLines();
            Template template = templateExtractor.createExtractedItem();
            for (FinancialCategory category : template) {
                System.out.println(category);
            }

            File folder = new File(ConfigConstants.TEXT_DIRECTORY + "\\" + ConfigConstants.STATES_DIR);
            List<EntityReport> entityReports = new LinkedList<>();
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    BufferedReader r = new BufferedReader(new FileReader(file));
                    String state = Utils.getPrefix(file.getName());
                    System.out.println(state);
                    EntityReportExtractor extractor = new EntityReportExtractor(
                            r, Utils.getPrefix(file.getName()), template);
                    extractor.readLines();
                    entityReports.add(extractor.createExtractedItem());
                    r.close();
                }
            }

            // Write output to csv
            try {
                Files.createDirectory(Paths.get(ConfigConstants.CSV_DIRECTORY));
            } catch (IOException ioe) {
            }
            String originalCsvfile = ConfigConstants.CSV_DIRECTORY + "//output.csv";
            String newFile = ConfigConstants.CSV_DIRECTORY + getNextAvailableFilename(originalCsvfile);
            System.out.println(newFile);
            createCSVFile(newFile, template, entityReports);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static String getNextAvailableFilename(String filename) {
        File originalFile = new File(filename);
        File folder = new File(ConfigConstants.CSV_DIRECTORY);
        File[] files = folder.listFiles();
        HashSet<String> filenames = new HashSet<String>();
        for (File file : files) {
            filenames.add(file.getName());
        }
//        System.out.println(filenames);
        if (!filenames.contains(originalFile.getName())) {
            return filename;
        } else {
            int index = 1;
            Pattern pattern = Pattern.compile("^(\\w+)(\\.\\w+)$");
            Matcher m = pattern.matcher(originalFile.getName());
            if (m.matches()) {
            }
            String baseFilename = m.group(1);
            while (true) {
                String newFilename = baseFilename + "(" + index + ")" + m.group(2);
                if (!filenames.contains(newFilename)) {
                    return newFilename;
                }
                index++;
            }
        }
    }

    public static void createCSVFile(String filename,
            Template template, List<EntityReport> entityReports) throws IOException {
        FileWriter out = new FileWriter(filename);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(
                "Entity", "Code", 
                "Cumulative expenditures to date", 
                "Amount spent since last recovery plan", 
                "Category (in individual report)", 
                "Category (in template document)"));
        new CSVPrinter(out, CSVFormat.DEFAULT);

        for (EntityReport entityReport : entityReports) {
            for (EntityLineItem item : entityReport.getLineItems()) {

                printer.printRecord(
                        entityReport.getEntityName(),
                        item.getNumber(),
                        item.getCumulativeExpenditures(),
                        item.getSinceLastRecoveryPlan(),
                        item.getText(),
                        template.get(item.getNumber()).getText());
            }
        }
        printer.flush();
    }
}
