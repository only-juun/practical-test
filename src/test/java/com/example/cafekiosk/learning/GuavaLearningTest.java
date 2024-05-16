package com.example.cafekiosk.learning;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GuavaLearningTest {

    @Test
    void 주어진_개수만큼_List를_파티셔닝한다_1() {
        // given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // when
        List<List<Integer>> partition = Lists.partition(integers, 3);

        // then
        assertThat(partition).hasSize(4)
                .isEqualTo(List.of(
                        List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9), List.of(10)
                ));
    }

    @Test
    void 주어진_개수만큼_List를_파티셔닝한다_2() {
        // given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // when
        List<List<Integer>> partition = Lists.partition(integers, 5);

        // then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1, 2, 3, 4, 5), List.of(6, 7, 8, 9, 10)
                ));
    }

    @Test
    void 멀티맵의_기능을_확인한다_1() {
        // given
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "카푸치노");
        multimap.put("베이커리", "크루아상");
        multimap.put("베이커리", "식빵");

        // when
        Collection<String> strings = multimap.get("커피");

        // then
        assertThat(strings).hasSize(3)
                .isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
    }

    @TestFactory
    Collection<DynamicTest> 멀티맵의_기능을_확인한다_2() {
        // given
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "카푸치노");
        multimap.put("베이커리", "크루아상");
        multimap.put("베이커리", "식빵");

        return List.of(
            DynamicTest.dynamicTest("1개 value 삭제", () -> {
                // when
                multimap.remove("커피", "카푸치노");

                // then
                Collection<String> results = multimap.get("커피");
                assertThat(results).hasSize(2)
                        .isEqualTo(List.of("아메리카노", "카페라떼"));
            }),
            DynamicTest.dynamicTest("1개 key 삭제", () -> {
                // when
                multimap.removeAll("커피");

                // then
                Collection<String> results = multimap.get("커피");
                assertThat(results).isEmpty();
            })
        );
    }
}
