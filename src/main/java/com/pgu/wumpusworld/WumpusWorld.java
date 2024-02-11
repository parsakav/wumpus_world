package com.pgu.wumpusworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WumpusWorld {

    public static final char EMPTY = ' ';
    public static final char GOLD = 'g';
    public static final char WUMPUS = 'w';
    public static final char PIT = 'p';

    public static final int[][] DIRECTIONS = {
        {0, 1}, // Right
        {1, 0}, // Down
        {0, -1}, // Left
        {-1, 0}  // Up
    };

    public static Position getNextPosition(char[][] world, Position agentPosition, boolean stench, boolean breeze) {
        List<Position> safeMoves = new ArrayList<>();

        // Check adjacent rooms for safety and gold
        for (int[] direction : DIRECTIONS) {
            int newRow = agentPosition.row + direction[0];
            int newCol = agentPosition.col + direction[1];

            if (isValidPosition(world, newRow, newCol) && world[newRow][newCol] != WUMPUS && world[newRow][newCol] != PIT) {
                safeMoves.add(new Position(newRow, newCol));

                if (world[newRow][newCol] == GOLD) {
                    // Return the gold position immediately
                    return new Position(newRow, newCol);
                }
            }
        }

        // Prioritize safe moves based on sensor readings
        if (safeMoves.isEmpty()) {
            // No safe moves found, take a risk based on sensors
            if (breeze) {
                // Avoid potential pits
                return chooseRandomMove(world, agentPosition, PIT);
            } else if (stench) {
                // Avoid potential wumpus
                return chooseRandomMove(world, agentPosition, WUMPUS);
            } else {
                // No specific sensor readings, choose a random move
                return chooseRandomMove(world, agentPosition);
            }
        } else {
            // Choose a random safe move
            return safeMoves.get(new Random().nextInt(safeMoves.size()));
        }
    }

    private static boolean isValidPosition(char[][] world, int row, int col) {
        return row >= 0 && row < world.length && col >= 0 && col < world[0].length;
    }

    private static Position chooseRandomMove(char[][] world, Position agentPosition, char avoidChar) {
        List<Position> possibleMoves = new ArrayList<>();
        for (int[] direction : DIRECTIONS) {
            int newRow = agentPosition.row + direction[0];
            int newCol = agentPosition.col + direction[1];

            if (isValidPosition(world, newRow, newCol) && world[newRow][newCol] != avoidChar) {
                possibleMoves.add(new Position(newRow, newCol));
            }
        }

        if (!possibleMoves.isEmpty()) {
            return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        } else {
            // No moves avoiding the specified character, try any move
            return chooseRandomMove(world, agentPosition);
        }
    }

    private static Position chooseRandomMove(char[][] world, Position agentPosition) {
        List<Position> possibleMoves = new ArrayList<>();
        for (int[] direction : DIRECTIONS) {
            int newRow = agentPosition.row + direction[0];
            int newCol = agentPosition.col + direction[1];

            if (isValidPosition(world, newRow, newCol)) {
                possibleMoves.add(new Position(newRow, newCol));
            }
        }

        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    static class Position {
        int row;
        int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}