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

import com.github.jcustenborder.kafka.connect.utils.config.Description;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.InsightsResponse;
import com.maxmind.geoip2.model.IspResponse;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Description("This transformation is used to lookup data from a MaxMind " +
    "[database](http://dev.maxmind.com/geoip/geoip2/geolite2/#Downloads) and append the data to an existing struct.")
public class MaxMindTransformation<R extends ConnectRecord<R>> implements Transformation<R> {
  private static final Logger log = LoggerFactory.getLogger(MaxMindTransformation.class);
  private MaxMindTransformationConfig config;
  private DatabaseReader reader;
  private Map<Schema, Schema> schemaLookup = new HashMap<>();


  @Override
  public R apply(R record) {
    Preconditions.checkState(
        null != record.valueSchema() && Schema.Type.STRUCT == record.valueSchema().type(),
        "record.valueSchema() must be a struct."
    );

    if (null == record.value()) {
      return record;
    }

    final Schema outputSchema = schemaLookup.computeIfAbsent(record.valueSchema(), inputSchema -> {
      SchemaBuilder builder = SchemaBuilder.struct();
      if (!Strings.isNullOrEmpty(inputSchema.name())) {
        builder.name(inputSchema.name());
      }
      if (!Strings.isNullOrEmpty(inputSchema.doc())) {
        builder.doc(inputSchema.name());
      }
      if (inputSchema.isOptional()) {
        builder.optional();
      }
      if (null != inputSchema.version()) {
        builder.version(inputSchema.version() + 1);
      }
      for (Field field : inputSchema.fields()) {
        builder.field(field.name(), field.schema());
      }
      builder.field(config.fieldOutput, Schemas.SCHEMA_MAXMIND);
      return builder.build();
    });

    final Struct inputValue = (Struct) record.value();
    final Struct outputValue = new Struct(outputSchema);
    final Field inputField = record.valueSchema().field(this.config.fieldInput);

    for (Field field : record.valueSchema().fields()) {
      log.trace("apply() - Copying field '{}' to new struct.", field.name());
      outputValue.put(field.name(), inputValue.get(field));
    }

    final Object inputFieldValue = inputValue.get(inputField);
    InetAddress address = null;

    if (null != inputFieldValue) {
      try {
        switch (inputField.schema().type()) {
          case BYTES:
            byte[] buffer = inputValue.getBytes(inputField.name());
            address = InetAddress.getByAddress(buffer);
            break;
          case STRING:
            String s = inputValue.getString(inputField.name());
            address = InetAddress.getByName(s);
            break;
          default:
            address = null;
            break;
        }
      } catch (UnknownHostException ex) {
        log.warn("Could not convert '{}' to InetAddress.", inputFieldValue, ex);
      }
    } else {
      address = null;
    }

    log.trace("apply() - address = '{}'", address);

    final AnonymousIpResponse anonymousIpResponse;
    final AsnResponse asnResponse;
    final CityResponse cityResponse;
    final CountryResponse countryResponse;
    final ConnectionTypeResponse connectionTypeResponse;
    final DomainResponse domainResponse;
    final EnterpriseResponse enterpriseResponse;
    final IspResponse ispResponse;
    final InsightsResponse insightsResponse = null;
    Struct geoIpStruct = null;

    if (null != address) {
      try {
        if (this.config.maxmindAnonymousIp) {
          log.trace("apply() - Calling anonymousIp('{}')", address);
          anonymousIpResponse = this.reader.anonymousIp(address);
        } else {
          anonymousIpResponse = null;
        }

        if (this.config.maxmindAsn) {
          log.trace("apply() - Calling asn('{}')", address);
          asnResponse = this.reader.asn(address);
        } else {
          asnResponse = null;
        }

        if (this.config.maxmindCity) {
          log.trace("apply() - Calling city('{}')", address);
          cityResponse = this.reader.city(address);
        } else {
          cityResponse = null;
        }

        if (this.config.maxmindConnectionType) {
          log.trace("apply() - Calling connectionType('{}')", address);
          connectionTypeResponse = this.reader.connectionType(address);
        } else {
          connectionTypeResponse = null;
        }

        if (this.config.maxmindCountry) {
          log.trace("apply() - Calling country('{}')", address);
          countryResponse = this.reader.country(address);
        } else {
          countryResponse = null;
        }

        if (this.config.maxmindDomain) {
          log.trace("apply() - Calling domain('{}')", address);
          domainResponse = this.reader.domain(address);
        } else {
          domainResponse = null;
        }

        if (this.config.maxmindEnterprise) {
          log.trace("apply() - Calling enterprise('{}')", address);
          enterpriseResponse = this.reader.enterprise(address);
        } else {
          enterpriseResponse = null;
        }

        if (this.config.maxmindIsp) {
          log.trace("apply() - Calling isp('{}')", address);
          ispResponse = this.reader.isp(address);
        } else {
          ispResponse = null;
        }

        geoIpStruct = Schemas.struct(anonymousIpResponse, asnResponse, cityResponse, connectionTypeResponse, countryResponse, domainResponse, enterpriseResponse, insightsResponse, ispResponse);
      } catch (GeoIp2Exception ex) {
        log.warn("Exception thrown while querying '{}'", address, ex);
      } catch (IOException ex) {
        log.error("Exception thrown while querying '{}'", address, ex);
      }
    }
    outputValue.put(this.config.fieldOutput, geoIpStruct);

    return record.newRecord(
        record.topic(),
        record.kafkaPartition(),
        record.keySchema(),
        record.key(),
        outputSchema,
        outputValue,
        record.timestamp()
    );
  }

  @Override
  public ConfigDef config() {
    return MaxMindTransformationConfig.config();
  }

  @Override
  public void close() {
    try {
      reader.close();
    } catch (IOException e) {
      log.warn("Exception thrown while closing reader.", e);
    }
  }

  @Override
  public void configure(Map<String, ?> map) {
    this.config = new MaxMindTransformationConfig(map);

    DatabaseReader.Builder builder = new DatabaseReader.Builder(this.config.databasePath)
        .fileMode(this.config.databaseFileMode);

    try {
      this.reader = builder.build();
    } catch (IOException ex) {
      throw new ConnectException("Exception thrown while opening database.", ex);
    }
  }
}
