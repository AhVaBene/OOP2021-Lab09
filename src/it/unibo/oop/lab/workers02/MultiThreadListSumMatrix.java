package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadListSumMatrix implements SumMatrix {

    private final int nthread;
    
    public MultiThreadListSumMatrix(final int t) {
        this.nthread = t;
    }
    
    private class Worker extends Thread{
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private long res;
        
        public Worker(final double[][] matrix, final int startpos, final int nelem) {
            super();
            this.matrix = matrix;
            this.startpos = startpos;
            this.nelem = nelem;
        }
        
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < matrix[0].length && i < startpos + nelem; i++) {
                for(int j = 0; j < matrix[0].length; j++) {
                    this.res += this.matrix[0][i]; 
                } 
            }
        }
        public long getResult() {
            return this.res;
        }
    }
    @Override
    public double sum(final double[][] matrix) {
        // TODO Auto-generated method stub
        final int size = matrix[0].length % nthread + matrix[0].length / nthread;
        long sum = 0;
        final List<Worker> workers = new ArrayList<>(nthread);
        for(int start = 0; start < matrix[0].length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (final Worker w: workers) {
            w.start();
        }
        for(final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

}
