package com.abc.fake;

import java.util.*;

public class ListStringList implements StringList {
    private final List<String> list;

    public ListStringList() {
        list = new ArrayList<>();
    }

    @Override
    public int count() {
        return list.size();
    }

    @Override
    public String getAtIndex(int index) {
        return list.get(index);
    }

    @Override
    public void append(String s) {
        list.add(s);
    }
}
