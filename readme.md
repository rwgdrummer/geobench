# Benchmark Tools

com.robertson.GeoBenchmark:  compare prepared vs. non prepared JTS geometry operations (e.g. intersection).  

com.robertson.GeoInternedBenchmark: compare interned prepared and non prepared JTS geometry with some overhead for maintaining the interned image.
Prepared Geomerties do not implement Object.hashCode(), thus a separated wrapper image is maintained. Furthermore, to measure the overhead of construction,
the geometry is maintained in byte[] array (as the key).

com.robertson.GeoBenchmarkWithShape: create benchmarks for comparing random sets of geometries to geometries in the shape file.  This can be used to measure
the performance between different granularities of a specific shape.



## Sample Run
<pre>
mvn clean compile
mvn exec:exec
</pre>



## Sample set of output data


### com.robertson.geobench.GeoBenchmark


The test compared two driving geometries to a thousand geometries over ten iterations.  Each iteration increases the number of points in the two driving geometries
by a factor of 'i' where 'i' is the iteration number.  

```
----iteration = 1-----
CompareResult [ duration=94265402, durationInSecs=0.094265402, count=1, algorithm=JTS Prepared Intersection, statName=count, value=2.0, mem=0]
CompareResult [ duration=3035839680, durationInSecs=3.03583968, count=1, algorithm=JTS Intersection, statName=count, value=2.0, mem=0]

----iteration = 2-----
CompareResult [ duration=79740512, durationInSecs=0.079740512, count=2, algorithm=JTS Prepared Intersection, statName=count, value=4.0, mem=0]
CompareResult [ duration=7955932109, durationInSecs=7.955932109, count=2, algorithm=JTS Intersection, statName=count, value=4.0, mem=0]

----iteration = 3-----
CompareResult [ duration=77883991, durationInSecs=0.077883991, count=3, algorithm=JTS Prepared Intersection, statName=count, value=6.0, mem=0]
CompareResult [ duration=19547602628, durationInSecs=19.547602628, count=3, algorithm=JTS Intersection, statName=count, value=6.0, mem=0]

----iteration = 4-----
CompareResult [ duration=79126607, durationInSecs=0.079126607, count=4, algorithm=JTS Prepared Intersection, statName=count, value=8.0, mem=0]
CompareResult [ duration=40767188218, durationInSecs=40.767188218, count=4, algorithm=JTS Intersection, statName=count, value=8.0, mem=0]

----iteration = 5-----
CompareResult [ duration=196091891, durationInSecs=0.196091891, count=5, algorithm=JTS Prepared Intersection, statName=count, value=10.0, mem=0]
CompareResult [ duration=69048456159, durationInSecs=69.048456159, count=5, algorithm=JTS Intersection, statName=count, value=10.0, mem=0]

----iteration = 6-----
CompareResult [ duration=314645087, durationInSecs=0.314645087, count=6, algorithm=JTS Prepared Intersection, statName=count, value=12.0, mem=0]
CompareResult [ duration=104467502504, durationInSecs=104.467502504, count=6, algorithm=JTS Intersection, statName=count, value=12.0, mem=0]

----iteration = 7-----
CompareResult [ duration=361935246, durationInSecs=0.361935246, count=7, algorithm=JTS Prepared Intersection, statName=count, value=14.0, mem=0]
CompareResult [ duration=154677083819, durationInSecs=154.677083819, count=7, algorithm=JTS Intersection, statName=count, value=14.0, mem=0]

----iteration = 8-----
CompareResult [ duration=357068985, durationInSecs=0.357068985, count=8, algorithm=JTS Prepared Intersection, statName=count, value=16.0, mem=0]
CompareResult [ duration=218353731620, durationInSecs=218.35373162, count=8, algorithm=JTS Intersection, statName=count, value=16.0, mem=0]

----iteration = 9-----
CompareResult [ duration=476454270, durationInSecs=0.47645427, count=9, algorithm=JTS Prepared Intersection, statName=count, value=18.0, mem=0]
CompareResult [ duration=284053408813, durationInSecs=284.053408813, count=9, algorithm=JTS Intersection, statName=count, value=18.0, mem=0]

----iteration = 10-----
CompareResult [ duration=735402195, durationInSecs=0.735402195, count=10, algorithm=JTS Prepared Intersection, statName=count, value=20.0, mem=0]
CompareResult [ duration=382627357457, durationInSecs=382.627357457, count=10, algorithm=JTS Intersection, statName=count, value=20.0, mem=0]
```

### com.robertson.geobench.GeoInternedBenchmark

Comare interned vs. non-interned for 1000 data items and 1 through 10 different prepared Geometries.  Inspect memory usage.
Result is that interning is roughtly 10 times faster and 30% less memory in this particular test.

##### Running without interning: (command line without)
```
----iteration = 1-----
CompareResult [ duration=9485942696, durationInSecs=9.485942696, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=15.0, mem=0]

----iteration = 2-----
CompareResult [ duration=18060667906, durationInSecs=18.060667906, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=30.0, mem=0]

----iteration = 3-----
CompareResult [ duration=27894375822, durationInSecs=27.894375822, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=45.0, mem=0]

----iteration = 4-----
CompareResult [ duration=36539367294, durationInSecs=36.539367294, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=60.0, mem=0]

----iteration = 5-----
CompareResult [ duration=45560556230, durationInSecs=45.56055623, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=75.0, mem=0]

----iteration = 6-----
CompareResult [ duration=53808944503, durationInSecs=53.808944503, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=90.0, mem=0]

----iteration = 7-----
CompareResult [ duration=62030068347, durationInSecs=62.030068347, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=105.0, mem=0]

----iteration = 8-----
CompareResult [ duration=69631987306, durationInSecs=69.631987306, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=120.0, mem=0]

----iteration = 9-----
CompareResult [ duration=79982944970, durationInSecs=79.98294497, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=135.0, mem=0]

----iteration = 10-----
CompareResult [ duration=88064234980, durationInSecs=88.06423498, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=150.0, mem=0]

Used Memory:236.80003356933594
Free Memory:608.1999664306641
Total Memory:845.0
Max Memory:1820.5
```

##### Running with interning: (command line with)
```
----iteration = 1-----
CompareResult [ duration=1482827147, durationInSecs=1.482827147, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=15.0, mem=0]

----iteration = 2-----
CompareResult [ duration=2279505565, durationInSecs=2.279505565, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=30.0, mem=0]

----iteration = 3-----
CompareResult [ duration=3492514062, durationInSecs=3.492514062, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=45.0, mem=0]

----iteration = 4-----
CompareResult [ duration=5065656529, durationInSecs=5.065656529, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=60.0, mem=0]

----iteration = 5-----
CompareResult [ duration=5302964064, durationInSecs=5.302964064, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=75.0, mem=0]

----iteration = 6-----
CompareResult [ duration=5367386497, durationInSecs=5.367386497, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=90.0, mem=0]

----iteration = 7-----
CompareResult [ duration=6922796752, durationInSecs=6.922796752, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=105.0, mem=0]

----iteration = 8-----
CompareResult [ duration=8163410434, durationInSecs=8.163410434, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=120.0, mem=0]

----iteration = 9-----
CompareResult [ duration=9449976813, durationInSecs=9.449976813, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=135.0, mem=0]

----iteration = 10-----
CompareResult [ duration=9923827260, durationInSecs=9.92382726, count=1000, algorithm=JTS Prepared Intersection With Intern, statName=count, value=150.0, mem=0]

Used Memory:339.4459457397461
Free Memory:560.0540542602539
Total Memory:899.5
Max Memory:1820.5
```