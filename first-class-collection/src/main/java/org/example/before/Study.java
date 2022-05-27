package org.example.before;

import org.example.Member;

import java.util.List;
import java.util.stream.Collectors;

public class Study {

    private String name;
    private boolean isFree;
    private List<Member> members;

    public Study(String name, boolean isFree, List<Member> members) {
        validate(members);
        this.name = name;
        this.isFree = isFree;
        this.members = members;
    }

    private void validate(List<Member> members) {
        if (members.size() > 10) {
            throw new IllegalArgumentException("스터디원은 최대 10명입니다.");
        }
    }

    public List<Member> findAdults() {
        return members.stream()
                .filter(m -> m.getAge() >= 20)
                .collect(Collectors.toList());
    }
}
