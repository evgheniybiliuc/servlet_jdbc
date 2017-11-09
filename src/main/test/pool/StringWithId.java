package pool;

public class StringWithId {
    private static int counter = 0;
    private final int id = counter++;
    private final String value;

    public StringWithId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "pool.StringWithId{Value:" + value + " , Id:" + id + "}";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StringWithId)) return false;

        StringWithId that = (StringWithId) obj;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String getValue() {
        return value;
    }
}
