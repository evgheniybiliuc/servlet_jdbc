package jdbc.servlet.todo.pool;

import jdbc.servlet.todo.util.Numbers;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ExpireGenericPool<T> implements ExpirePool<T> {

    protected final BlockingQueue<T> freeObjects;
    protected final Map<T, Long> busyObjects;
    private final Supplier<T> factory;
    private final int maxObjectsInPool;
    private final AtomicInteger counter;
    private final long expireTime;


    private ExpireGenericPool(Builder<T> builder) {

        maxObjectsInPool = builder.maxObjectsInPool;
        expireTime = builder.expireTime;
        counter = new AtomicInteger(0);
        freeObjects = new LinkedBlockingDeque<>(maxObjectsInPool);
        busyObjects = new ConcurrentHashMap<>(maxObjectsInPool);
        factory = builder.factory;
         runExpiredTracker();
    }

    private void runExpiredTracker() {

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> busyObjects.forEach((key, value) -> {
            if (isExpired(key)) {
                returnBack(key);
            }
        }), 0, expireTime / 2, TimeUnit.MILLISECONDS);

    }

    @Override
    public T get() {
        try {
            if (freeObjects.isEmpty() && counter.get() < maxObjectsInPool) {
                counter.incrementAndGet();
                T newObj = factory.get();
                busyObjects.put(newObj, System.currentTimeMillis());
                return newObj;
            }

            T freeObj = freeObjects.take();
            busyObjects.put(freeObj, System.currentTimeMillis());
            return freeObj;
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public int size() {
        return counter.get();
    }

    @Override
    public void returnBack(T obj) {
        Long idleObject = busyObjects.remove(obj);
        if (idleObject == null)
            throw new NoSuchElementException("Unrecognized object: " + obj);
        freeObjects.offer(obj);
    }

    @Override
    public boolean isExpired(T obj) {
        Long objectLifeTime = busyObjects.get(obj);
        if (objectLifeTime == null) {

            if (freeObjects.contains(obj)) {
                return true;
            }


            throw new NoSuchElementException("Unrecognized object : " + obj);
        }
        return System.currentTimeMillis() - objectLifeTime > expireTime;
    }

    @Override
    public long getExpirationTime() {
        return expireTime;
    }

    @Override
    public int busyObjectSize() {
        return busyObjects.size();
    }

    @Override
    public int freeObjectSize() {
        return freeObjects.size();
    }

    public static class Builder<T> {
        private Supplier<T> factory;
        private int maxObjectsInPool;
        private long expireTime;

        public Builder<T> setFactory(Supplier<T> factory) {
            this.factory = factory;

            return this;
        }

        public Builder<T> setMaxObjectsInPool(int maxObjectsInPool) {
            this.maxObjectsInPool = maxObjectsInPool;
            return this;
        }

        public Builder<T> setExpireTime(long expireTime) {
            this.expireTime = expireTime;
            return this;
        }


        public ExpireGenericPool<T> build() {
            Objects.requireNonNull(factory);

            Numbers.require(maxObjectsInPool, (number) -> number > 0);

            Numbers.require(expireTime, (number) -> number > 0);
            return new ExpireGenericPool<>(this);
        }


    }
}