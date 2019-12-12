package ar.iua.edu.webIII.proyecto.viano.model.dto;

public class ChangeSensitiveDataDto {
    private String password;
    private Object newValue;

    public ChangeSensitiveDataDto() {
    }

    public ChangeSensitiveDataDto(String password, Object newValue) {
        this.password = password;
        this.newValue = newValue;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }
}
