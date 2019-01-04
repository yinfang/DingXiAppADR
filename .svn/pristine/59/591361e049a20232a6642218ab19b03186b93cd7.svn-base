package com.bigkoo.pickerview.adapter;


/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;
    private boolean isBlock = false;//是否是按时间段请假

    /**
     * Default constructor
     */
    public NumericWheelAdapter() {
        this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, false);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public NumericWheelAdapter(int minValue, int maxValue, boolean isBlock) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.isBlock = isBlock;
    }

    @Override
    public Object getItem(int index) {
        if (isBlock) {//按时间段请假
            if (index == 0) {
                return 0;
            } else {
                return 30;
            }
        } else {
            if (index >= 0 && index < getItemsCount()) {
                int value = minValue + index;
                return value;
            }
        }
        return 0;
    }

    @Override
    public int getItemsCount() {
        if (isBlock) {//按时间段请假
            return 2;
        } else {
            return maxValue - minValue + 1;
        }
    }

    @Override
    public int indexOf(Object o) {
        try {
            return (int) o - minValue;
        } catch (Exception e) {
            return -1;
        }

    }
}
