package com.epickrram.benchmark.branch;

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
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Random;

@State(Scope.Benchmark)
public class BranchBenchmark2
{
    private static final int DATA_SET_SIZE = 65536;
    private static final int MASK = DATA_SET_SIZE - 1;
    private final int[] pseudoRandom = new int[DATA_SET_SIZE];
    private final int[] alwaysIncreasing = new int[DATA_SET_SIZE];
    private final int[] alwaysDecreasing = new int[DATA_SET_SIZE];
    private final int[] alwaysFlipping = new int[DATA_SET_SIZE];

    private int pointer = 0;

    @Setup
    public void setup()
    {
        final Random random = new Random(1234567890L);
        for(int i = 0; i < DATA_SET_SIZE; i++)
        {
            pseudoRandom[i] = random.nextInt();
            alwaysIncreasing[i] = i;
            alwaysDecreasing[i] = DATA_SET_SIZE - i;
            alwaysFlipping[i] = (i % 2) == 0 ? 100 : 0;
        }
    }

    @Benchmark
    public int branchWithRandomSet()
    {
        return Math.max(pseudoRandom[pointer++ & MASK], pseudoRandom[pointer++ & MASK]);
    }

    @Benchmark
    public int branchWithUniformAlwaysIncreasingSet()
    {
        return Math.max(alwaysIncreasing[pointer++ & MASK], alwaysIncreasing[pointer++ & MASK]);
    }

    @Benchmark
    public int branchWithUniformAlwaysDecreasingSet()
    {
        return Math.max(alwaysDecreasing[pointer++ & MASK], alwaysDecreasing[pointer++ & MASK]);
    }

    @Benchmark
    public int branchWithUniformAlwaysFlippingSet()
    {
        return Math.max(alwaysFlipping[pointer++ & MASK], alwaysFlipping[pointer++ & MASK]);
    }
}