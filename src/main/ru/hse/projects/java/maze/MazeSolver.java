package main.ru.hse.projects.java.maze;

import java.util.*;

public class MazeSolver implements Maze {

    public static class Vertex {
        public int index;
        public int fstPos;
        public int sndPos;
        public boolean terminal;

        public Vertex(int idx, int i, int j, boolean term) {
            index = idx;
            fstPos = i;
            sndPos = j;
            terminal = term;
        }
    }

    public static class IndexPair {
        public int fst;
        public int snd;

        public IndexPair(int x, int y) {
            fst = x;
            snd = y;
        }
    }

    public int[][] matrix;
    public int[] parents;
    public boolean[] used;
    public final ArrayList<Integer> terminals = new ArrayList<>();
    public final ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
    public ArrayList<IndexPair> path = new ArrayList<>();
    public final Map<Integer, Vertex> verticesStorageIdx = new HashMap<>();
    public int[][] verticesStorageCoordsArray;
    public IndexPair startPos;
    public Integer startIdx;
    public Integer curIndex = 0;

    @Override
    public void readInput() throws IllegalArgumentException {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int m = scan.nextInt();

        if (n == 0 && m == 0) {
            throw new IllegalArgumentException("Matrix does not contain any elements.");
        }

        initializeFields(n, m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scan.nextInt();
                if (matrix[i][j] != 1 && matrix[i][j] != 0) {
                    throw new IllegalArgumentException("Incorrect maze format.");
                }
            }
        }
        startPos = new IndexPair(scan.nextInt(), scan.nextInt());

        if (startPos.fst >= n || startPos.snd >= m || matrix[startPos.fst][startPos.snd] == 1) {
            throw new IllegalArgumentException("Impossible starting position");
        }
        scan.close();
    }

    public void initializeFields(int n, int m) {
        matrix = new int[n][m];
        used = new boolean[n * m];
        parents = new int[n * m];
        verticesStorageCoordsArray = new int[n][m];
        for (int i = 0; i < n * m; i++) {
            edges.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                verticesStorageCoordsArray[i][j] = -1;
            }
        }
    }

    public void traverseMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    boolean terminal = i == 0 || i == matrix.length - 1 || j == matrix[0].length - 1 || j == 0;
                    Vertex current = new Vertex(curIndex, i, j, terminal);
                    verticesStorageCoordsArray[i][j] = curIndex;
                    verticesStorageIdx.put(curIndex, current);
                    curIndex++;
                }
            }
        }
    }

    private void addEdges(IndexPair idx, int i) {
        if (idx.fst >= 0 && idx.snd >= 0 && idx.fst < verticesStorageCoordsArray.length
                && idx.snd < verticesStorageCoordsArray[0].length && verticesStorageCoordsArray[idx.fst][idx.snd] != -1) {
            Vertex curVertex = verticesStorageIdx.get(verticesStorageCoordsArray[idx.fst][idx.snd]);
            edges.get(i).add(curVertex.index);
        }
    }

    @Override
    public void constructGraph() {
        traverseMatrix();
        for (int i = 0; i < curIndex; i++) {
            IndexPair current = new IndexPair(verticesStorageIdx.get(i).fstPos, verticesStorageIdx.get(i).sndPos);
            if (current.fst == startPos.fst && current.snd == startPos.snd) {
                startIdx = i;
            }
            IndexPair left = new IndexPair(current.fst, current.snd - 1);
            IndexPair right = new IndexPair(current.fst, current.snd + 1);
            IndexPair up = new IndexPair(current.fst - 1, current.snd);
            IndexPair down = new IndexPair(current.fst + 1, current.snd);
            addEdges(left, i);
            addEdges(right, i);
            addEdges(up, i);
            addEdges(down, i);
        }
    }

    @Override
    public void getPath() {
        dfs(startIdx);
        if (terminals.isEmpty()) {
            System.out.println("No path was found.");
            return;
        }
        int terminal = terminals.get(0);
        for (int i = terminal; i != startIdx; i = parents[i]) {
            path.add(new IndexPair(verticesStorageIdx.get(i).fstPos, verticesStorageIdx.get(i).sndPos));
        }
        path.add(new IndexPair(verticesStorageIdx.get(startIdx).fstPos, verticesStorageIdx.get(startIdx).sndPos));
        Collections.reverse(path);
        writeOutput();
    }

    private boolean getDirection(int i, ArrayList<IndexPair> path) {
        if (path.get(i).fst == path.get(i + 1).fst && path.get(i).snd == path.get(i + 1).snd - 1) {
            System.out.println("Go to the right");
        } else if (path.get(i).fst == path.get(i + 1).fst && path.get(i).snd == path.get(i + 1).snd + 1) {
            System.out.println("Go to the left");
        } else if (path.get(i).fst == path.get(i + 1).fst - 1 && path.get(i).snd == path.get(i + 1).snd) {
            System.out.println("Go down");
        } else if (path.get(i).fst == path.get(i + 1).fst + 1 && path.get(i).snd == path.get(i + 1).snd) {
            System.out.println("Go up");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void writeOutput() throws IllegalArgumentException {
        System.out.print("Your starting point is {");
        System.out.print(startPos.fst);
        System.out.print("; ");
        System.out.print(startPos.snd);
        System.out.println("}");

        for (int i = 0; i < path.size() - 1; i++) {
            boolean correct = getDirection(i, path);
            if (!correct) {
                throw new RuntimeException("Impossible path was found.");
            }
        }
        System.out.println("Congratulations! You found the way out!");
    }

    @Override
    public void dfs(int vertex) {
        if (vertex != startIdx && verticesStorageIdx.get(vertex).terminal) {
            terminals.add(vertex);
            return;
        }
        used[vertex] = true;
        for (int i : edges.get(vertex)) {
            if (!used[i]) {
                dfs(i);
                parents[i] = vertex;
            }
        }
    }
}