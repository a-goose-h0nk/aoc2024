package net.h0nk.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import net.h0nk.aoc.run.Run;

public class Day2 {

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partOne(final Path path) throws IOException {

        final var splitter = Pattern.compile("\\s+");

        try (final var lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank())
                    .map(line -> Arrays.stream(splitter.split(line))
                            .map(Integer::parseInt)
                            .toList())
                    .mapToInt(list -> safe(list) ? 1 : 0)
                    .sum();
        }
    }

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partTwo(final Path path) throws IOException {

        final var splitter = Pattern.compile("\\s+");

        final var temp = new ArrayList<Integer>();
        try (final var lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank())
                    .map(line -> Arrays.stream(splitter.split(line))
                            .map(Integer::parseInt)
                            .toList())
                    .mapToInt(list -> {
                        if (safe(list)) {
                            return 1;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            temp.clear();
                            temp.addAll(list);
                            temp.remove(i);

                            if (safe(temp)) {
                                return 1;
                            }
                        }
                        return 0;
                    })
                    .sum();
        }
    }

    static boolean safe(final List<Integer> list) {

        int direction = 0;
        for (int i = 0; i < list.size() - 1; i++) {

            final int diff = list.get(i) - list.get(i + 1);

            if (diff == 0 || diff < -3 || diff > 3) {
                return false;
            } else if (direction == 0) {
                direction = Math.clamp(diff, -1, 1);
            } else if (direction != Math.clamp(diff, -1, 1)) {
                return false;
            }
        }
        return true;
    }
}
