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

  public <S> MyReferencePipeline(Supplier<? extends Spliterator<S>> supplier, int sourceFlags) {
    super((Supplier) supplier, sourceFlags);
  }

  @Override
  public boolean exists(Predicate<? super U> predicate) {
    // Reuse existing anyMatch implementation
    return pipeline(MatchOp.match(predicate, MatchOp.MatchKind.ANY));
  }
}