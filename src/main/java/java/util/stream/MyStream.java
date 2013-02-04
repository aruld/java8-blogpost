package java.util.stream;

import java.util.function.Predicate;

/**
 * Stream API Extensions.
 *
 * @param <T> Type of elements.
 *
 * @author Arul Dhesiaseelan (aruld@acm.org)
 */
public interface MyStream<T> extends Stream<T> {

  boolean exists(Predicate<? super T> predicate);
}