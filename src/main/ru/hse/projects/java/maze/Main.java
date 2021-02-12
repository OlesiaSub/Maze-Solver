package main.ru.hse.projects.java.maze;

public class Main {
    public static void main(String[] args) {
        MazeSolver maze = new MazeSolver();
        maze.readInput();
        maze.constructGraph();
        maze.getPath();
    }
}