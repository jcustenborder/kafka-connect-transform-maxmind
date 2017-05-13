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

import com.github.jcustenborder.kafka.connect.utils.config.ConfigUtils;
import com.github.jcustenborder.kafka.connect.utils.config.ValidEnum;
import com.maxmind.db.Reader;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.io.File;
import java.util.Map;

class MaxMindTransformationConfig extends AbstractConfig {

  public static final String FIELD_INPUT_CONFIG = "field.input";
  public static final String FIELD_OUTPUT_CONFIG = "field.output";
  public static final String MAXMIND_DATABASE_PATH_CONFIG = "maxmind.database.path";
  public static final String MAXMIND_DATABASE_FILE_MODE_CONFIG = "maxmind.database.file.mode";
  public static final String MAXMIND_ANONYMOUS_IP_CONFIG = "maxmind.enable.anonymousip";
  public static final String MAXMIND_ASN_CONFIG = "maxmind.enable.asn";
  public static final String MAXMIND_CITY_CONFIG = "maxmind.enable.city";
  public static final String MAXMIND_CONNECTION_TYPE_CONFIG = "maxmind.enable.connectiontype";
  public static final String MAXMIND_COUNTRY_CONFIG = "maxmind.enable.country";
  public static final String MAXMIND_DOMAIN_CONFIG = "maxmind.enable.domain";
  public static final String MAXMIND_ENTERPRISE_CONFIG = "maxmind.enable.enterprise";
  public static final String MAXMIND_ISP_CONFIG = "maxmind.enable.isp";

  static final String FIELD_INPUT_DOC = "The field in the value() of the connect record to read the ip address from.";
  static final String FIELD_OUTPUT_DOC = "The field in the value() to write the GeoIp Data to. The schema will be appended.";
  static final String MAXMIND_DATABASE_PATH_DOC = "The path of the database on the local file system.";
  static final String MAXMIND_DATABASE_FILE_MODE_DOC = "The mode in which to open the database.";
  static final String MAXMIND_ANONYMOUS_IP_DOC = "Flag to determine if the ip should be checked if it's used for anonymous internet access.";
  static final String MAXMIND_ASN_DOC = "Flag to determine if the ip should be checked if ASN information should be included.";
  static final String MAXMIND_CITY_DOC = "Flag to determine if the ip should be checked if city information should be included.";
  static final String MAXMIND_CONNECTIONTYPE_DOC = "Flag to determine if the ip should be checked if connection information should be included.";
  static final String MAXMIND_COUNTRY_DOC = "Flag to determine if the ip should be checked if country information should be included.";
  static final String MAXMIND_DOMAIN_DOC = "Flag to determine if the ip should be checked if domain information should be included.";
  static final String MAXMIND_ENTERPRISE_DOC = "Flag to determine if the ip should be checked if enterprise information should be included.";
  static final String MAXMIND_ISP_DOC = "Flag to determine if the ip should be checked if ISP information should be included.";

  public final File databasePath;
  public final Reader.FileMode databaseFileMode;
  public final String fieldInput;
  public final String fieldOutput;
  public final boolean maxmindAnonymousIp;
  public final boolean maxmindAsn;
  public final boolean maxmindCity;
  public final boolean maxmindConnectionType;
  public final boolean maxmindCountry;
  public final boolean maxmindDomain;
  public final boolean maxmindEnterprise;
  public final boolean maxmindIsp;


  public MaxMindTransformationConfig(Map<String, ?> parsedConfig) {
    super(config(), parsedConfig);

    this.databasePath = ConfigUtils.getAbsoluteFile(this, MAXMIND_DATABASE_PATH_CONFIG);
    this.databaseFileMode = ConfigUtils.getEnum(Reader.FileMode.class, this, MAXMIND_DATABASE_FILE_MODE_CONFIG);
    this.maxmindAnonymousIp = this.getBoolean(MAXMIND_ANONYMOUS_IP_CONFIG);
    this.maxmindAsn = this.getBoolean(MAXMIND_ASN_CONFIG);
    this.maxmindCity = this.getBoolean(MAXMIND_CITY_CONFIG);
    this.maxmindConnectionType = this.getBoolean(MAXMIND_CONNECTION_TYPE_CONFIG);
    this.maxmindCountry = this.getBoolean(MAXMIND_COUNTRY_CONFIG);
    this.maxmindDomain = this.getBoolean(MAXMIND_DOMAIN_CONFIG);
    this.maxmindEnterprise = this.getBoolean(MAXMIND_ENTERPRISE_CONFIG);
    this.maxmindIsp = this.getBoolean(MAXMIND_ISP_CONFIG);
    this.fieldInput = this.getString(FIELD_INPUT_CONFIG);
    this.fieldOutput = this.getString(FIELD_OUTPUT_CONFIG);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(MAXMIND_DATABASE_PATH_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, MAXMIND_DATABASE_PATH_DOC)
        .define(FIELD_INPUT_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, FIELD_INPUT_DOC)
        .define(FIELD_OUTPUT_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, FIELD_OUTPUT_DOC)
        .define(MAXMIND_DATABASE_FILE_MODE_CONFIG, ConfigDef.Type.STRING, Reader.FileMode.MEMORY_MAPPED.name(), ValidEnum.of(Reader.FileMode.class), ConfigDef.Importance.LOW, MAXMIND_DATABASE_FILE_MODE_DOC)
        .define(MAXMIND_ANONYMOUS_IP_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_ANONYMOUS_IP_DOC)
        .define(MAXMIND_ASN_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_ASN_DOC)
        .define(MAXMIND_CITY_CONFIG, ConfigDef.Type.BOOLEAN, true, ConfigDef.Importance.MEDIUM, MAXMIND_CITY_DOC)
        .define(MAXMIND_CONNECTION_TYPE_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_CONNECTIONTYPE_DOC)
        .define(MAXMIND_COUNTRY_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_COUNTRY_DOC)
        .define(MAXMIND_DOMAIN_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_DOMAIN_DOC)
        .define(MAXMIND_ENTERPRISE_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_ENTERPRISE_DOC)
        .define(MAXMIND_ISP_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.MEDIUM, MAXMIND_ISP_DOC);
  }

}
