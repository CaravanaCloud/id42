package id42.chat;

import id42.Identity;

public class TestIdentity implements Identity {
    public static Identity anonymous() {
        return new TestIdentity();
    }
}
