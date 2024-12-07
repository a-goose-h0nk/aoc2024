package net.h0nk.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import net.h0nk.aoc.run.Run;

public class Day3 {

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partOne(final Path path) throws IOException {

        final var pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");

        try (final var lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank())
                    .flatMap(line -> pattern.matcher(line).results())
                    .mapToInt(res -> Integer.parseInt(res.group(1)) * Integer.parseInt(res.group(2)))
                    .sum();
        }
    }

    @Run(paths = {"day:puzzle2", "day:puzzle1"})
    static int partTwo(final Path path) throws IOException {

        final var pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)|(do)(n't)?\\(\\)");
        final var allowed = new AtomicBoolean(true);

        try (final var lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank())
                    .flatMap(line -> pattern.matcher(line).results())
                    .filter(result -> {
                        if (result.group(4) != null) {
                            allowed.set(false);
                            return false;
                        } else if (result.group(3) != null) {
                            allowed.set(true);
                            return false;
                        }
                        return allowed.get();
                    })
                    .mapToInt(result -> Integer.parseInt(result.group(1)) * Integer.parseInt(result.group(2)))
                    .sum();
        }
    }
}
