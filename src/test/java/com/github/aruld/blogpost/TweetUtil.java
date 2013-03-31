package com.github.aruld.blogpost;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.MyList;

import static org.junit.Assert.assertTrue;

public class TweetUtil {

  /**
   * Run with -Xbootclasspath/p:target/classes
   */
  @Test
  public void tweetExists() {
    final List<String> keywords = Arrays.asList("brown", "fox", "dog", "pangram");
    final String tweet = "The quick brown fox jumps over a lazy dog. #pangram http://www.rinkworks.com/words/pangrams.shtml";

    // legacy pre-JDK 8
    assertTrue(exists(keywords, tweet));

    // take 1
    assertTrue(keywords.stream().anyMatch(keyword -> tweet.contains(keyword)));

    // take 2 using method reference
    assertTrue(keywords.stream().anyMatch(tweet::contains));

    // take 3 using reduce()
    assertTrue(keywords.stream().reduce(false, (Boolean b, String keyword) -> b || tweet.contains(keyword), (l, r) -> l | r));

    // take 4 custom stream API
    MyList<String> myList = new MyList<>(keywords);
    assertTrue(myList.stream().exists(tweet::contains));
  }

  public static boolean exists(List<String> keywords, String tweet) {
    for (String keyword : keywords) {
      if (tweet.contains(keyword)) return true;
    }
    return false;
  }

}