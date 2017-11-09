package jdbc.servlet.todo.util.session;

public enum SessionAttributes {
    EMAIL("_e"),LOGIN("_l"), USER_ENC_KEY("enc");
    private String value;

    SessionAttributes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
