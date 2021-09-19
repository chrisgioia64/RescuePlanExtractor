package project.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.Utils;
import project.model.EntityLineItem;
import project.model.EntityReport;
import project.model.FinancialCategory;
import project.model.Template;

public class EntityReportExtractor extends Extractor<EntityReport> {

    private List<EntityLineItem> lineItems;
    private String entityName;
    private Template template;
    
    public EntityReportExtractor(BufferedReader reader, String entity, Template template) {
        super(reader);
        this.lineItems = new LinkedList<>();
        this.entityName = entity;
        this.template = template;
    }

    @Override
    public EntityReport createExtractedItem() {
        return new EntityReport(entityName, lineItems);
    }
    
    private Double getNumber(StringBuilder b) {
        try {
            Double num = Double.parseDouble(b.toString());
            return num;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private String getCanonicalForm(String groupCode) {
        StringBuilder b = new StringBuilder();
        int index = 0;
        for (char c : groupCode.toCharArray()) {
            if (index > 0 && c == '0' && groupCode.charAt(index-1) == '.') {
                
            } else {
                b.append(c);
            }
            index++;
        }
        if (b.length() > 0 && b.charAt(b.length()-1) == '.') {
            b.replace(b.length()-1, b.length(), "");
        }
        return b.toString();
    }

    @Override
    public void readLines() {
        String line = null;
        Pattern pattern = Pattern.compile("^\\s{1,3}(\\d(?:\\.\\d{1,2})?)\\.?\\s(.*)$");
        try {
            while ((line = reader.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.matches()) {
//                    Pattern p2 = Pattern.compile("^\\s{1,3}(\\d(?:\\.\\d{1,2})?)[a-zA-Z ]*"
//                            + "([\\$]?\\d{1,3}[\\.,]\\d{1,3}(?:[\\.,]\\d{1,3})*)\\s+"
//                            + "([\\$]?\\d{1,3}[\\.,]\\d{1,3}(?:[\\.,]\\d{1,3})*)\\s*$");
//                    Pattern p2 = Pattern.compile("^\\s{1,3}(\\d(?:\\.\\d{1,2})?).*((?:[\\$]\\s*)\\d{1,3}[\\d,\\.]*)\\s+((?:[\\$]\\s*)\\d{1,3}[\\d,\\.]*)\\s+$");
                    String groupCode = getCanonicalForm(m.group(1));
                    char[] ary = m.group(2).toCharArray();
                    int i = 0;
                    ParsePhase phase = ParsePhase.CATEGORY;
                    StringBuilder text = new StringBuilder();
                    StringBuilder num1 = new StringBuilder();
                    StringBuilder num2 = new StringBuilder();
                    
                    while (i < ary.length) {
                        char c = ary[i];
                        
                        if (phase.equals(ParsePhase.CATEGORY)) {
                            if (Utils.isDigit(c)) {
                                if (i > 0 && (ary[i-1] == ' ' || ary[i-1] == '$')) {
                                    num1.append(c);
                                    phase = ParsePhase.FIRST_NUMBER;
                                } else {
                                    text.append(c);
                                }
                            } else {
                                text.append(c);
                            }
                        } else if (phase.equals(ParsePhase.FIRST_NUMBER)) {
                            if (c == ' ') {
                                phase = ParsePhase.GAP;
                            } else if (c == ',') {
                                
                            } else {
                                num1.append(c);
                            }
                        } else if (phase.equals(ParsePhase.GAP)) {
                            if (Utils.isDigit(c)) {
                                num2.append(c);
                                phase = ParsePhase.GAP;
                            }
                        } else if (phase.equals(ParsePhase.SECOND_NUMBER)) {
                            if (c == ' ') {
                                phase = ParsePhase.END;
                            } else if (c == ',') {
                                
                            } else {
                                num2.append(c);
                            }
                        } else if (phase.equals(ParsePhase.END)) {
                            
                        }
                        
                        i++;
                    }
                    Double number1 = null;
                    if (num1.length() > 0) {
                        number1 = getNumber(num1);
                    }
                    Double number2 = null;
                    if (num2.length() > 0) {
                        number2 = getNumber(num2);
                    }
                    
                    if (template.get(groupCode) != null) {
                        FinancialCategory cat = template.get(groupCode);
                        String[] templateWords = cat.getText().toLowerCase().split("\\s+");
                        HashSet<String> set = new HashSet<>();
                        for (String templateWord : templateWords) {
                            set.add(templateWord);
                        }
                        boolean found = false;
                        String[] words = text.toString().toLowerCase().split("\\s+");
                        for (String word : words) {
                            if (set.contains(word)) {
                                found = true;
                                break;
                            }
                        }
                        
                        if (!found) {
//                            System.err.println("Invalid category \"" + groupCode + "\"");
//                            System.out.println(line);
                        } else {
                            EntityLineItem report = new EntityLineItem(groupCode, text.toString(), 
                                    number1, number2);
                            lineItems.add(report);
                        }
                        
                    } else {
//                        System.err.println("Invalid group code: " + groupCode);
//                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private enum ParsePhase {
        CATEGORY, FIRST_NUMBER, GAP, SECOND_NUMBER, END
    }

}
