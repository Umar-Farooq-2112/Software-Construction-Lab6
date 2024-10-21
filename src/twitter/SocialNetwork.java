/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
    	String author;
        Map<String, Set<String>> graph = new HashMap<>();

    	for (Tweet tweet:tweets) {
    		author = tweet.getAuthor().toLowerCase();
    		String[] words = tweet.getText().split(" ");
    		Set<String> mentions = new HashSet<>();
    		
    		for (String word: words) {
    			if (word.startsWith("@")) {
    				String name = word.substring(1).replaceAll("[^A-Za-z0-9_-]", "").toLowerCase();
    				if (!author.equals(name)) {
    					mentions.add(name);
    				}
    			}
    		}
    		if (!mentions.isEmpty()) {
    			graph.putIfAbsent(author, new HashSet<>());
    			graph.get(author).addAll(mentions);    			
    		}
    		
    	}
    
    	return graph;
    
    }

    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
    	Map<String, Integer> numberOfFollowers = new HashMap<>();
    	
    	for (Set<String> person:followsGraph.values()) {
    		for (String name:person) {
    			numberOfFollowers.put(name, numberOfFollowers.getOrDefault(name, 0) + 1);
    		}
    	}
    	
    	List<String> influencers = new ArrayList<>(numberOfFollowers.keySet());
        influencers.sort((a, b) -> numberOfFollowers.get(b) - numberOfFollowers.get(a));

        return influencers;
    }

}
