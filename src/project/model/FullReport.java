package project.model;

import java.util.List;

public class FullReport {
    private List<EntityReport> entityReports;
    private Template template;
    
    public FullReport(List<EntityReport> entityReports, Template template) {
        super();
        this.entityReports = entityReports;
        this.template = template;
    }

    public List<EntityReport> getEntityReports() {
        return entityReports;
    }

    public Template getTemplate() {
        return template;
    }
    
}
