package java.util.stream;

import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.Supplier;

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