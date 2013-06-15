package com.github.aruld.blogpost;

import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class TweetSetTest {
  final static List<String> google = Arrays.asList("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus");
  final static List<String> apple = Arrays.asList("ios", "iOS", "iphone", "iPhone", "ipad", "iPad");

  @Test
  public void trendingTest() {
    List<Tweet> googleTweets = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> (google.stream().anyMatch(t.text::contains))).collect(toList());
    assertEquals(38, googleTweets.size());

//    List<String> appleTweets = TweetReader.fromTweetSet(TweetReader.allTweets).stream().map((Tweet tweet) -> tweet.text).filter(text -> (apple.stream().anyMatch(keyword -> text.contains(keyword)))).collect(toList());
    List<Tweet> appleTweets = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> (apple.stream().anyMatch(t.text::contains))).collect(toList());
    assertEquals(150, appleTweets.size());
    Set<Tweet> union = new HashSet<>(googleTweets);
    union.addAll(appleTweets);
    assertEquals(179, union.size());

    TweetSet googleTweets2 = TweetReader.allTweets.filter(tweet -> (google.stream().anyMatch(tweet.text::contains)));
    assertEquals(38, googleTweets2.size());
    TweetSet appleTweets2 = TweetReader.allTweets.filter(tweet -> (apple.stream().anyMatch(tweet.text::contains)));
    assertEquals(150, appleTweets2.size());
    Trending trending = googleTweets2.union(appleTweets2).ascendingByRetweet();
    assertEquals(179, trending.size());

//    TweetSet googleTweets3a = TweetReader.allTweets.filter(tweet -> (new MyList<>(google).stream().exists(tweet.text::contains)));
//    System.out.println(googleTweets3a.size());
//    TweetSet appleTweets3a = TweetReader.allTweets.filter(tweet -> (new MyList<>(apple).stream().exists(tweet.text::contains)));
//    System.out.println(appleTweets3a.size());
//    Trending trending2 = googleTweets3a.union(appleTweets3a).ascendingByRetweet();
//    System.out.println(trending2.size());

//    List<Tweet> googleTweets3b = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(tweet -> (new MyList<>(google).stream().exists(tweet.text::contains))).collect(toList());
//    System.out.println(googleTweets3b.size());
//    List<Tweet> appleTweets3b = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(tweet -> (new MyList<>(apple).stream().exists(tweet.text::contains))).collect(toList());
//    System.out.println(appleTweets3b.size());
//
//    Set<Tweet> union3 = new HashSet<>(googleTweets3b);
//    union3.addAll(appleTweets3b);
//    System.out.println(union3.size());

    List<Tweet> googleTweets4 = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> google.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), (a, b) -> a || b)).collect(toList());
    assertEquals(38, googleTweets4.size());
    List<Tweet> appleTweets4 = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> apple.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), (a, b) -> a || b)).collect(toList());
    assertEquals(150, appleTweets4.size());
    Set<Tweet> union2 = new HashSet<>(googleTweets4);
    union2.addAll(appleTweets4);
    assertEquals(179, union2.size());
  }
}
