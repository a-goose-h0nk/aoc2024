package net.h0nk.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import net.h0nk.aoc.run.Run;

public class Day4 {

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partOne(final Path path) throws IOException {

        final var directions = Cardinal.values();
        final var suffix = "MAS".toCharArray();

        final char[][] grid;
        try (final var lines = Files.lines(path)) {
            grid = lines.filter(line -> !line.isBlank())
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
        }

        int sum = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] != 'X') {
                    continue;
                }

                for (final var direction : directions) {
                    if (matchInDirection(grid, x, y, direction, suffix)) {
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    static boolean matchInDirection(
            final char[][] grid, final int x, final int y, final Cardinal direction, final char[] word) {
        int deltaX = x, deltaY = y;
        for (final var c : word) {

            deltaX = direction.moveX(deltaX);
            deltaY = direction.moveY(deltaY);

            if (notMatching(grid, deltaX, deltaY, c)) {
                return false;
            }
        }
        return true;
    }

    static boolean notMatching(final char[][] grid, final int x, final int y, final char check) {
        return x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || grid[x][y] != check;
    }

    @Run(paths = {"day:puzzle0", "day:puzzle1"})
    static int partTwo(final Path path) throws IOException {

        // list of states (M list, S List)
        var xMasStates = List.of(
                List.of(List.of(Cardinal.NW, Cardinal.NE), List.of(Cardinal.SE, Cardinal.SW)),
                List.of(List.of(Cardinal.NE, Cardinal.SE), List.of(Cardinal.SW, Cardinal.NW)),
                List.of(List.of(Cardinal.NW, Cardinal.SW), List.of(Cardinal.SE, Cardinal.NE)),
                List.of(List.of(Cardinal.SW, Cardinal.SE), List.of(Cardinal.NE, Cardinal.NW)));

        final char[][] grid;
        try (final var lines = Files.lines(path)) {
            grid = lines.filter(line -> !line.isBlank())
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
        }

        int sum = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] != 'A') {
                    continue;
                }
                int deltaX, deltaY;
                states:
                for (final var state : xMasStates) {
                    for (final var mState : state.getFirst()) {
                        deltaX = mState.moveX(x);
                        deltaY = mState.moveY(y);
                        if (notMatching(grid, deltaX, deltaY, 'M')) {
                            continue states;
                        }
                    }
                    for (final var sState : state.getLast()) {
                        deltaX = sState.moveX(x);
                        deltaY = sState.moveY(y);
                        if (notMatching(grid, deltaX, deltaY, 'S')) {
                            continue states;
                        }
                    }
                    sum++;
                }
            }
        }
        return sum;
    }

    enum Cardinal {
        N(-1, -0),
        S(1, 0),
        W(0, -1),
        E(0, 1),
        NW(-1, -1),
        NE(-1, 1),
        SW(1, -1),
        SE(1, 1);
        private final int deltaX;
        private final int deltaY;

        int moveX(final int x) {
            return x + deltaX;
        }

        int moveY(final int y) {
            return y + deltaY;
        }

        Cardinal(final int x, final int y) {
            this.deltaX = x;
            this.deltaY = y;
        }
    }
}
