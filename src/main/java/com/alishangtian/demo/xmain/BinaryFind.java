package com.alishangtian.demo.xmain;

import lombok.extern.log4j.Log4j2;

/**
 * @Description BinaryFind
 * @Date 2020/5/13 上午9:39
 * @Author maoxiaobing
 **/
@Log4j2
public class BinaryFind {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 5, 7, 9, 10, 13, 13, 13, 15, 16, 17};
        log.info("target index is: " + binaryLastSearch(nums, 13));
    }

    /**
     * @Author maoxiaobing
     * @Description binarySearch
     * @Date 2020/5/13
     * @Param [nums, target]
     * @Return int
     */
    static int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int middle = left + ((right - left) >> 1);
            if (nums[middle] > target) {
                right = middle - 1;
            } else if (nums[middle] < target) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        return 0;
    }

    /**
     * @Author maoxiaobing
     * @Description binaryFirstSearch
     * @Date 2020/5/13
     * @Param [nums, target]
     * @Return int
     */
    static int binaryFirstSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int middle = left + ((right - left) >> 1);
            if (nums[middle] >= target) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return nums[right + 1] == target ? right + 1 : -1;
    }

    /**
     * @Author maoxiaobing
     * @Description binaryLastSearch
     * @Date 2020/5/13
     * @Param [nums, target]
     * @Return int
     */
    static int binaryLastSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int middle = left + ((right - left) >> 1);
            if (nums[middle] > target) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return nums[right + 1] > target ? right + 1 : -1;
    }
}
