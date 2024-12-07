package net.h0nk.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.LongBinaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import net.h0nk.aoc.run.Run;

public class Day7 {

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static long partOne(final Path path) throws IOException {

        final var splitter = Pattern.compile(":?\\s+");

        final var symbols = new LongBinaryOperator[] {(left, right) -> left * right, Long::sum};

        try (final var lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank())
                    .map(line -> Arrays.stream(splitter.split(line))
                            .map(Long::parseLong)
                            .toList())
                    .mapToLong(list -> IntStream.range(0, (int) Math.pow(symbols.length, list.size() - 2))
                            .mapToLong(i -> {
                                int temp = i;
                                long total = list.get(1);
                                for (int j = 1; j < list.size() - 1; j++) {
                                    total = symbols[temp % symbols.length].applyAsLong(total, list.get(j + 1));
                                    temp /= symbols.length;
                                    if(total > list.getFirst()) {
                                        return 0;
                                    }
                                }
                                return list.getFirst() == total ? list.getFirst() : 0;
                            })
                            .filter(result -> result != 0)
                            .findFirst()
                            .orElse(0))
                    .sum();
        }
    }

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static long partTwo(final Path path) throws IOException {

        final var splitter = Pattern.compile(":?\\s+");

        final var symbols = new LongBinaryOperator[] {
            (left, right) -> left * right, (left, right) -> Long.parseLong(left + Long.toString(right)), Long::sum
        };

        try (final var lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank())
                    .map(line -> Arrays.stream(splitter.split(line))
                            .map(Long::parseLong)
                            .toList())
                    .mapToLong(list -> IntStream.range(0, (int) Math.pow(symbols.length, list.size() - 2))
                            .mapToLong(i -> {
                                int temp = i;
                                long total = list.get(1);
                                for (int j = 1; j < list.size() - 1; j++) {
                                    total = symbols[temp % symbols.length].applyAsLong(total, list.get(j + 1));
                                    temp /= symbols.length;
                                    if (total > list.getFirst()) {
                                        return 0;
                                    }
                                }
                                return list.getFirst() == total ? list.getFirst() : 0;
                            })
                            .filter(result -> result != 0)
                            .findFirst()
                            .orElse(0))
                    .sum();
        }
    }
}
