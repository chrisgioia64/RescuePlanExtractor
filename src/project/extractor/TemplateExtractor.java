package project.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.model.FinancialCategory;
import project.model.Template;

public class TemplateExtractor extends Extractor<Template> {
    
    private Map<String, FinancialCategory> map;
    
    public TemplateExtractor(BufferedReader reader) {
        super(reader);
        this.map = new LinkedHashMap<>();
    }
    
    @Override
    public void readLines() {
        String line = null;
        Pattern pattern = Pattern.compile("^\\s{1,3}(\\d(?:\\.\\d{1,2})?)\\.?\\s+(.*)$");
        try {
            while ((line = reader.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.matches()) {
                    String code = m.group(1);
                    String category = m.group(2);
                    FinancialCategory financialCategory = new FinancialCategory(
                            code, category);
                    map.put(code, financialCategory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Override
    public Template createExtractedItem() {
        return new Template(map);
    }

}
