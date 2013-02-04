package com.aruld.blogpost;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class TweetSetTest {
  final static List<String> google = Arrays.asList("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus");
  final static List<String> apple = Arrays.asList("ios", "iOS", "iphone", "iPhone", "ipad", "iPad");

  public static void main(String[] args) {
    List<Tweet> googleTweets = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> (google.stream().anyMatch(t.text::contains))).collect(toList());
    System.out.println(googleTweets.size());

//    List<String> appleTweets = TweetReader.fromTweetSet(TweetReader.allTweets).stream().map((Tweet tweet) -> tweet.text).filter(text -> (apple.stream().anyMatch(keyword -> text.contains(keyword)))).collect(toList());
    List<Tweet> appleTweets = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> (apple.stream().anyMatch(t.text::contains))).collect(toList());
    System.out.println(appleTweets.size());
    Set<Tweet> union = new HashSet<>(googleTweets);
    union.addAll(appleTweets);
    System.out.println(union.size());

    TweetSet googleTweets2 = TweetReader.allTweets.filter(tweet -> (google.stream().anyMatch(tweet.text::contains)));
    System.out.println(googleTweets2.size());
    TweetSet appleTweets2 = TweetReader.allTweets.filter(tweet -> (apple.stream().anyMatch(tweet.text::contains)));
    System.out.println(appleTweets2.size());
    Trending trending = googleTweets2.union(appleTweets2).ascendingByRetweet();
    System.out.println(trending.size());

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

    List<Tweet> googleTweets4 = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> google.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), null)).collect(toList());
    System.out.println(googleTweets4.size());
    List<Tweet> appleTweets4 = TweetReader.fromTweetSet(TweetReader.allTweets).stream().filter(t -> apple.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), null)).collect(toList());
    System.out.println(appleTweets4.size());
    Set<Tweet> union2 = new HashSet<>(googleTweets4);
    union2.addAll(appleTweets4);
    System.out.println(union2.size());
  }
}
