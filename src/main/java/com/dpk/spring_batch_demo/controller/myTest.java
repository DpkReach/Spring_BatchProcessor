package com.dpk.spring_batch_demo.controller;


/*

Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.

Note that you must do this in-place without making a copy of the array.



Example 1:

Input: nums = [0,1,0,3,12]
Output: [1,3,12,0,0]
Example 2:

Input: nums = [0]
Output: [0]

 */
public class myTest {
    public static void main(String args[]) {
        int[] input = {0,1,3,0,0,2};
        int[] output = moveZeros(input);
        for(int n:output){
            System.out.print(n);
            /*if(n!=0){
                System.out.print(" ");
            }*/
        }
    }
    public static int[] moveZeros(int[] nums) {
        int insertPos = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[i] != 0){
                nums[insertPos++] = nums[i];
            }
        }
        while(insertPos < nums.length){
            nums[insertPos++] = 0;
        }
            return nums;
    }



}
