package serializers.search;

public class CriteriaOutput {
    private String criteriaType;
    private String criteriaValue;

    public CriteriaOutput(String criteriaType, String criteriaValue) {
        this.criteriaType = criteriaType;
        this.criteriaValue = criteriaValue;
    }

    public String getCriteriaType() {
        return criteriaType;
    }

    public void setCriteriaType(String criteriaType) {
        this.criteriaType = criteriaType;
    }

    public String getCriteriaValue() {
        return criteriaValue;
    }

    public void setCriteriaValue(String criteriaValue) {
        this.criteriaValue = criteriaValue;
    }
}
