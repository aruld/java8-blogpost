package com.github.aruld.blogpost;

public class Tweet implements Comparable<Tweet> {
  final public String user;
  final public String text;
  final public int retweets;

  public Tweet(String user, String text, int retweets) {
    this.user = user;
    this.text = text;
    this.retweets = retweets;
  }

  @Override
  public int compareTo(Tweet that) {
    return (text.compareTo(that.text));
  }

  @Override
  public String toString() {
    return "User: " + user + "\n" +
      "Text: " + text + " [" + retweets + "]";
  }
}
