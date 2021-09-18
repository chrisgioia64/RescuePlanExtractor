package project.model;

public class EntityLineItem {
    private String number;
    private String text;
    private Double cumulativeExpenditures;
    private Double sinceLastRecoveryPlan;
    
    
    public EntityLineItem(String number, String text, Double cumulativeExpenditures, Double sinceLastRecoveryPlan) {
        super();
        this.number = number;
        this.text = text;
        this.cumulativeExpenditures = cumulativeExpenditures;
        this.sinceLastRecoveryPlan = sinceLastRecoveryPlan;
    }

    public String getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
    
    
    public Double getCumulativeExpenditures() {
        return cumulativeExpenditures;
    }

    public Double getSinceLastRecoveryPlan() {
        return sinceLastRecoveryPlan;
    }




    public class Cost {
        private boolean isSet;
        private double value;
        
        public Cost(boolean isSet, double value) {
            super();
            this.isSet = isSet;
            this.value = value;
        }
        public boolean isSet() {
            return isSet;
        }
        public double getValue() {
            return value;
        }
    }

}
