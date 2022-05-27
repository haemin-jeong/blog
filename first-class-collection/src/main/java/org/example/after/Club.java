package org.example.after;

import org.example.Member;

import java.util.List;

public class Club {
    private String name;
    private boolean isFree;
    private Members members;

    public Club(String name, boolean isFree, Members members) {
        this.name = name;
        this.isFree = isFree;
        this.members = members;
    }

    public List<Member> findAdults() {
        return members.findAdults();
    }
}
