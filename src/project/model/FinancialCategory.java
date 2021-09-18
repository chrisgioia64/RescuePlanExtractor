package project.model;

public class FinancialCategory {
    private String number;
    private String text;
    
    public FinancialCategory(String number, String text) {
        super();
        this.number = number;
        this.text = text;
    }
    public String getNumber() {
        return number;
    }
    public String getText() {
        return text;
    }
    @Override
    public String toString() {
        return "FinancialCategory [number=" + number + ", text=" + text + "]";
    }
}