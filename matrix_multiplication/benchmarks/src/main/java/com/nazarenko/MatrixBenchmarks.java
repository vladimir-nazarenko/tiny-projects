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

    @Param({"100", "1000", "1500"})
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
    public void ikj(Blackhole blackhole) {
        final Multiplier mult = new IKJMultiplier();
        blackhole.consume(mult.multiply(left, right));
    }

    @Benchmark
    public void recursive64(Blackhole blackhole) {
        final Multiplier mult = new RecursiveMultiplier(new IKJMultiplier(), 64);
        blackhole.consume(mult.multiply(left, right));
    }

    @Benchmark
    public void strassen64(Blackhole blackhole) {
        final Multiplier mult = new StrassenMultiplier(new IKJMultiplier(), 64);
        blackhole.consume(mult.multiply(left, right));
    }

    @Benchmark
    public void cpuParallel(Blackhole blackhole) {
        final Multiplier mult = new ParallelCPUMultiplier(new IKJMultiplier());
        blackhole.consume(mult.multiply(left, right));
    }
}
