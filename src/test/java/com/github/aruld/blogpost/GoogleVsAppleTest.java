package com.github.aruld.blogpost;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class GoogleVsAppleTest {

  @Test
  public void keywordExists() {
    List<String> google = Arrays.asList("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus");

    List<String> apple = Arrays.asList("ios", "iOS", "iphone", "iPhone", "ipad", "iPad");

    List<Tweet> tweets = TweetReader.fromTweetSet(TweetReader.allTweets);

    // Using anyMatch and method reference
    List<Tweet> googleTweets = tweets.stream().filter(t -> (google.stream().anyMatch(t.text::contains))).collect(toList());
    assertEquals(38, googleTweets.size());
    List<Tweet> appleTweets = tweets.stream().filter(t -> (apple.stream().anyMatch(t.text::contains))).collect(toList());
    assertEquals(150, appleTweets.size());

    // Using reduce
    List<Tweet> googleTweets2 = tweets.stream().filter(t -> google.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), (l, r) -> l | r)).collect(toList());
    assertEquals(38, googleTweets2.size());
    List<Tweet> appleTweets2 = tweets.stream().filter(t -> apple.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), (l, r) -> l | r)).collect(toList());
    assertEquals(150, appleTweets2.size());

  }
}
