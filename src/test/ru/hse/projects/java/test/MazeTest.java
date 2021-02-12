package test.ru.hse.projects.java.test;

import main.ru.hse.projects.java.maze.MazeSolver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;

public class MazeTest {

    public MazeSolver maze;

    @BeforeEach
    public void createMaze() {
        maze = new MazeSolver();
    }

    private void constructMatrix() {
        maze.initializeFields(3, 3);
        maze.matrix = new int[][]{{1, 0, 1}, {1, 0, 1}, {1, 0, 0}};
        maze.startPos = new MazeSolver.IndexPair(0, 1);
    }

    @Test
    public void testReadInput() {
        ByteArrayInputStream in = new ByteArrayInputStream("3 4 1 0 1 1 0 1 0 0 1 0 0 1 2 1".getBytes());
        System.setIn(in);
        maze = new MazeSolver();
        maze.readInput();
        assertEquals(maze.matrix.length, 3);
        assertEquals(maze.matrix[0].length, 4);
        assertEquals(maze.startPos.fst, 2);
        assertEquals(maze.startPos.snd, 1);
    }

    @Test
    public void testReadInputZeroInput() {
        ByteArrayInputStream in = new ByteArrayInputStream("0 0 0 0".getBytes());
        System.setIn(in);
        maze = new MazeSolver();
        assertThrows(IllegalArgumentException.class, () -> maze.readInput());
    }

    @Test
    public void testReadInputWrongMatrixContent() {
        ByteArrayInputStream in = new ByteArrayInputStream("2 2 1 2 3 4 0 1".getBytes());
        System.setIn(in);
        maze = new MazeSolver();
        assertThrows(IllegalArgumentException.class, () -> maze.readInput());
    }

    @Test
    public void testReadInputStartingPointOutOfBounds() {
        ByteArrayInputStream in = new ByteArrayInputStream("2 2 0 0 1 1 3 2".getBytes());
        System.setIn(in);
        maze = new MazeSolver();
        assertThrows(IllegalArgumentException.class, () -> maze.readInput());
    }

    @Test
    public void testReadInputStartingPointEqualsOne() {
        ByteArrayInputStream in = new ByteArrayInputStream("2 2 1 1 1 1 0 1".getBytes());
        System.setIn(in);
        maze = new MazeSolver();
        assertThrows(IllegalArgumentException.class, () -> maze.readInput());
    }

    @Test
    public void testTraverseGraph() {
        constructMatrix();
        maze.traverseMatrix();

        assertEquals(maze.verticesStorageCoordsArray[0][1], 0);
        assertEquals(maze.verticesStorageCoordsArray[1][1], 1);
        assertEquals(maze.verticesStorageCoordsArray[2][1], 2);
        assertEquals(maze.verticesStorageCoordsArray[2][2], 3);

        assertEquals(maze.verticesStorageIdx.get(1).index, 1);
        assertEquals(maze.verticesStorageIdx.get(1).fstPos, 1);
        assertEquals(maze.verticesStorageIdx.get(1).sndPos, 1);
        assertFalse(maze.verticesStorageIdx.get(1).terminal);
    }

    @Test
    public void testConstructGraph() {
        constructMatrix();
        maze.constructGraph();

        assertTrue(maze.edges.get(0).contains(1));
        assertTrue(maze.edges.get(1).contains(0));
        assertTrue(maze.edges.get(1).contains(2));
        assertTrue(maze.edges.get(2).contains(1));
        assertTrue(maze.edges.get(2).contains(3));
        assertTrue(maze.edges.get(3).contains(2));

        assertFalse(maze.edges.get(0).contains(2));
        assertFalse(maze.edges.get(1).contains(3));
        assertFalse(maze.edges.get(2).contains(0));
        assertFalse(maze.edges.get(2).contains(2));
    }

    @Test
    public void testWriteOutputWrongIndicesDiff() {
        constructMatrix();
        maze.path.add(new MazeSolver.IndexPair(1, 0));
        maze.path.add(new MazeSolver.IndexPair(1, 2));
        assertThrows(RuntimeException.class, () -> maze.writeOutput());
    }

    @Test
    public void testWriteOutputEqualIndices() {
        constructMatrix();
        maze.path.add(new MazeSolver.IndexPair(1, 0));
        maze.path.add(new MazeSolver.IndexPair(1, 0));
        assertThrows(RuntimeException.class, () -> maze.writeOutput());
    }

    @Test
    public void testGetPath() {
        constructMatrix();
        maze.constructGraph();
        maze.getPath();

        assertTrue(maze.terminals.contains(2));
        assertFalse(maze.terminals.contains(0));
        assertFalse(maze.terminals.contains(1));
        assertFalse(maze.terminals.contains(3));

        assertEquals(maze.path.get(0).fst, 0);
        assertEquals(maze.path.get(0).snd, 1);
        assertEquals(maze.path.get(1).fst, 1);
        assertEquals(maze.path.get(1).snd, 1);
        assertEquals(maze.path.get(2).fst, 2);
        assertEquals(maze.path.get(2).snd, 1);
    }
}