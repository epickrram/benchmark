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


import com.epickrram.benchmark.util.UnsafeAccess;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public final class UnsafeOffHeapObjectBuffer implements MessageBuffer
{
    private final ByteBuffer storage;
    private final int mask;
    private final int elementSize;
    private final long baseAddress;
    private final Unsafe unsafe = UnsafeAccess.getUnsafe();
    private static final long BASE_OFFSET  = UnsafeAccess.getUnsafe().arrayBaseOffset(byte[].class);

    public UnsafeOffHeapObjectBuffer(final int size, final int dataBufferSize)
    {
        if(Integer.bitCount(size) != 1)
        {
            throw new IllegalStateException("Size must be a power of two");
        }
        elementSize = dataBufferSize + 8;
        storage = ByteBuffer.allocateDirect(size * (elementSize));
        baseAddress = getBufferAddress(storage);
        mask = size - 1;
    }

    @Override
    public long setAt(final long sequenceNumber, final byte[] data)
    {
        final int index = mask(sequenceNumber);
        final int offset = elementSize * index;

        final long dataOffset = baseAddress + offset;
        unsafe.putLong(dataOffset, sequenceNumber);
        unsafe.copyMemory(data, BASE_OFFSET, null, dataOffset, data.length);

        return index;
    }

    @Override
    public long getAt(final long sequenceNumber)
    {
        final int index = mask(sequenceNumber);
        final int offset = elementSize * index;

        final long dataOffset = baseAddress + offset;
        return unsafe.getLong(dataOffset);
    }

    private int mask(final long sequenceNumber)
    {
        return (int) (sequenceNumber & mask);
    }

    private static long getBufferAddress(final Buffer buffer)
    {
        try
        {
            final Field field = Buffer.class.getDeclaredField("address");
            field.setAccessible(true);
            return ((Long) field.get(buffer)).longValue();
        }
        catch (NoSuchFieldException e)
        {
            throw new IllegalStateException("Unable to get buffer address", e);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalStateException("Unable to get buffer address", e);
        }
    }
}
