package net.morher.house.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The ResourceManager helps manage {@link AutoCloseable} resources by registering them and
 * providing a single {@link #close()} method to close all of them.
 *
 * <p>When resources are created in a constructor the {@link ConstructionBlock} from {@link
 * #constructionBlock()} can help close resources created in case of a failure.
 *
 * @author Morten Hermansen
 */
public class ResourceManager implements AutoCloseable {
  private final List<AutoCloseable> resources = new ArrayList<>();

  /**
   * Add a resource to the registry.
   *
   * @param resource The resource to add.
   */
  public void add(AutoCloseable resource) {
    this.resources.add(resource);
  }

  /**
   * Try to close all the registered resources. If any of the resources throws an exception during
   * close, the first Exception will be rethrown when all resources are closed.
   */
  @Override
  public void close() throws Exception {
    ArrayList<Exception> exceptions = tryClose();
    if (!exceptions.isEmpty()) {
      throw exceptions.get(0);
    }
  }

  /**
   * Same as {@link #close(), but ignores any exceptions.
   */
  public void closeQuietly() {
    tryClose();
  }

  private ArrayList<Exception> tryClose() {
    ArrayList<Exception> exceptions = new ArrayList<>();
    for (AutoCloseable resource : resources) {
      tryClose(resource, exceptions);
    }
    return exceptions;
  }

  /**
   * Creates a {@link ConstructionBlock}.
   *
   * @return The {@link ConstructionBlcok}.
   */
  public ConstructionBlock constructionBlock() {
    return new ConstructionBlock();
  }

  private void tryClose(AutoCloseable resource, Collection<Exception> exceptions) {
    try {
      resource.close();
    } catch (Exception e) {
      exceptions.add(e);
    }
  }

  /**
   * Tool for managing resources in case of a constructor failure.
   *
   * <ol>
   *   <li>Use {@link ResourceManager#constructionBlock()} in a try-with-resources.
   *   <li>Add created resources to the manager with {@link ResourceManager#add(AutoCloseable)}.
   *   <lI>Then at the end of the try-block mark the ConstructionBlock as {@link
   *       ConstructionBlock#complete() complete}.
   *       <p>If {@link ConstructionBlock#complete()} is not called before the try-with-resources
   *       block ends, {@link ResourceManager#closeQuietly()} is called, and all the registered
   *       resources will be closed.
   *
   * @author Morten Hermansen
   */
  public class ConstructionBlock implements AutoCloseable {
    private boolean constructionComplete;

    public void complete() {
      this.constructionComplete = true;
    }

    @Override
    public void close() {
      if (!constructionComplete) {
        ResourceManager.this.closeQuietly();
      }
    }
  }
}
