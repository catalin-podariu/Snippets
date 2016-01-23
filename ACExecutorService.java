package Utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * based on farenda.com/java/java-auto-closeable-executor-service
 *
 * @author catalin.podariu@gmail.com
 */
public class ACExecutorService implements AutoCloseable, ExecutorService {

    private ExecutorService executor;

    public ACExecutorService(ExecutorService exec) {
        Objects.requireNonNull(exec, "ExecutorService must NOT be null!");
        this.executor = exec;
    }

    // static factory
    public static ACExecutorService newCached() {
        System.out.println("Creating a new CachedThreadPool executor.");
        return new ACExecutorService(Executors.newCachedThreadPool());
    }

    // static factory
    public static ACExecutorService newFixed(int nThreads) {
        System.out.println("Creating a new FixedThreadPool executor.");
        return new ACExecutorService(Executors.newFixedThreadPool(nThreads));
    }

    // static factory
    public static ACExecutorService newScheduled(int corePoolSize) {
        System.out.println("Creating a new ScheduledPool executor.");
        return new ACExecutorService(Executors.newScheduledThreadPool(corePoolSize));
    }

    // static factory
    public static ACExecutorService newSingleThread() {
        System.out.println("Creating a new SingleThread executor.");
        return new ACExecutorService(Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }

    @Override
    public void close() {
        System.out.println("Shutting down custom ACExecutor service.");
        executor.shutdown();
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executor.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        return executor.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
            long timeout, TimeUnit unit)
            throws InterruptedException {
        return executor.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return executor.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
            long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return executor.invokeAny(tasks, timeout, unit);
    }
}
