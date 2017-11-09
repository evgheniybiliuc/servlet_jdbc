package pool;

import jdbc.servlet.todo.pool.ExpireGenericPool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestExpireObjectPool {

    private final String DUMMY_FACTORY_RESULT = "Hello world";
    private final int EXPIRATION_TIME = 4000;
    private final int MAX_OBJECT_POOL_SIZE = 10;
    ExpireGenericPool<StringWithId> expireGenericPool;

    @Before
    public void init() {
        expireGenericPool = new ExpireGenericPool.Builder<StringWithId>()
                .setExpireTime(EXPIRATION_TIME)
                .setFactory(() -> new StringWithId(DUMMY_FACTORY_RESULT))
                .setMaxObjectsInPool(MAX_OBJECT_POOL_SIZE)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testNullFactory() {
        new ExpireGenericPool.Builder<String>().setFactory(null)
                .setExpireTime(10)
                .setMaxObjectsInPool(10)
                .build();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroExpirationTime() {
        new ExpireGenericPool.Builder<String>().setFactory(() -> "")
                .setExpireTime(0)
                .setMaxObjectsInPool(10)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpirationTime() {
        new ExpireGenericPool.Builder<String>().setFactory(() -> "")
                .setExpireTime(-1)
                .setMaxObjectsInPool(10)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroObjectsSizeInPool() {
        new ExpireGenericPool.Builder<String>().setFactory(() -> "")
                .setExpireTime(10)
                .setMaxObjectsInPool(0)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeObjectsSizeInPool() {
        new ExpireGenericPool.Builder<String>().setFactory(() -> "")
                .setExpireTime(10)
                .setMaxObjectsInPool(-1)
                .build();
    }

    @Test
    public void testObjectFactoryPoolValue() {
        StringWithId producedValueByFactory = expireGenericPool.get();
        Assert.assertEquals(DUMMY_FACTORY_RESULT, producedValueByFactory.getValue());
    }


    @Test
    public void testObjectInPoolSize() {
        testObjectFactoryPoolValue();
        Assert.assertEquals(1, expireGenericPool.size());
    }

    @Test
    public void testBusyObjectPoolSize() {
        testObjectFactoryPoolValue();
        Assert.assertEquals(1, expireGenericPool.busyObjectSize());
    }

    @Test
    public void testFreeObjectPoolSize() {
        testObjectFactoryPoolValue();
        Assert.assertEquals(0, expireGenericPool.freeObjectSize());
    }

    @Test(expected = NullPointerException.class)
    public void testIfObjectIsExpiredWithNull() {
        expireGenericPool.isExpired(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIfUnrecognizedObjectIsExpired() {
        expireGenericPool.isExpired(new StringWithId("UNRECOGNIZED OBJECT"));
    }

    @Test(expected = NullPointerException.class)
    public void testReturnBackForNull() {
        expireGenericPool.returnBack(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testReturnBackWithUnrecognizedObject() {
        expireGenericPool.returnBack(new StringWithId("UNRECOGNIZED OBJECT"));
    }

    @Test
    public void testReturnBackReceivedObject() {
        StringWithId receivedObj = expireGenericPool.get();

        Assert.assertEquals(1, expireGenericPool.busyObjectSize());
        Assert.assertEquals(0, expireGenericPool.freeObjectSize());
        Assert.assertEquals(1, expireGenericPool.size());

        expireGenericPool.returnBack(receivedObj);

        Assert.assertEquals(0, expireGenericPool.busyObjectSize());
        Assert.assertEquals(1, expireGenericPool.freeObjectSize());
        Assert.assertEquals(1, expireGenericPool.size());

    }

    @Test
    public void testObjectValidObjectExpirationTime() throws InterruptedException {
        StringWithId receivedObj = expireGenericPool.get();
        TimeUnit.MILLISECONDS.sleep(EXPIRATION_TIME + 100);
        Assert.assertTrue(expireGenericPool.isExpired(receivedObj));
    }


    @Test
    public void testConcurrentObjectCreation() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            while (expireGenericPool.busyObjectSize() < 5) {
                expireGenericPool.get();
            }
        });
        //Time for value generation
        TimeUnit.SECONDS.sleep(1);
        Assert.assertEquals(expireGenericPool.busyObjectSize(), 5);
    }

    @Test
    public void testForceRetrieveObjectsIfTheyAreExpired() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        int totalReceivedObjects = MAX_OBJECT_POOL_SIZE / 2;

        executorService.execute(() -> {

            while (expireGenericPool.busyObjectSize() < totalReceivedObjects) {
                expireGenericPool.get();
            }
        });

        //for object instantiation
        TimeUnit.MILLISECONDS.sleep(EXPIRATION_TIME / 5);

        Assert.assertEquals(totalReceivedObjects, expireGenericPool.size());
        Assert.assertEquals(totalReceivedObjects, expireGenericPool.busyObjectSize());
        Assert.assertEquals(0, expireGenericPool.freeObjectSize());

        TimeUnit.MILLISECONDS.sleep(EXPIRATION_TIME * 2);

        Assert.assertEquals(totalReceivedObjects, expireGenericPool.freeObjectSize());
        Assert.assertEquals(0, expireGenericPool.busyObjectSize());

    }
}
