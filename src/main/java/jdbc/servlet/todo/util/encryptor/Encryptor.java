package jdbc.servlet.todo.util.encryptor;

public interface Encryptor<T> {
    T encrypt(T t, EncryptionStrategy<T> strategy);

    T encrypt(T s);

    @FunctionalInterface
    interface EncryptionStrategy<K> {
        K encrypt(K k);
    }
}
