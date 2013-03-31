package com.github.aruld.blogpost;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

abstract class TweetSet {

  TweetSet filter(Predicate<Tweet> p) {
    return filter0(p, new Empty());
  }

  abstract TweetSet filter0(Predicate<Tweet> p, TweetSet accu);

  TweetSet union(TweetSet that) {
    return union0(that, this);
  }

  private TweetSet union0(TweetSet tail, TweetSet orig) {
    if (!tail.isEmpty())
      if (!orig.contains(tail.head()))
        return union0(tail.tail(), orig.incl(tail.head()));
      else return union0(tail.tail(), orig);
    else return orig;
  }

  abstract TweetSet incl(Tweet x);

  abstract Boolean contains(Tweet x);

  abstract Boolean isEmpty();

  abstract Tweet head();

  abstract TweetSet tail();

  abstract TweetSet remove(Tweet tw);

  void foreach(Consumer<Tweet> f) {
    if (!this.isEmpty()) {
      f.accept(this.head());
      this.tail().foreach(f);
    }
  }

  private Tweet findMin0(Tweet curr) {
    if (this.isEmpty()) return curr;
    else if (this.head().retweets < curr.retweets)
      return this.tail().findMin0(this.head());
    else return this.tail().findMin0(curr);
  }

  Tweet findMin() {
    return this.tail().findMin0(this.head());
  }

  int size() {
    if (isEmpty()) return 0;
    else return 1 + tail().size();
  }

  Trending ascendingByRetweet() {
    return ascendingByRetweet0(new EmptyTrending());
  }

  private Trending ascendingByRetweet0(Trending accu) {
    if (!isEmpty())
      return remove(findMin()).ascendingByRetweet0(accu.plus(findMin()));
    else
      return accu;
  }
}

class Empty extends TweetSet {

  @Override
  TweetSet filter0(Predicate<Tweet> p, TweetSet accu) {
    return accu;
  }

  @Override
  TweetSet incl(Tweet x) {
    return new NonEmpty(x, new Empty(), new Empty());
  }

  @Override
  Boolean contains(Tweet x) {
    return false;
  }

  @Override
  Boolean isEmpty() {
    return true;
  }

  @Override
  Tweet head() {
    throw new RuntimeException("Empty.head");
  }

  @Override
  TweetSet tail() {
    throw new RuntimeException("Empty.tail");
  }

  @Override
  TweetSet remove(Tweet tw) {
    return this;
  }
}

class NonEmpty extends TweetSet {
  Tweet elem;
  TweetSet left;
  TweetSet right;

  NonEmpty(Tweet elem, TweetSet left, TweetSet right) {
    this.elem = elem;
    this.left = left;
    this.right = right;
  }

  @Override
  TweetSet filter0(Predicate<Tweet> p, TweetSet accu) {
    if (p.test(this.head()))
      return this.tail().filter0(p, accu.incl(this.head()));
    else return this.tail().filter0(p, accu);
  }

  @Override
  TweetSet incl(Tweet x) {
    if (x.compareTo(elem) < 0)
      return new NonEmpty(elem, left.incl(x), right);
    else if (elem.compareTo(x) < 0)
      return new NonEmpty(elem, left, right.incl(x));
    else return this;
  }

  @Override
  Boolean contains(Tweet x) {
    if (x.compareTo(elem) < 0) return left.contains(x);
    else if (elem.compareTo(x) < 0) return right.contains(x);
    else return true;
  }

  @Override
  Boolean isEmpty() {
    return false;
  }

  @Override
  Tweet head() {
    if (left.isEmpty()) return elem;
    else return left.head();
  }

  @Override
  TweetSet tail() {
    if (left.isEmpty()) return right;
    else return new NonEmpty(elem, left.tail(), right);
  }

  @Override
  TweetSet remove(Tweet tw) {
    if (tw.compareTo(elem) < 0) return new NonEmpty(elem, left.remove(tw), right);
    else if (elem.compareTo(tw) < 0) return new NonEmpty(elem, left, right.remove(tw));
    else return left.union(right);
  }
}

abstract class Trending {
  abstract Trending plus(Tweet tw);

  abstract Tweet head();

  abstract Trending tail();

  abstract Boolean isEmpty();

  void foreach(Consumer<Tweet> f) {
    if (!this.isEmpty()) {
      f.accept(this.head());
      this.tail().foreach(f);
    }
  }

  int size() {
    if (isEmpty()) return 0;
    else return 1 + tail().size();
  }
}

class EmptyTrending extends Trending {

  @Override
  Trending plus(Tweet tw) {
    return new NonEmptyTrending(tw, new EmptyTrending());
  }

  @Override
  Tweet head() {
    throw new RuntimeException();
  }

  @Override
  Trending tail() {
    throw new RuntimeException();
  }

  @Override
  Boolean isEmpty() {
    return true;
  }

  @Override
  public String toString() {
    return "EmptyTrending";
  }
}

class NonEmptyTrending extends Trending {
  final Tweet elem;
  final Trending next;

  NonEmptyTrending(Tweet elem, Trending next) {
    this.elem = elem;
    this.next = next;
  }

  @Override
  Trending plus(Tweet tw) {
    return new NonEmptyTrending(elem, next.plus(tw));
  }

  @Override
  Tweet head() {
    return elem;
  }

  @Override
  Trending tail() {
    return next;
  }

  @Override
  Boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    return "NonEmptyTrending(" + elem.retweets + ", " + next + ")";
  }
}

class GoogleVsApple {

  final static List<String> google = Arrays.asList("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus");
  final static List<String> apple = Arrays.asList("ios", "iOS", "iphone", "iPhone", "ipad", "iPad");

  static Boolean transform(List<String> keywords, String text) {
    if (keywords.isEmpty()) return false;
    else if (text.contains(keywords.stream().findFirst().get())) return true;
    else return transform(keywords.stream().substream(1).collect(toList()), text);
  }

  final static TweetSet googleTweets = TweetReader.allTweets.filter(t -> transform(google, t.text));
  final static TweetSet appleTweets = TweetReader.allTweets.filter(t -> transform(apple, t.text));

  final static TweetSet googleTweets2 = TweetReader.allTweets.filter(t -> google.stream().anyMatch(t.text::contains));
  final static TweetSet appleTweets2 = TweetReader.allTweets.filter(t -> apple.stream().anyMatch(t.text::contains));

  final static Trending trending = googleTweets.union(appleTweets).ascendingByRetweet();
  final static Trending trending2 = googleTweets2.union(appleTweets2).ascendingByRetweet();

}

class AppMain {
  public static void main(String[] args) {
    System.out.println("RANKED:");
    GoogleVsApple.trending.foreach(System.out::println);
  }
}
