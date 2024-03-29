import java.io.Serializable;

public class Command implements Serializable {
    private static final long serialVersionUID = 2;
    private String login;
    private String password;
    private String email;
    private String name;
    private Animal object;
    private String file;
    private String token;
    private Status status;
    private int attempts;

    public int getAttempts() {
        return attempts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObject(Animal object) {
        this.object = object;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Animal getObject() {
        return object;
    }

    public String getFile() {
        return file;
    }

    public String getToken() {
        return token;
    }
}
