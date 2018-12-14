import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] x;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <=0 || trials <= 0)
            throw new IllegalArgumentException("erro");
        x = new double[trials];
        for (int i = 0; i < trials; i++) {
            x[i] = simulate(n);
        }
        mean = StdStats.mean(x);
        stddev = StdStats.stddev(x);
        confidenceLo = mean - (1.96 * stddev) / Math.sqrt(trials);
        confidenceHi = mean + (1.96 * stddev) / Math.sqrt(trials);
    }

    private double simulate(int n) {
        Percolation perc = new Percolation(n);
        while (true) {
            perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            if (perc.percolates()) {
                double result = perc.numberOfOpenSites() / (double) (n * n);
                return result;
            }
        }
    }

    public double mean() {                         // sample mean of percolation threshold
        return mean;
    }

    public double stddev() {                       // sample standard deviation of percolation threshold
        return stddev;
    }

    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return confidenceLo;
    }

    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return confidenceHi;
    }

    public static void main(String[] args) {       // test client (described below)
        PercolationStats percStats = new PercolationStats(100, 20);
        System.out.println(percStats.mean);
    }
}
