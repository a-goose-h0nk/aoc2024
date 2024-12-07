package net.h0nk.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import net.h0nk.aoc.run.Run;

public class Day1 {

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partOne(final Path path) throws IOException {

        final var splitter = Pattern.compile("\\s+");

        final var left = new ArrayList<Integer>();
        final var right = new ArrayList<Integer>();

        try (final var lines = Files.lines(path)) {

            lines.filter(line -> !line.isBlank()).forEach(line -> {
                final var split = splitter.split(line);
                left.add(Integer.parseInt(split[0]));
                right.add(Integer.parseInt(split[1]));
            });
        }

        left.sort(Comparator.naturalOrder());
        right.sort(Comparator.naturalOrder());

        return IntStream.range(0, left.size())
                .map(i -> Math.abs(left.get(i) - right.get(i)))
                .sum();
    }

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partTwo(final Path path) throws IOException {

        final var splitter = Pattern.compile("\\s+");
        final var defaultInt = new AtomicInteger(0);

        final var left = new ArrayList<Integer>();
        final var rightCount = new HashMap<Integer, AtomicInteger>();

        try (final var lines = Files.lines(path)) {

            lines.filter(line -> !line.isBlank()).forEach(line -> {
                final var split = splitter.split(line);
                left.add(Integer.parseInt(split[0]));
                // keep a count of right hand side entries, we could also do this after parsing but 🤷‍♂️
                rightCount
                        .computeIfAbsent(Integer.parseInt(split[1]), __ -> new AtomicInteger())
                        .incrementAndGet();
            });
        }

        return left.stream()
                .mapToInt(val -> val * rightCount.getOrDefault(val, defaultInt).get())
                .sum();
    }
}
