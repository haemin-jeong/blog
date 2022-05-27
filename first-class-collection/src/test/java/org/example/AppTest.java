package org.example;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AppTest {

    @Test
    @DisplayName("final 키워드로는 컬렉션을 불변으로 만들 수 없다.")
    public void finalTest() {
        final List<String> list = new ArrayList<>();

        list.add("Hello");
        list.remove("World");
    }
}
