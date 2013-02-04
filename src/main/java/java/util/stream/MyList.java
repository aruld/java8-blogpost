package java.util.stream;

import com.aruld.blogpost.Tweet;
import com.aruld.blogpost.TweetReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MyList<E> extends ArrayList<E> {

  public MyList(int initialCapacity) {
    super(initialCapacity);
  }

  public MyList() {
    super();
  }

  public MyList(Collection<? extends E> c) {
    super(c);
  }

  @Override
  public MyStream<E> stream() {
    return new MyReferencePipeline<>(() -> Arrays.spliterator((E[]) this.toArray(), 0, this.size()), StreamOpFlag.IS_SIZED | StreamOpFlag.IS_ORDERED);
  }

  /**
   * Run with -Xbootclasspath/p:target/classes
   */
  public static void main(String[] args) {
    List<String> google = Arrays.asList("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus");

    List<String> apple = Arrays.asList("ios", "iOS", "iphone", "iPhone", "ipad", "iPad");

    List<Tweet> tweets = TweetReader.fromTweetSet(TweetReader.allTweets);

    List<Tweet> googleTweets3 = tweets.stream().filter(tweet -> (new MyList<>(google).stream().exists(tweet.text::contains))).collect(toList());

    List<Tweet> appleTweets3 = tweets.stream().filter(tweet -> (new MyList<>(apple).stream().exists(tweet.text::contains))).collect(toList());
    System.out.println(googleTweets3.size());
    System.out.println(appleTweets3.size());

  }

}