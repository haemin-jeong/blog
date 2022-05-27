package org.example.after;

import org.example.Member;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 일급 컬렉션
 */
public class Members {
    private List<Member> members;

    public Members(List<Member> members) {
        validate(members);
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

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }
}