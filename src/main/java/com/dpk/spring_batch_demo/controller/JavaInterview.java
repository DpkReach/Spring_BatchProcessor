package com.dpk.spring_batch_demo.controller;


import java.util.HashMap;
import java.util.Map;

/*
Coding question:
Finding the Longest Substring Without Repeating Characters
Description:
Given a string s, find the longest substring of s that contains no repeated characters. Return the substring itself. If there are multiple substrings of the same maximum length, return the one that appears first.
________________________________________
Constraints:
●	The input string s consists of ASCII characters.
●	The length of s can be up to 10^5 characters.
●	The substring must be contiguous.

Examples:
Example 1:
Input: s = "abcabcbb"
Output: "abc"

Explanation: The longest substring without repeating characters is "abc" which has length 3.
Note that "bca" and "cab" are also substrings without repeats but appear later.
Example 2:
Input: s = "bbbbb"
Output: "b"

Explanation: The longest substring without repeating characters is "b", which appears multiple times.
Example 3:
Input: s = "pwwkew"
Output: "wke"

Explanation: The longest substring without repeating characters is "wke" with length 3.
Note that "pwke" is not a substring because the characters are not contiguous.

Example 4:
Input: s = ""
Output: ""

Explanation: Empty input string returns an empty substring.
 */
public class JavaInterview {

    public static void main(String args[]) {

        String s[] ={"abcabcbbabc","bbbbb","pwwkew",""};

        for(int i=0;i<s.length;i++) {
           System.out.println(findSubstring(s[i]));
        }
    }

        public static String findSubstring(String s){
        int start = 0, maxLen = 0, maxStart = 0;
        Map<Character, Integer> outputmap = new HashMap<>();

        for (int end = 0; end < s.length(); end++) {
            char current = s.charAt(end);
            if (outputmap.containsKey(current) && (outputmap.get(current) >= start)) {
                start = outputmap.get(current) + 1;
            }
            outputmap.put(current, end);
            if (end - start + 1 > maxLen) {
                maxLen = end - start + 1;
                maxStart = start;
            }
        }
            return(s.substring(maxStart, maxStart + maxLen));
    }

}