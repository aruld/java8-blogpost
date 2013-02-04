package java.util.stream;

import java.util.function.Predicate;

public interface MyStream<T> extends Stream<T> {

  default boolean exists(Predicate<? super T> predicate) {
    return anyMatch(predicate);
  }
}