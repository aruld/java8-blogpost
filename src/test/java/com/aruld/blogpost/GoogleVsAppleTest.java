package com.aruld.blogpost;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GoogleVsAppleTest {

  public static void main(String[] args) {
    List<String> google = Arrays.asList("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus");

    List<String> apple = Arrays.asList("ios", "iOS", "iphone", "iPhone", "ipad", "iPad");

    List<Tweet> tweets = TweetReader.fromTweetSet(TweetReader.allTweets);

    // Using anyMatch and method reference
    List<Tweet> googleTweets = tweets.stream().filter(t -> (google.stream().anyMatch(t.text::contains))).collect(toList());

    List<Tweet> appleTweets = tweets.stream().filter(t -> (apple.stream().anyMatch(t.text::contains))).collect(toList());
    System.out.println(googleTweets.size());
    System.out.println(appleTweets.size());

    // Using reduce
    List<Tweet> googleTweets2 = tweets.stream().filter(t -> google.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), (l, r) -> l | r)).collect(toList());

    List<Tweet> appleTweets2 = tweets.stream().filter(t -> apple.stream().reduce(false, (Boolean b, String keyword) -> b || t.text.contains(keyword), (l, r) -> l | r)).collect(toList());
    System.out.println(googleTweets2.size());
    System.out.println(appleTweets2.size());

  }
}
