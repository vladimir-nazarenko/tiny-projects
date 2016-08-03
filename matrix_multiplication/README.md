Here are naive benchmarks of even more naive matrix multiplication implementations. Long story short, here is the output of the jmh:

|Benchmark                    |  (N) |   Mode |  Cnt |     Score |     Error | Units|
|-----------------------------|------|--------|------|-----------|-----------|------|
|MatrixBenchmarks.cpuParallel |  100 | sample | 8857 |     0.788 |±    0.021 | ms/op|
|MatrixBenchmarks.cpuParallel | 1000 | sample |   24 |   342.316 |±   76.542 | ms/op|
|MatrixBenchmarks.cpuParallel | 1500 | sample |   10 |  1377.619 |±  554.037 | ms/op|
|MatrixBenchmarks.ikj         |  100 | sample | 7408 |     0.945 |±    0.007 | ms/op|
|MatrixBenchmarks.ikj         | 1000 | sample |   11 |  1157.151 |±  424.884 | ms/op|
|MatrixBenchmarks.ikj         | 1500 | sample |    7 |  4750.349 |± 2063.427 | ms/op|
|MatrixBenchmarks.naive       |  100 | sample | 3823 |     1.831 |±    0.017 | ms/op|
|MatrixBenchmarks.naive       | 1000 | sample |    9 |  4049.834 |± 1196.254 | ms/op|
|MatrixBenchmarks.naive       | 1500 | sample |    7 | 25630.793 |± 4443.870 | ms/op|
|MatrixBenchmarks.recursive64 |  100 | sample | 1792 |     3.909 |±    0.031 | ms/op|
|MatrixBenchmarks.recursive64 | 1000 | sample |    7 |  4000.168 |±  668.229 | ms/op|
|MatrixBenchmarks.recursive64 | 1500 | sample |    7 | 13397.805 |±  463.223 | ms/op|
|MatrixBenchmarks.rowWise     |  100 | sample | 6799 |     1.029 |±    0.010 | ms/op|
|MatrixBenchmarks.rowWise     | 1000 | sample |    7 |  1891.931 |±   71.081 | ms/op|
|MatrixBenchmarks.rowWise     | 1500 | sample |    8 |  4846.518 |± 3189.402 | ms/op|
|MatrixBenchmarks.strassen64  |  100 | sample | 3491 |     2.006 |±    0.035 | ms/op|
|MatrixBenchmarks.strassen64  | 1000 | sample |   14 |   738.198 |±   85.847 | ms/op|
|MatrixBenchmarks.strassen64  | 1500 | sample |    7 |  5034.363 |±  148.535 | ms/op|

I found it strange that recursive (divide and conquer) implementation is so slow. Profiler says that there are a lot of iTLB misses, but I'm not that pro to solve this problem.  
Also there is an optimal matrix layout, based on peano curve, but I hava ommited it from this benchmarks because I'm already tired of all that index stuff.

Also you may find interesting the test setup (it took me some time to find a graceful way of testing subclasses of an abstract class), and, if you are novice with either jmh ot maven, you also may find some examples of these techs in this project.
