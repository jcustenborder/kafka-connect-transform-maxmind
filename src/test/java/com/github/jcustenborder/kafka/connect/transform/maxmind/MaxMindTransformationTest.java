/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
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
package com.github.jcustenborder.kafka.connect.transform.maxmind;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

public class MaxMindTransformationTest {
  MaxMindTransformation transformation;

  @BeforeEach
  public void before() {

    Map<String, ?> settings = ImmutableMap.of(
        MaxMindTransformationConfig.FIELD_INPUT_CONFIG, "ipAddress",
        MaxMindTransformationConfig.FIELD_OUTPUT_CONFIG, "geoIpData",
        MaxMindTransformationConfig.MAXMIND_DATABASE_PATH_CONFIG, new File("data/GeoLite2-City.mmdb").getAbsolutePath()
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
