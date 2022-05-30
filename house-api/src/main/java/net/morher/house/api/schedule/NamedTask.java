package net.morher.house.api.schedule;

public class NamedTask implements Runnable {
    private final Runnable delegate;
    private final String name;

    public NamedTask(Runnable delegate, String name) {
        this.delegate = delegate;
        this.name = name;
    }

    @Override
    public void run() {
        delegate.run();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this
                || obj.equals(delegate);
    }
}
