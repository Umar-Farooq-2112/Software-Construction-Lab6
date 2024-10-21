/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import java.time.Instant;
import java.util.*;

public class SocialNetworkTest {

    // 1. Empty List of Tweets
    @Test
    public void testEmptyListOfTweets() {
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.isEmpty()); 
    }

    // 2. Tweets Without Mentions
    @Test
    public void testTweetsWithoutMentions() {
        List<Tweet> tweets = Arrays.asList(
                new Tweet(1, "alice", "Just a random thought.", Instant.now()),
                new Tweet(2, "bob", "Another tweet without a mention.", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.isEmpty()); 
    }

    // 3. Single Mention
    @Test
    public void testSingleMention() {
        List<Tweet> tweets = Arrays.asList(
                new Tweet(1, "alice", "Hello @bob!", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("alice"));
        assertTrue(followsGraph.get("alice").contains("bob"));
    }

    // 4. Multiple Mentions
    @Test
    public void testMultipleMentions() {
        List<Tweet> tweets = Arrays.asList(
                new Tweet(1, "alice", "Hello @bob and @charlie!", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("alice"));
        assertTrue(followsGraph.get("alice").contains("bob"));
        assertTrue(followsGraph.get("alice").contains("charlie"));
    }

    // 5. Multiple Tweets from One User
    @Test
    public void testMultipleTweetsFromOneUser() {
        List<Tweet> tweets = Arrays.asList(
                new Tweet(1, "alice", "Hello @bob!", Instant.now()),
                new Tweet(2, "alice", "Hey @charlie!", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("alice"));
        assertTrue(followsGraph.get("alice").contains("bob"));
        assertTrue(followsGraph.get("alice").contains("charlie"));
    }

    // 6. Empty Graph for influencers()
    @Test
    public void testEmptyGraphForInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    // 7. Single User Without Followers
    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>()); 
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    // 8. Single Influencer
    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(1, influencers.size());
        assertEquals("bob", influencers.get(0)); 
    }

    // 9. Multiple Influencers
    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob", "charlie")));
        followsGraph.put("bob", new HashSet<>(Arrays.asList("charlie")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(2, influencers.size());
        assertEquals("charlie", influencers.get(0)); 
        assertEquals("bob", influencers.get(1)); 
    }

    // 10. Tied Influence
    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        followsGraph.put("charlie", new HashSet<>(Arrays.asList("bob")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(1, influencers.size());
        assertEquals("bob", influencers.get(0)); 
    }
}
