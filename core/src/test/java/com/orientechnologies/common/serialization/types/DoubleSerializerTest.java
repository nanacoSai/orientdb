/*
 * Copyright 2010-2012 Luca Garulli (l.garulli--at--orientechnologies.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orientechnologies.common.serialization.types;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Ilya Bershadskiy (ibersh20-at-gmail.com)
 * @since 18.01.12
 */
@Test
public class DoubleSerializerTest {
  private static final int    FIELD_SIZE = 8;
  private static final Double OBJECT     = Math.PI;
  private ODoubleSerializer doubleSerializer;
  byte[] stream = new byte[FIELD_SIZE];

  @BeforeClass
  public void beforeClass() {
    doubleSerializer = new ODoubleSerializer();
  }

  public void testFieldSize() {
    Assert.assertEquals(doubleSerializer.getObjectSize(null), FIELD_SIZE);
  }

  public void testSerialize() {
    doubleSerializer.serialize(OBJECT, stream, 0);
    Assert.assertEquals(doubleSerializer.deserialize(stream, 0), OBJECT);
  }

  public void testSerializeNative() {
    doubleSerializer.serializeNative(OBJECT, stream, 0);
    Assert.assertEquals(doubleSerializer.deserializeNative(stream, 0), OBJECT);
  }

  public void testNativeDirectMemoryCompatibility() {
    doubleSerializer.serializeNative(OBJECT, stream, 0);

    ByteBuffer buffer = ByteBuffer.allocateDirect(stream.length).order(ByteOrder.nativeOrder());
    buffer.put(stream);
    buffer.position(0);

    Assert.assertEquals(doubleSerializer.deserializeFromByteBufferObject(buffer), OBJECT);
  }

}
