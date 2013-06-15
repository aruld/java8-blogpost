package com.github.aruld.blogpost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TweetReader {

  private final static List<Tweet> gizmodoTweets = TweetReader.ParseTweets.getTweetData("gizmodo", TweetData.gizmodo);
  private final static List<Tweet> techCrunchTweets = TweetReader.ParseTweets.getTweetData("TechCrunch", TweetData.TechCrunch);
  private final static List<Tweet> engadgetTweets = TweetReader.ParseTweets.getTweetData("engadget", TweetData.engadget);
  private final static List<Tweet> amazondealsTweets = TweetReader.ParseTweets.getTweetData("amazondeals", TweetData.amazondeals);
  private final static List<Tweet> cnetTweets = TweetReader.ParseTweets.getTweetData("CNET", TweetData.CNET);
  private final static List<Tweet> gadgetlabTweets = TweetReader.ParseTweets.getTweetData("gadgetlab", TweetData.gadgetlab);
  private final static List<Tweet> mashableTweets = TweetReader.ParseTweets.getTweetData("mashable", TweetData.mashable);

  private static final List<List<Tweet>> sources = new ArrayList<List<Tweet>>() {{
    add(gizmodoTweets);
    add(techCrunchTweets);
    add(engadgetTweets);
    add(amazondealsTweets);
    add(cnetTweets);
    add(gadgetlabTweets);
    add(mashableTweets);
  }};

  private static final List<TweetSet> tweetSets = sources.stream().map(TweetReader::toTweetSet).collect(toList());
  public static final TweetSet allTweets = unionOfAllTweetSets(tweetSets, new Empty());

  private static TweetSet unionOfAllTweetSets(List<TweetSet> curSets, TweetSet acc) {
    if (curSets.isEmpty()) return acc;
    else
      return unionOfAllTweetSets(curSets.stream().substream(1).collect(toList()), acc.union(curSets.stream().findFirst().get()));
  }


  public static void main(String[] args) {
    System.out.println(toTweetSet(gizmodoTweets).size());
    System.out.println(fromTweetSet(toTweetSet(gizmodoTweets)).size());
    System.out.println(allTweets.size());
  }

  static TweetSet toTweetSet(List<Tweet> list) {
    return list.stream().reduce(new Empty(), (TweetSet tweetSet, Tweet tweet) -> tweetSet.incl(tweet), (tweetSet, tweetSet2) -> tweetSet.union(tweetSet2));
  }

  public static List<Tweet> fromTweetSet(TweetSet ts) {
    List<Tweet> tweets = new ArrayList<>();
    while (!ts.isEmpty()) {
      tweets.add(ts.head());
      ts = ts.tail();
    }
    return tweets;
  }


  static class ParseTweets {
    static List<Tweet> getTweetData(String user, String json) {
      List<Tweet> tweets = new ArrayList<>();
      try {
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
          JSONObject jo = array.getJSONObject(i);
          tweets.add(new Tweet(user, jo.getString("text"), jo.getInt("retweets")));
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return tweets;
    }

  }
}
