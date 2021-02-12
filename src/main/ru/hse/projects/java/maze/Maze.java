package main.ru.hse.projects.java.maze;

public interface Maze {

    /**
     * Reads input from console: matrix dimensions, matrix, starting point coordinates
     *
     * @throws IllegalArgumentException if matrix is of zero size or if matrix content is wrong or if starting point is not zero
     */
    void readInput() throws IllegalArgumentException;

    /**
     * Constructs graph from the given matrix
     */
    void constructGraph();

    /**
     * Restores path which was found during DFS traversal
     * Can be easily modified to return several paths found during DFS traversal
     */
    void getPath();

    /**
     * Prints path which was found after DFS graph traversal
     *
     * @throws IllegalArgumentException if the indexes of path units are incorrect
     */
    void writeOutput();

    /**
     * DFS traversal
     *
     * @param vertex is the starting point of traversal
     */
    void dfs(int vertex);
}