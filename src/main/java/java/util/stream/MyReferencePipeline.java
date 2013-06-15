package java.util.stream;

import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A pipeline of elements that are references to objects of type <code>T</code>.
 *
 * @param <T> Type of elements in the upstream source.
 * @param <U> Type of elements in produced by this stage.
 * @see ReferencePipeline
 *
 * @author Arul Dhesiaseelan (aruld@acm.org)
 */
public class MyReferencePipeline<T, U> extends ReferencePipeline<T, U> implements MyStream<U> {

  /**
   * Constructor for the source stage of a Stream.
   *
   * @param source {@code Supplier<Spliterator>} describing the stream
   *               source
   * @param sourceFlags the source flags for the stream source, described
   *                    in {@link StreamOpFlag}
   */
  public MyReferencePipeline(Supplier<? extends Spliterator<?>> source,
       int sourceFlags, boolean parallel) {
    super(source, sourceFlags, parallel);
  }

  /**
   * Constructor for the source stage of a Stream.
   *
   * @param source {@code Spliterator} describing the stream source
   * @param sourceFlags the source flags for the stream source, described
   *                    in {@link StreamOpFlag}
   */
  public MyReferencePipeline(Spliterator<?> source,
       int sourceFlags, boolean parallel) {
    super(source, sourceFlags, parallel);
  }

  @Override
  final boolean opIsStateful() {
    throw new UnsupportedOperationException();
  }

  @Override
  final Sink<T> opWrapSink(int flags, Sink<U> sink) {
    throw new UnsupportedOperationException();
  }


  @Override
  public boolean exists(Predicate<? super U> predicate) {
    // Reuse existing anyMatch implementation
    return evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.ANY));
  }
}