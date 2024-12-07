package net.h0nk.aoc.run;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class Runner {

    public static void main(String[] args) throws Exception {

        log("---");
        log("Advent of Code 2024");

        if (args.length == 1) {
            try {
                runDay(Integer.parseInt(args[0]));
            } catch (final ClassNotFoundException ignored) {
            }
        } else {
            for (int day = 1; day < 26; day++) {
                try {
                    runDay(day);
                } catch (final ClassNotFoundException ignored) {
                }
            }
        }

        log("---");
    }

    public static void runDay(final int day) throws Exception {

        final var methods = Arrays.stream(
                        Class.forName("net.h0nk.aoc.Day" + day).getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.isAnnotationPresent(Run.class))
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> Path.class.isAssignableFrom(m.getParameters()[0].getType()))
                .filter(m -> !void.class.isAssignableFrom(m.getReturnType()))
                .sorted(Comparator.comparing(Method::getName))
                .toList();

        if (methods.isEmpty()) {
            return;
        }

        log("---");
        log("Day " + day);
        log("---");

        FileSystem jar = null;

        for (final var method : methods) {
            final var run = method.getAnnotation(Run.class);
            for (final var pathStr : run.paths()) {

                final Path path;
                if (pathStr.startsWith("day:")) {
                    final var resource = "/day" + day + "/" + pathStr.substring("day:".length());
                    final var uri = Objects.requireNonNull(Runner.class.getResource(resource))
                            .toURI();
                    if (uri.getScheme().equals("jar")) {
                        if (jar == null) {
                            jar = FileSystems.newFileSystem(uri, Collections.emptyMap());
                        }
                        path = jar.getPath(resource);
                    } else {
                        path = Paths.get(uri);
                    }
                } else {
                    path = Paths.get(pathStr);
                }

                method.setAccessible(true);

                long taken = System.nanoTime();
                final var result = method.invoke(null, path);
                taken = System.nanoTime() - taken;

                log(
                        "[%s]: Path [%s] Answer [%s] Millis [%d]",
                        method.getName(),
                        path.getFileName(),
                        result,
                        Duration.ofNanos(taken).toMillis());
            }
        }

        if (jar != null) {
            jar.close();
        }
    }

    public static void log(final String str) {
        System.out.println(str);
    }

    public static void log(final String pattern, final Object... args) {
        System.out.printf(pattern + System.lineSeparator(), args);
    }
}
