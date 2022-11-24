package com.abc.fake;

public class ArrayStringList implements StringList {
    private String[] slots;
    private int count;

    public ArrayStringList() {
        slots = new String[10];
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public String getAtIndex(int index) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException(index);
        return slots[index];
    }

    private void growArrayIfNeeded(int numberOfSlotsNeeded) {
        if (count + numberOfSlotsNeeded >= slots.length) {
           String[] newSlots = new String[slots.length + numberOfSlotsNeeded + 5];
           System.arraycopy(slots, 0, newSlots, 0, count);
           slots = newSlots;
        }
    }

    @Override
    public void append(String s) {
        growArrayIfNeeded(1);
        slots[count] = s;
        count++;
    }
}
