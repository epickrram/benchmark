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


import org.openjdk.jol.util.VMSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.lang.Integer.parseInt;

public final class Allocator
{
    private final int bufferSize;
    private final int objectCount;
    private Data[] buffer;

    public Allocator(final int bufferSize, final int objectCount)
    {
        this.bufferSize = bufferSize;
        this.objectCount = objectCount;
    }

    private void allocate()
    {
        this.buffer = new Data[objectCount];

        for(int i = 0; i < objectCount; i++)
        {
            buffer[i] = new Data();
            buffer[i].cache = new byte[bufferSize];
        }

    }

    private void forceGc()
    {
        for(int i = 0; i < 5; i++)
        {
            System.gc();
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1L));
        }
    }

    private void printObjectAddresses()
    {
        System.out.println("Starting audit");
        long lastObjectNativeAddress = 0L;
        long lastCacheNativeAddress = 0L;
        long lastObjectDelta = 0L;
        long lastCacheDelta = 0L;
        long lastFieldDelta = 0L;
        int parentObjectDeltaOffsetCount = 0;
        int objectFieldDeltaOffsetCount = 0;
        int fieldDeltaOffsetCount = 0;
        for(int i = 0; i < objectCount; i++)
        {
            final long dataAddress = VMSupport.addressOf(buffer[i]);
            final long nativeDataAddress = VMSupport.toNativeAddress(dataAddress);

            final long cacheAddress = VMSupport.addressOf(buffer[i].cache);
            final long nativeCacheAddress = VMSupport.toNativeAddress(cacheAddress);
            final long deltaObjectAddress = lastObjectNativeAddress - nativeDataAddress;
            final long deltaCacheAddress = lastCacheNativeAddress - nativeCacheAddress;

            final long fieldDelta = dataAddress - cacheAddress;

            if(lastFieldDelta != 0)
            {
                if(fieldDelta != lastFieldDelta)
                {
                    fieldDeltaOffsetCount++;
                }
            }

//            System.out.printf("%d %d %d %d %d %d %d%n",
//                    i, dataAddress, nativeDataAddress, cacheAddress, nativeCacheAddress, deltaObjectAddress, deltaCacheAddress);

            if(lastObjectNativeAddress != 0L)
            {
                if(lastObjectDelta != 0L && lastObjectDelta != deltaObjectAddress)
                {
                    parentObjectDeltaOffsetCount++;
                }
                if(lastCacheDelta != 0L && lastCacheDelta != deltaCacheAddress)
                {
                    objectFieldDeltaOffsetCount++;
                }

                lastObjectDelta = deltaObjectAddress;
                lastCacheDelta = deltaCacheAddress;
            }

            lastObjectNativeAddress = nativeDataAddress;
            lastCacheNativeAddress = nativeCacheAddress;
            lastFieldDelta = fieldDelta;
        }

        System.out.printf("Parent objects with differing stride: %d (%.2f%%), fields with differing stride: %d (%.2f%%), object-to-field strides: %d%n",
                parentObjectDeltaOffsetCount, (parentObjectDeltaOffsetCount/(float)objectCount)*100,
                objectFieldDeltaOffsetCount, (objectFieldDeltaOffsetCount/(float)objectCount)*100,
                fieldDeltaOffsetCount);
    }

    private static final class Data
    {
        private long id;
        private long timestamp;
        private int valueOne;
        private int valueTwo;
        private byte[] cache;
    }

    public static void main(final String[] args)
    {
        final int bufferSize = parseInt(args[0]);
        final int objectCount = parseInt(args[1]);

        final Allocator allocator = new Allocator(bufferSize, objectCount);
        allocator.allocate();
        allocator.forceGc();
        allocator.printObjectAddresses();
    }
}
