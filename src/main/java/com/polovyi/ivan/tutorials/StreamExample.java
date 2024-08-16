package com.polovyi.ivan.tutorials;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        Stream<Object> emptyStream = Stream.empty();

        Stream<Integer> streamWithSingleElements = Stream.of(1);
        // Stream<Integer> streamWithNullElements = Stream.of(null); // -- exception
        Stream<Integer> streamWithMultipleElements = Stream.of(1, 2, null);

        IntStream intStreamWithSingleElements = IntStream.of(1);
        // IntStream intStreamWithNullElements = IntStream.of(null); // -- exception
        IntStream intStreamWithMultipleElements = IntStream.of(1, 2);
        // IntStream intStreamWithMultipleElementsAndNull = IntStream.of(1, 2, null); // -- wont compile

        LongStream longStreamWithSingleElements = LongStream.of(1L);
        // LongStream intStreamWithNullElements = LongStream.of(null); // -- exception
        LongStream longStreamWithMultipleElements = LongStream.of(1L, 2L);
        // LongStream longStreamWithMultipleElementsAndNull = LongStream.of(1L, 2L, null); // -- wont compile

        DoubleStream doubleStreamWithSingleElements = DoubleStream.of(1.0);
        // DoubleStream doubleStreamWithNullElements = DoubleStream.of(null); // -- exception
        DoubleStream doubleStreamWithMultipleElements = DoubleStream.of(1.0, 2.0);
        // DoubleStream doubleStreamWithMultipleElementsAndNull = DoubleStream.of(1.0, 2.0, null); // -- wont compile

        Stream<Integer> nonNullStream = Stream.ofNullable(1);
        Stream<Integer> nullableStream = Stream.ofNullable(null);

        Stream<Integer> builtStream = Stream.<Integer>builder()
                .add(1)
                .add(2)
                .add(null)
                .build();

        IntStream builtIntStream = IntStream.builder()
                .add(1)
                .add(2)
                //.add(null) -- wont compile
                .build();

        LongStream builtLongStream = LongStream.builder()
                .add(1L)
                .add(2L)
                //.add(null) -- wont compile
                .build();

        DoubleStream builtDoubleStream = DoubleStream.builder()
                .add(1.0)
                .add(2.0)
                //.add(null) -- wont compile
                .build();

        Stream<Integer> streamFromList = List.of(1, 2, 3).stream();
        Stream<Integer> parallelStreamFromList = List.of(1, 2, 3).parallelStream();

        System.out.println("concat method - ordered");
        List<Integer> orderedIntegers1 = List.of(1, 2);
        List<Integer> orderedIntegers2 = List.of(3, 4);
        Stream.concat(orderedIntegers1.stream(),
                orderedIntegers2.stream()).forEachOrdered(System.out::println);
        // 1, 2, 3, 4

        System.out.println("concat method - unordered1");
        Set<Integer> unorderedIntegers1 = Set.of(1, 2);
        Set<Integer> unorderedIntegers2 = Set.of(3, 4);
        Stream.concat(unorderedIntegers1.stream(),
                unorderedIntegers2.stream()).forEachOrdered(System.out::println);
        // 2, 1, 4, 3

        System.out.println("concat method - unordered2");
        Stream.concat(unorderedIntegers1.stream(),
                orderedIntegers2.stream()).forEachOrdered(System.out::println);
        // 2, 1, 3, 4

        System.out.println("concat method - parallel streams");
        boolean isParallelWhenBothParallel = Stream.concat(Stream.of(1).parallel(), Stream.of(2).parallel()).isParallel();
        System.out.println("isParallelWhenBothParallel = " + isParallelWhenBothParallel);
        boolean isParallelWhenOnlyOneParallel = Stream.concat(Stream.of(1).parallel(), Stream.of(2)).isParallel();
        System.out.println("isParallelWhenOnlyOneParallel = " + isParallelWhenOnlyOneParallel);
        boolean isParallelWhenBothSequential = Stream.concat(Stream.of(1), Stream.of(2)).isParallel();
        System.out.println("isParallelWhenBothSequential = " + isParallelWhenBothSequential);

        List<Integer> stream1 = List.of(1, 2, 3, 4, 5).stream()
                .skip(1)
                .limit(2)
                .toList();
        // [2, 3]
        System.out.println("stream1 = " + stream1);

        System.out.println("generate method");
        List<Integer> stream2 = Stream.generate(() -> ThreadLocalRandom.current()
                        .nextInt(0, 10 + 1))
                .limit(3L)
                .toList();
        // [3, 9, 4] (each run it is different)
        // Stream.generate(null); -- NPE
        System.out.println("stream2 = " + stream2);

        System.out.println("Iterate method");
        List<Integer> stream3 = Stream.iterate(0, i -> i + 1)
                .limit(5)
                .toList();
        // [0, 1, 2, 3, 4, 5]
        System.out.println("stream3 = " + stream3);

        List<Integer> stream4 = Stream.iterate(8, i -> i - 2)
                .limit(5)
                .toList();
        // [8, 6, 4, 2, 0]
        System.out.println("stream4 = " + stream4);

        List<Integer> stream5 = Stream.iterate(0, i -> i <= 5, i -> i + 1)
                .toList();
        // [0, 1, 2, 3, 4, 5]
        System.out.println("stream5 = " + stream5);

        System.out.println("Range method");
        List<Integer> stream6 = IntStream.range(0, 5)
                .boxed()
                .toList();
        // [0, 1, 2, 3, 4]
        System.out.println("stream6 = " + stream6);

        System.out.println("RangeClosed method");
        List<Integer> stream7 = IntStream.rangeClosed(0, 5)
                .boxed()
                .toList();
        // [0, 1, 2, 3, 4, 5]
        System.out.println("stream7 = " + stream7);

        System.out.println("Character stream");
        List<Character> stream8 = IntStream.rangeClosed(65, 68)
                .mapToObj(c -> (char) c)
                .toList();
        // [A, B, C, D]
        System.out.println("stream8 = " + stream8);

        System.out.println("String chars method");
        List<Character> stream9 = "ABCD".chars()
                .mapToObj(c -> (char) c)
                .toList();
        // [A, B, C, D]
        System.out.println("stream9 = " + stream9);

        System.out.println("String codePoints method");
        List<Character> stream10 = "ABC🙂".codePoints()
                .mapToObj(c -> (char) c)
                .toList();
        // [A, B, C, 🙂]
        System.out.println("stream10 = " + stream10);

        System.out.println("String lines method");
        String stream11 = "line1" + System.lineSeparator() + "line2";
        List<String> stringList = stream11.lines()
                .toList();
        // [line1, line2]
        System.out.println("stream11 = " + stringList);

        System.out.println("Arrays methods");

        List<String> stream12 = Arrays.stream(new String[]{"string1", "string2"})
                .toList();
        // string1, string2
        System.out.println("stream12 = " + stream12);

        List<Integer> stream13 = Arrays.stream(new int[]{0, 1, 2, 3})
                .boxed()
                .toList();
        // [0, 1, 2, 3]
        System.out.println("stream12 = " + stream13);

        List<String> stream14 = Arrays.stream(
                        new String[]{"string1", "string2", "string3", "string4"}, 1, 3)
                .toList();
        // string1, string2
        System.out.println("stream14 = " + stream14);

        generateStreamOfStringsFromFile();
        generatePathStream();
        streamFromBufferedReader();

        System.out.println("Random methods");
        List<Integer> stream15 = new Random().ints()
                .limit(3)
                .boxed().toList();
        // [20245242, -1186518608, -1252644247]
        System.out.println("stream15 = " + stream15);

        List<Integer> stream16 = new Random().ints(3)
                .boxed().toList();
        // [-972062906, -486366573, 877948768]
        System.out.println("stream16 = " + stream16);

        List<Integer> stream17 = new Random()
                .ints(1, 10)
                .limit(3)
                .boxed().toList();
        // [5, 7, 1]
        System.out.println("stream17 = " + stream17);

        List<Integer> stream18 = new Random()
                .ints(3, 1, 10)
                .boxed().toList();
        // [9, 7, 5]
        System.out.println("stream18 = " + stream18);

    }

    private static void streamFromBufferedReader() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("src/main/java/com/polovyi/ivan/tutorials/files/text-file.txt"));
            List<String> streamFromBufferedReader = br.lines()
                    .toList();
            // [line1, line2]
            System.out.println("streamFromBufferedReader = " + streamFromBufferedReader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void generateStreamOfStringsFromFile() {
        try {
            List<String> streamFromFile = Files.lines(
                            Path.of("src/main/java/com/polovyi/ivan/tutorials/files/text-file.txt"))
                    .toList();
            // [line1, line2]
            System.out.println("streamFromFile = " + streamFromFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generatePathStream() {
        try {
            List<Path> pathStream = Files.list(Path.of("src/main/java/com/polovyi/ivan/tutorials"))
                    .toList();
            // [src/main/java/com/polovyi/ivan/tutorials/files,
            // src/main/java/com/polovyi/ivan/tutorials/StreamExample.java]
            System.out.println("pathStream = " + pathStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
