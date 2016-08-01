package com.nazarenko;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;


/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */
@Warmup(iterations = 3)
@Measurement(iterations = 7)
@BenchmarkMode(Mode.SampleTime)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class MatrixBenchmarks {

    @Param({"10", "100", "1000", "3000"})
    public int N;
    int[][] left;
    int[][] right;

    @Setup
    public void prepareMatrices() {
        final MatrixProvider provider = new MatrixProvider(N, N, N);
        left  = provider.getLeftMatrix();
        right = provider.getRightMatrix();
    }

    @Benchmark
    public void naive(Blackhole blackhole) {
        final Multiplier mult = new StraightforwardMultiplier();
        blackhole.consume(mult.multiply(left, right));
    }

    @Benchmark
    public void rowWise(Blackhole blackhole) {
        final Multiplier mult = new RowWiseMultiplier();
        blackhole.consume(mult.multiply(left, right));
    }

    @Benchmark
    public void recursive(Blackhole blackhole) {
        final Multiplier mult = new RecursiveMultiplier();
        blackhole.consume(mult.multiply(left, right));
    }

    @Benchmark
    public void strassen(Blackhole blackhole) {
        final Multiplier mult = new StrassenMultiplier();
        blackhole.consume(mult.multiply(left, right));
    }
}
