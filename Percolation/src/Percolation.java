import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf2;
    private boolean open[][];
    private int n;
    private int openCnt;
    private int TOP;
    private int DOWN;

    public Percolation(int n) {               // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException("n is false");
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        open = new boolean[n][n];
        this.n = n;
        openCnt = 0;
        TOP = n * n;
        DOWN = n * n + 1;
    }

    public void open(int row, int col) {   // open site (row, col) if it is not open already
        validateRowCol(row, col);
        row--;
        col--;
        if (open[row][col] == false) {
            open[row][col] = true;
            connect(row, col);
            connect2(row, col);
            openCnt++;
        }
    }

    private void connect(int row, int col) {
        if (row != 0) {
            if (open[row - 1][col]) {
                uf.union((row - 1) * n + col, row * n + col);
            }
        } else {
            uf.union(TOP, row * n + col);
        }
        if (col != 0) {
            if (open[row][col - 1])
                uf.union(row * n + (col - 1), row * n + col);
        }
        if (row != n - 1) {
            if (open[row + 1][col])
                uf.union((row + 1) * n + col, row * n + col);
        } else {
            uf.union(DOWN, row * n + col);
        }
        if (col != n - 1) {
            if (open[row][col + 1])
                uf.union(row * n + (col + 1), row * n + col);
        }
    }

    private void connect2(int row, int col) {
        if (row != 0) {
            if (open[row - 1][col]) {
                uf2.union((row - 1) * n + col, row * n + col);
            }
        } else {
            uf2.union(TOP, row * n + col);
        }
        if (col != 0) {
            if (open[row][col - 1])
                uf2.union(row * n + (col - 1), row * n + col);
        }
        if (row != n - 1) {
            if (open[row + 1][col])
                uf2.union((row + 1) * n + col, row * n + col);
        }
        if (col != n - 1) {
            if (open[row][col + 1])
                uf2.union(row * n + (col + 1), row * n + col);
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        validateRowCol(row, col);
        row--;
        col--;
        return open[row][col];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        validateRowCol(row, col);
        row--;
        col--;
        return uf2.connected(TOP, row * n + col);
    }

    public int numberOfOpenSites() {      // number of open sites
        return openCnt;
    }

    public boolean percolates() {             // does the system percolate?
        return uf.connected(TOP, DOWN);
    }

    private void validateRowCol(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("row or col false");
    }

    public static void main(String[] args) {  // test client (optional)

    }
}
