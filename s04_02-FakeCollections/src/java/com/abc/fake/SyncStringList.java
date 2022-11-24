package com.abc.fake;

// Decorator .... SyncStringList IS-A StringList and HAS-A StringList
public class SyncStringList implements StringList {
    private final StringList rawList;

    public SyncStringList(StringList rawList) {
        this.rawList = rawList;
    }

    @Override
    public synchronized int count() {
        return rawList.count();
    }

    @Override
    public synchronized String getAtIndex(int index) {
        return rawList.getAtIndex(index);
    }

    @Override
    public synchronized void append(String s) {
        rawList.append(s);
    }
}
