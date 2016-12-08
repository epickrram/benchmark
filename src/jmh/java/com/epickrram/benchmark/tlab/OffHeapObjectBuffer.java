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


import java.nio.ByteBuffer;

public final class OffHeapObjectBuffer implements MessageBuffer
{
    private final ByteBuffer storage;
    private final int mask;
    private final int elementSize;

    public OffHeapObjectBuffer(final int size, final int dataBufferSize)
    {
        if(Integer.bitCount(size) != 1)
        {
            throw new IllegalStateException("Size must be a power of two");
        }
        elementSize = dataBufferSize + 8;
        storage = ByteBuffer.allocateDirect(size * (elementSize));
        mask = size - 1;
    }

    @Override
    public long setAt(final long sequenceNumber, final byte[] data)
    {
        final int index = mask(sequenceNumber);
        final int offset = elementSize * index;

        storage.position(offset);
        storage.putLong(sequenceNumber);
        storage.put(data);

        return storage.position();
    }

    @Override
    public long getAt(final long sequenceNumber)
    {
        final int index = mask(sequenceNumber);
        final int offset = elementSize * index;

        storage.position(offset);
        return storage.getLong();
    }

    private int mask(final long sequenceNumber)
    {
        return (int) (sequenceNumber & mask);
    }
}
