package com.epickrram.benchmark.tlab;

//////////////////////////////////////////////////////////////////////////////////
//   Copyright 2014   Mark Price     mark at epickrram.com                      //
//                                                                              //
//   Licensed under the Apache License, Version 2.0 (the "License");            //
//   you may not use this file except in compliance with the License.           //
//   You may obtain a copy of the License at                                    //
//                                                                              //
//       http://www.apache.org/licenses/LICENSE-2.0                             //
//                                                                              //
//   Unless required by applicable law or agreed to in writing, software        //
//   distributed under the License is distributed on an "AS IS" BASIS,          //
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   //
//   See the License for the specific language governing permissions and        //
//   limitations under the License.                                             //
//////////////////////////////////////////////////////////////////////////////////


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static com.epickrram.benchmark.tlab.BenchmarkParams.*;

@State(Scope.Benchmark)
public class UnsafeOffHeapMemoryStridingBenchmark
{
    private long sequenceNumber = 0;
    private final UnsafeOffHeapObjectBuffer unsafeOffHeapObjectBuffer = new UnsafeOffHeapObjectBuffer(OBJECT_COUNT, DATA_BUFFER_SIZE);

    @Benchmark
    public long timeUnsafeOffHeapStride()
    {
        final long busyWork = unsafeOffHeapObjectBuffer.setAt(sequenceNumber, SOURCE);
        sequenceNumber++;
        return busyWork;
    }
}
