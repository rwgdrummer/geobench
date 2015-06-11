# Benchmark Tools

**com.robertson.geobench.GeoBenchmark**:  compare prepared vs. non prepared JTS geometry operations (e.g. intersection).  

**com.robertson.geobench.GeoInternedBenchmark**: compare interned prepared and non prepared JTS geometry with some overhead for maintaining the interned image.
Prepared Geomerties do not implement Object.hashCode(), thus a separated wrapper image is maintained. Furthermore, to measure the overhead of construction,
the geometry is maintained in byte[] array (as the key).

**com.robertson.geobench.GeoBenchmarkWithShape**: create benchmarks for comparing random sets of geometries to geometries in the shape file.  This can be used to measure
the performance between different granularities of a specific shape.



## Sample Run
<pre>
mvn clean compile
mvn exec:exec
</pre>



## Sample set of output data


### com.robertson.geobench.GeoBenchmark


The test compared two driving geometries to a thousand geometries for five iterations.  Each iteration increases the number of points in the two driving geometries
by a factor of 'i' where 'i' is the iteration number.    The results show the effectivness of preparing a geometry increases proportionally as the complexity
of geometry increases.

```
----iteration = 1-----
CompareResult [ duration=554303969, durationInSecs=0.554303969, count=1000, algorithm=JTS Prepared Intersection, statName=count, value=30.0]
CompareResult [ duration=16642083002, durationInSecs=16.642083002, count=1000, algorithm=JTS Intersection, statName=count, value=30.0]

----iteration = 2-----
CompareResult [ duration=544393212, durationInSecs=0.544393212, count=1000, algorithm=JTS Prepared Intersection, statName=count, value=30.0]
CompareResult [ duration=36147822978, durationInSecs=36.147822978, count=1000, algorithm=JTS Intersection, statName=count, value=30.0]

----iteration = 3-----
CompareResult [ duration=777077045, durationInSecs=0.777077045, count=1000, algorithm=JTS Prepared Intersection, statName=count, value=30.0]
CompareResult [ duration=68208585100, durationInSecs=68.2085851, count=1000, algorithm=JTS Intersection, statName=count, value=30.0]

----iteration = 4-----
CompareResult [ duration=1200043231, durationInSecs=1.200043231, count=1000, algorithm=JTS Prepared Intersection, statName=count, value=30.0]
CompareResult [ duration=120539701834, durationInSecs=120.539701834, count=1000, algorithm=JTS Intersection, statName=count, value=30.0]

----iteration = 5-----
CompareResult [ duration=989814138, durationInSecs=0.989814138, count=1000, algorithm=JTS Prepared Intersection, statName=count, value=30.0]
CompareResult [ duration=171163920484, durationInSecs=171.163920484, count=1000, algorithm=JTS Intersection, statName=count, value=30.0]

```

### com.robertson.geobench.GeoInternedBenchmark

Comare interned vs. non-interned for 1000 data items and 1 through 10 different prepared Geometries.  Inspect memory usage.
Result is that interning is roughtly 10 times faster and 20% less memory in this particular test.

##### Running without interning: (command line without)
```
..........
----iteration = 1-----
CompareResult [ duration=9245741079, durationInSecs=9.245741079, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=15.0]

----iteration = 2-----
CompareResult [ duration=19967681228, durationInSecs=19.967681228, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=30.0]

----iteration = 3-----
CompareResult [ duration=28553679169, durationInSecs=28.553679169, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=45.0]

----iteration = 4-----
CompareResult [ duration=36562460801, durationInSecs=36.562460801, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=60.0]

----iteration = 5-----
CompareResult [ duration=45204086204, durationInSecs=45.204086204, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=75.0]

----iteration = 6-----
CompareResult [ duration=53701260007, durationInSecs=53.701260007, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=90.0]

----iteration = 7-----
CompareResult [ duration=63311614954, durationInSecs=63.311614954, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=105.0]

----iteration = 8-----
CompareResult [ duration=67956152458, durationInSecs=67.956152458, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=120.0]

----iteration = 9-----
CompareResult [ duration=77634651751, durationInSecs=77.634651751, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=135.0]

----iteration = 10-----
CompareResult [ duration=85915494357, durationInSecs=85.915494357, count=1000, algorithm=JTS Prepared Intersection Without Intern, statName=count, value=150.0]

Used Memory:531.3118133544922
Free Memory:544.6881866455078
Total Memory:1076.0
Max Memory:1820.5

```

##### Running with interning: (command line with)
```
..........
----iteration = 1-----
CompareResult [ duration=1849656939, durationInSecs=1.849656939, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=15.0]

----iteration = 2-----
CompareResult [ duration=2247486273, durationInSecs=2.247486273, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=30.0]

----iteration = 3-----
CompareResult [ duration=3542926196, durationInSecs=3.542926196, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=45.0]

----iteration = 4-----
CompareResult [ duration=4824038530, durationInSecs=4.82403853, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=60.0]

----iteration = 5-----
CompareResult [ duration=5120748098, durationInSecs=5.120748098, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=75.0]

----iteration = 6-----
CompareResult [ duration=5634712861, durationInSecs=5.634712861, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=90.0]

----iteration = 7-----
CompareResult [ duration=6960461037, durationInSecs=6.960461037, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=105.0]

----iteration = 8-----
CompareResult [ duration=7528478340, durationInSecs=7.52847834, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=120.0]

----iteration = 9-----
CompareResult [ duration=9481588442, durationInSecs=9.481588442, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=135.0]

----iteration = 10-----
CompareResult [ duration=8667329716, durationInSecs=8.667329716, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=150.0]

Used Memory:517.4288558959961
Free Memory:341.0711441040039
Total Memory:858.5
Max Memory:1820.5

```