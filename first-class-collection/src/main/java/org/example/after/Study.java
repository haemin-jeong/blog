package org.example.after;

import org.example.Member;

import java.util.List;

public class Study {

    private String name;
    private boolean isFree;
    private Members members;

    public Study(String name, boolean isFree, Members members) {
        this.name = name;
        this.isFree = isFree;
        this.members = members;
    }

    public List<Member> findAdults() {
        return members.findAdults();
    }
}


