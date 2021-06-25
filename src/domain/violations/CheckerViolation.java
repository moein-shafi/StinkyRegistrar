package domain.violations;

public class CheckerViolation extends BaseViolation {
    String message;

    public CheckerViolation(String message){
        this.message = message;
    }

    public String getViolationMessage(){
        return message;
    }
}
