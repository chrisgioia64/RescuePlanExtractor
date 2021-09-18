package project.model;

import java.util.Iterator;
import java.util.Map;

public class Template implements Iterable<FinancialCategory> {
    
    // Map from number to the financial category
    private Map<String, FinancialCategory> map;
    
    public Template(Map<String, FinancialCategory> map) {
        super();
        this.map = map;
    }
    
    public FinancialCategory get(String code) {
        return map.get(code);
    }
    
    @Override
    public Iterator<FinancialCategory> iterator() {
        return map.values().iterator();
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (String key : map.keySet()) {
            b.append(key + "\n");
        }
        return b.toString();
    }
}
