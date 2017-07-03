/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.kafka.connect.transform.maxmind;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxMindTransformationTest {
  MaxMindTransformation transformation;

  static File geoLiteDataFile;

  @BeforeAll
  public static void beforeAll() throws IOException {
    geoLiteDataFile = new File("data/GeoLite2-City.mmdb");
    if (geoLiteDataFile.isFile() && geoLiteDataFile.exists()) {
      return;
    }

    URL md5Url = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.tar.gz.md5");
    final String expectedHash;
    try(InputStream inputStream = md5Url.openStream()) {
      try(InputStreamReader reader = new InputStreamReader(inputStream, Charsets.UTF_8)){
        expectedHash = CharStreams.toString(reader);
      }
    }

    final String actualHash;
    URL databaseUrl = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.tar.gz");
    try (InputStream webStream = databaseUrl.openStream()) {
      try(HashingInputStream hashingInputStream = new HashingInputStream(Hashing.md5(), webStream)) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(hashingInputStream)) {
          try (ReadableByteChannel inputChannel = Channels.newChannel(gzipInputStream)) {
            try (FileOutputStream outputStream = new FileOutputStream(geoLiteDataFile)) {
              try (FileChannel outputChannel = outputStream.getChannel()) {
                outputChannel.transferFrom(inputChannel, 0L, Long.MAX_VALUE);
              }
            }
          }
        }
        actualHash = hashingInputStream.hash().toString();
      }
    }

    assertEquals(expectedHash, actualHash, "hashes do not match.");
  }

  @BeforeEach
  public void before() {

    Map<String, ?> settings = ImmutableMap.of(
        MaxMindTransformationConfig.FIELD_INPUT_CONFIG, "ipAddress",
        MaxMindTransformationConfig.FIELD_OUTPUT_CONFIG, "geoIpData",
        MaxMindTransformationConfig.MAXMIND_DATABASE_PATH_CONFIG, geoLiteDataFile.getAbsolutePath()
    );

    this.transformation = new MaxMindTransformation();
    this.transformation.configure(settings);
  }

  @Test
  public void test() {
    final Schema inputSchema = SchemaBuilder.struct()
        .doc("Testing")
        .field("ipAddress", Schema.OPTIONAL_STRING_SCHEMA)
        .build();
    final Struct inputStruct = new Struct(inputSchema)
        .put("ipAddress", "8.8.8.8");

    final ConnectRecord inputRecord = new SinkRecord(
        "test",
        1,
        null,
        null,
        inputSchema,
        inputStruct,
        System.currentTimeMillis()
    );

    final ConnectRecord outputRecord = this.transformation.apply(inputRecord);
  }

  @AfterEach
  public void after() {
    this.transformation.close();
  }


}
