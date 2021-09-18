package project.model;

import java.util.List;

public class EntityReport {
    /** Name of a state, county, or local government entity. */
    private String entityName;
    
    private List<EntityLineItem> lineItems;

    public EntityReport(String entityName, List<EntityLineItem> lineItems) {
        super();
        this.entityName = entityName;
        this.lineItems = lineItems;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<EntityLineItem> getLineItems() {
        return lineItems;
    }

}
