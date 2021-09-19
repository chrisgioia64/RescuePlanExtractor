package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import project.extractor.EntityReportExtractor;
import project.extractor.TemplateExtractor;
import project.model.EntityLineItem;
import project.model.EntityReport;
import project.model.Template;

public class TestRunner {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Expecting the filename of the csv as an argument");
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ConfigConstants.TEMPLATE_TEXT));
            TemplateExtractor templateExtractor = new TemplateExtractor(reader);
            templateExtractor.readLines();
            Template template = templateExtractor.createExtractedItem();
            

            File folder = new File(ConfigConstants.TEXT_DIRECTORY + "\\" + ConfigConstants.STATES_DIR);
            List<EntityReport> entityReports = new LinkedList<>();
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    BufferedReader r = new BufferedReader(new FileReader(file));
                    String state = Utils.getPrefix(file.getName());
                    EntityReportExtractor extractor = new EntityReportExtractor(
                            r, state, template);
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
            String originalCsvfile = ConfigConstants.CSV_DIRECTORY + "//" + args[0];
            String newFile = ConfigConstants.CSV_DIRECTORY + getNextAvailableFilename(originalCsvfile);
            createCSVFile(newFile, template, entityReports);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the next available filename 
     * e.g. if filename is output.csv,
     * but there is already an output.csv file,
     * then return output(1).csv
     * @return
     */
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
            return originalFile.getName();
        } else {
            int index = 1;
            String[] items = originalFile.getName().split("\\.");
            String baseFilename = items[0];
            String extension = items.length > 1 ? "." + items[1] : "";
            while (true) {
                String newFilename = baseFilename + "(" + index + ")" + extension;
                if (!filenames.contains(newFilename)) {
                    return newFilename;
                }
                index++;
            }
        }
    }

    public static void createCSVFile(String filename,
            Template template, List<EntityReport> entityReports) throws IOException {
//        FileWriter out = new FileWriter(new File(filename));
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
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
                        Utils.formatDollar(item.getCumulativeExpenditures()),
                        Utils.formatDollar(item.getSinceLastRecoveryPlan()),
                        item.getText(),
                        template.get(item.getNumber()).getText());
            }
        }
        printer.flush();
    }
}
