package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import project.extractor.EntityReportExtractor;
import project.extractor.TemplateExtractor;
import project.model.EntityReport;
import project.model.FinancialCategory;
import project.model.FullReport;
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
//            System.out.println(template);
            
            
            int count = 0;
            File folder = new File(ConfigConstants.TEXT_DIRECTORY + "\\" + ConfigConstants.STATES_DIR);
            List<EntityReport> entityReports = new LinkedList<>();
            for (File file : folder.listFiles()) {
                if (file.isFile() 
//                        && file.getName().contains("Alabama")
                        ) {
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
            FullReport fullReport = new FullReport(entityReports, template);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
