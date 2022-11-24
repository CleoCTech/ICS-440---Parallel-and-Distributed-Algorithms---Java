package com.abc.fake;

public class StringListDemo {
    public static void main(String[] args) {

        StringList sl = new ArrayStringList();
        sl.append("apple");
        sl.append("banana");

        StringList slB = new SyncStringList(new ListStringList());
        slB.count(); // multi-thread safe
        // ...
    }
}
