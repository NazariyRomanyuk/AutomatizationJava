import org.example.BST;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BSTTest {
    private BST<Integer,String> bst;

    @BeforeEach
    void buildTree() {
        bst = new BST<>();
        bst.put(1, "one");
        bst.put(5, "five");
        bst.put(4, "four");
        bst.put(2, "two");
        bst.put(7, "seven");
    }

    @Test
    @Tag("get")
    @DisplayName("Should correctly get the expected value for a given key")
    void getReturnsExpectedValues() {
        assertEquals("four", bst.get(4), "Key 4 should return value \"four\".");
    }

    @ParameterizedTest
    @Tag("get")
    @ValueSource(ints = {1,7,2,4,5})
    @DisplayName("Should get non-null values for all inserted keys")
    void insertedKeysAreNotNull(int key) {
        assertNotNull(bst.get(key), "Key " + key + "should be in BST");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 0",
            "2, 1",
            "4, 2",
            "5, 3",
            "6, 4",
            "7, 4"
    })
    @Tag("advanced")
    @DisplayName("Should return expected rank for each given key")
    void rankReturnsExpectedValues(int key, int expectedRank) {
        assertEquals(expectedRank, bst.rank(key), "Key " + key + " should have rank " + expectedRank);
    }

    @Tag("advanced")
    @TestFactory
    Stream<DynamicTest> floorFromFile() throws IOException {
        Path path = Paths.get("floor.txt");
        assumeTrue(Files.exists(path), "Test data file must be present.");

        return Files.readAllLines(path).stream().map(line -> {
            String[] pair = line.split(",");
            int key = Integer.parseInt(pair[0].strip());
            int expectedFloor = Integer.parseInt(pair[1].strip());
            int actualFloor = bst.floor(key);
            return DynamicTest.dynamicTest("Should return expected floor for key " + key,
                    () -> assertEquals(actualFloor, expectedFloor, "floor(" + key + ") should return " + expectedFloor + ", returned " + actualFloor + " instead."));
        });

    }




}
