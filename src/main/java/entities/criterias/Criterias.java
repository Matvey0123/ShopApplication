package entities.criterias;

import java.util.List;

public class Criterias {
    private List<Object> criterias;

    public Criterias(List<Object> criterias) {
        this.criterias = criterias;
    }

    public List<Object> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Object> criterias) {
        this.criterias = criterias;
    }
}
