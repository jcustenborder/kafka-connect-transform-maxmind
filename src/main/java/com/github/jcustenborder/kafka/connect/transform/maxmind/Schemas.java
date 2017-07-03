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

import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.InsightsResponse;
import com.maxmind.geoip2.model.IspResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import java.util.ArrayList;
import java.util.List;

class Schemas {
  static final Schema SCHEMA_REPRESENTED_COUNTRY;

  static {
    SCHEMA_REPRESENTED_COUNTRY = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.RepresentedCountry")
        .doc("")
        .field("type", SchemaBuilder.string().doc("").optional().build())
        .field("name", SchemaBuilder.string().doc("").optional().build())
        .field("names", SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).doc("").optional().build())
        .field("geoNameId", SchemaBuilder.int32().doc("").optional().build())
        .build();
  }

  public static Struct representedCountry(RepresentedCountry representedCountry) {
    if (null == representedCountry) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_REPRESENTED_COUNTRY);
    struct.put("name", representedCountry.getName());
    struct.put("type", representedCountry.getType());
    struct.put("names", representedCountry.getNames());
    struct.put("geoNameId", representedCountry.getGeoNameId());
    return struct;
  }

  static final Schema SCHEMA_CONTINENT;

  static {
    SCHEMA_CONTINENT = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.Continent")
        .doc("")
        .field("code", SchemaBuilder.string().doc("").optional().build())
        .field("name", SchemaBuilder.string().doc("").optional().build())
        .field("names", SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).doc("").optional().build())
        .field("geoNameId", SchemaBuilder.int32().doc("").optional().build())
        .build();
  }

  public static Struct continent(Continent continent) {
    if (null == continent) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_CONTINENT);
    struct.put("name", continent.getName());
    struct.put("code", continent.getCode());
    struct.put("names", continent.getNames());
    struct.put("geoNameId", continent.getGeoNameId());
    return struct;
  }

  static final Schema SCHEMA_LOCATION;

  static {
    SCHEMA_LOCATION = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.Location")
        .doc("")
        .field("accuracyRadius", SchemaBuilder.int32().doc("").optional().build())
        .field("averageIncome", SchemaBuilder.int32().doc("").optional().build())
        .field("latitude", SchemaBuilder.float64().doc("").optional().build())
        .field("longitude", SchemaBuilder.float64().doc("").optional().build())
        .field("metroCode", SchemaBuilder.int32().doc("").optional().build())
        .field("populationDensity", SchemaBuilder.int32().doc("").optional().build())
        .field("timeZone", SchemaBuilder.string().doc("").optional().build())
        .build();
  }

  public static Struct location(Location location) {
    if (null == location) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_LOCATION);
    struct.put("accuracyRadius", location.getAccuracyRadius());
    struct.put("averageIncome", location.getAverageIncome());
    struct.put("latitude", location.getLatitude());
    struct.put("longitude", location.getLongitude());
    struct.put("metroCode", location.getMetroCode());
    struct.put("populationDensity", location.getPopulationDensity());
    struct.put("timeZone", location.getTimeZone());
    return struct;
  }

  static final Schema SCHEMA_POSTAL;

  static {
    SCHEMA_POSTAL = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.Postal")
        .doc("")
        .field("code", SchemaBuilder.string().doc("").optional().build())
        .field("confidence", SchemaBuilder.int32().doc("").optional().build())
        .build();
  }

  public static Struct postal(Postal postal) {
    if (null == postal) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_POSTAL);
    struct.put("code", postal.getCode());
    struct.put("confidence", postal.getConfidence());
    return struct;
  }

  static final Schema SCHEMA_CITY;

  static {
    SCHEMA_CITY = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.City")
        .doc("")
        .field("confidence", SchemaBuilder.int32().doc("").optional().build())
        .field("name", SchemaBuilder.string().doc("").optional().build())
        .field("names", SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).doc("").optional().build())
        .field("geoNameId", SchemaBuilder.int32().doc("").optional().build())
        .build();
  }

  public static Struct city(City city) {
    if (null == city) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_CITY);
    struct.put("name", city.getName());
    struct.put("confidence", city.getConfidence());
    struct.put("names", city.getNames());
    struct.put("geoNameId", city.getGeoNameId());
    return struct;
  }

  static final Schema SCHEMA_COUNTRY;

  static {
    SCHEMA_COUNTRY = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.Country")
        .doc("")
        .field("confidence", SchemaBuilder.int32().doc("").optional().build())
        .field("isoCode", SchemaBuilder.string().doc("").optional().build())
        .field("name", SchemaBuilder.string().doc("").optional().build())
        .field("names", SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).doc("").optional().build())
        .field("geoNameId", SchemaBuilder.int32().doc("").optional().build())
        .build();
  }

  public static Struct country(Country country) {
    if (null == country) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_COUNTRY);
    struct.put("name", country.getName());
    struct.put("confidence", country.getConfidence());
    struct.put("isoCode", country.getIsoCode());
    struct.put("names", country.getNames());
    struct.put("geoNameId", country.getGeoNameId());
    return struct;
  }

  static final Schema SCHEMA_SUBDIVISION;

  static {
    SCHEMA_SUBDIVISION = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.Subdivision")
        .doc("")
        .field("confidence", SchemaBuilder.int32().doc("").optional().build())
        .field("isoCode", SchemaBuilder.string().doc("").optional().build())
        .field("name", SchemaBuilder.string().doc("").optional().build())
        .field("names", SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).doc("").optional().build())
        .field("geoNameId", SchemaBuilder.int32().doc("").optional().build())
        .build();
  }

  public static Struct subdivision(Subdivision subdivision) {
    if (null == subdivision) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_SUBDIVISION);
    struct.put("name", subdivision.getName());
    struct.put("confidence", subdivision.getConfidence());
    struct.put("isoCode", subdivision.getIsoCode());
    struct.put("names", subdivision.getNames());
    struct.put("geoNameId", subdivision.getGeoNameId());
    return struct;
  }

  static final Schema SCHEMA_TRAITS;

  static {
    SCHEMA_TRAITS = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.record.Traits")
        .doc("")
        .field("autonomousSystemNumber", SchemaBuilder.int32().doc("").optional().build())
        .field("autonomousSystemOrganization", SchemaBuilder.string().doc("").optional().build())
        .field("connectionType", SchemaBuilder.string().doc("").optional().build())
        .field("domain", SchemaBuilder.string().doc("").optional().build())
        .field("isAnonymousProxy", SchemaBuilder.bool().doc("").optional().build())
        .field("isSatelliteProvider", SchemaBuilder.bool().doc("").optional().build())
        .field("isp", SchemaBuilder.string().doc("").optional().build())
        .field("organization", SchemaBuilder.string().doc("").optional().build())
        .field("userType", SchemaBuilder.string().doc("").optional().build())
        .field("isLegitimateProxy", SchemaBuilder.bool().doc("").optional().build())
        .build();
  }

  public static Struct traits(Traits traits) {
    if (null == traits) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_TRAITS);
    struct.put("autonomousSystemNumber", traits.getAutonomousSystemNumber());
    struct.put("autonomousSystemOrganization", traits.getAutonomousSystemOrganization());
    struct.put("connectionType", traits.getConnectionType());
    struct.put("domain", traits.getDomain());
    struct.put("isAnonymousProxy", traits.isAnonymousProxy());
    struct.put("isSatelliteProvider", traits.isSatelliteProvider());
    struct.put("isp", traits.getIsp());
    struct.put("organization", traits.getOrganization());
    struct.put("userType", traits.getUserType());
    struct.put("isLegitimateProxy", traits.isLegitimateProxy());
    return struct;
  }

  static final Schema SCHEMA_COUNTRY_RESPONSE;

  static {
    SCHEMA_COUNTRY_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.CountryResponse")
        .doc("")
        .field("continent", SCHEMA_CONTINENT)
        .field("country", SCHEMA_COUNTRY)
        .field("registeredCountry", SCHEMA_COUNTRY)
        .field("representedCountry", SCHEMA_REPRESENTED_COUNTRY)
        .field("traits", SCHEMA_TRAITS)
        .build();
  }

  public static Struct countryResponse(CountryResponse countryResponse) {
    if (null == countryResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_COUNTRY_RESPONSE);
    struct.put("continent", continent(countryResponse.getContinent()));
    struct.put("country", country(countryResponse.getCountry()));
    struct.put("registeredCountry", country(countryResponse.getRegisteredCountry()));
    struct.put("representedCountry", representedCountry(countryResponse.getRepresentedCountry()));
    struct.put("traits", traits(countryResponse.getTraits()));
    return struct;
  }

  static final Schema SCHEMA_DOMAIN_RESPONSE;

  static {
    SCHEMA_DOMAIN_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.DomainResponse")
        .doc("")
        .field("domain", SchemaBuilder.string().doc("").optional().build())
        .build();
  }

  public static Struct domainResponse(DomainResponse domainResponse) {
    if (null == domainResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_DOMAIN_RESPONSE);
    struct.put("domain", domainResponse.getDomain());
    return struct;
  }

  static final Schema SCHEMA_CONNECTION_TYPE_RESPONSE;

  static {
    SCHEMA_CONNECTION_TYPE_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.ConnectionTypeResponse")
        .doc("")
        .field("connectionType", SchemaBuilder.string().doc("").optional().build())
        .build();
  }

  public static Struct connectionTypeResponse(ConnectionTypeResponse connectionTypeResponse) {
    if (null == connectionTypeResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_CONNECTION_TYPE_RESPONSE);
    struct.put("connectionType", connectionTypeResponse.getConnectionType());
    return struct;
  }

  static final Schema SCHEMA_CITY_RESPONSE;

  static {
    SCHEMA_CITY_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.CityResponse")
        .doc("")
        .field("city", SCHEMA_CITY)
        .field("location", SCHEMA_LOCATION)
        .field("postal", SCHEMA_POSTAL)
        .field("subdivisions", SchemaBuilder.array(SCHEMA_SUBDIVISION).doc("").optional().build())
        .field("continent", SCHEMA_CONTINENT)
        .field("country", SCHEMA_COUNTRY)
        .field("registeredCountry", SCHEMA_COUNTRY)
        .field("representedCountry", SCHEMA_REPRESENTED_COUNTRY)
        .field("traits", SCHEMA_TRAITS)
        .build();
  }

  public static Struct cityResponse(CityResponse cityResponse) {
    if (null == cityResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_CITY_RESPONSE);
    struct.put("city", city(cityResponse.getCity()));
    struct.put("location", location(cityResponse.getLocation()));
    struct.put("postal", postal(cityResponse.getPostal()));
    List<Struct> subdivisionsList = new ArrayList();
    for (Subdivision s : cityResponse.getSubdivisions()) {
      Struct subdivisionStruct = subdivision(s);
      subdivisionsList.add(subdivisionStruct);
    }
    struct.put("subdivisions", subdivisionsList);
    struct.put("continent", continent(cityResponse.getContinent()));
    struct.put("country", country(cityResponse.getCountry()));
    struct.put("registeredCountry", country(cityResponse.getRegisteredCountry()));
    struct.put("representedCountry", representedCountry(cityResponse.getRepresentedCountry()));
    struct.put("traits", traits(cityResponse.getTraits()));
    return struct;
  }

  static final Schema SCHEMA_ASN_RESPONSE;

  static {
    SCHEMA_ASN_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.AsnResponse")
        .doc("")
        .field("autonomousSystemNumber", SchemaBuilder.int32().doc("").optional().build())
        .field("autonomousSystemOrganization", SchemaBuilder.string().doc("").optional().build())
        .build();
  }

  public static Struct asnResponse(AsnResponse asnResponse) {
    if (null == asnResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_ASN_RESPONSE);
    struct.put("autonomousSystemNumber", asnResponse.getAutonomousSystemNumber());
    struct.put("autonomousSystemOrganization", asnResponse.getAutonomousSystemOrganization());
    return struct;
  }

  static final Schema SCHEMA_INSIGHTS_RESPONSE;

  static {
    SCHEMA_INSIGHTS_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.InsightsResponse")
        .doc("")
        .field("city", SCHEMA_CITY)
        .field("location", SCHEMA_LOCATION)
        .field("postal", SCHEMA_POSTAL)
        .field("subdivisions", SchemaBuilder.array(SCHEMA_SUBDIVISION).doc("").optional().build())
        .field("continent", SCHEMA_CONTINENT)
        .field("country", SCHEMA_COUNTRY)
        .field("registeredCountry", SCHEMA_COUNTRY)
        .field("representedCountry", SCHEMA_REPRESENTED_COUNTRY)
        .field("traits", SCHEMA_TRAITS)
        .build();
  }

  public static Struct insightsResponse(InsightsResponse insightsResponse) {
    if (null == insightsResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_INSIGHTS_RESPONSE);
    struct.put("city", city(insightsResponse.getCity()));
    struct.put("location", location(insightsResponse.getLocation()));
    struct.put("postal", postal(insightsResponse.getPostal()));
    List<Struct> subdivisionsList = new ArrayList();
    for (Subdivision s : insightsResponse.getSubdivisions()) {
      Struct subdivisionStruct = subdivision(s);
      subdivisionsList.add(subdivisionStruct);
    }
    struct.put("subdivisions", subdivisionsList);
    struct.put("continent", continent(insightsResponse.getContinent()));
    struct.put("country", country(insightsResponse.getCountry()));
    struct.put("registeredCountry", country(insightsResponse.getRegisteredCountry()));
    struct.put("representedCountry", representedCountry(insightsResponse.getRepresentedCountry()));
    struct.put("traits", traits(insightsResponse.getTraits()));
    return struct;
  }

  static final Schema SCHEMA_ENTERPRISE_RESPONSE;

  static {
    SCHEMA_ENTERPRISE_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.EnterpriseResponse")
        .doc("")
        .field("city", SCHEMA_CITY)
        .field("location", SCHEMA_LOCATION)
        .field("postal", SCHEMA_POSTAL)
        .field("subdivisions", SchemaBuilder.array(SCHEMA_SUBDIVISION).doc("").optional().build())
        .field("continent", SCHEMA_CONTINENT)
        .field("country", SCHEMA_COUNTRY)
        .field("registeredCountry", SCHEMA_COUNTRY)
        .field("representedCountry", SCHEMA_REPRESENTED_COUNTRY)
        .field("traits", SCHEMA_TRAITS)
        .build();
  }

  public static Struct enterpriseResponse(EnterpriseResponse enterpriseResponse) {
    if (null == enterpriseResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_ENTERPRISE_RESPONSE);
    struct.put("city", city(enterpriseResponse.getCity()));
    struct.put("location", location(enterpriseResponse.getLocation()));
    struct.put("postal", postal(enterpriseResponse.getPostal()));
    List<Struct> subdivisionsList = new ArrayList();
    for (Subdivision s : enterpriseResponse.getSubdivisions()) {
      Struct subdivisionStruct = subdivision(s);
      subdivisionsList.add(subdivisionStruct);
    }
    struct.put("subdivisions", subdivisionsList);
    struct.put("continent", continent(enterpriseResponse.getContinent()));
    struct.put("country", country(enterpriseResponse.getCountry()));
    struct.put("registeredCountry", country(enterpriseResponse.getRegisteredCountry()));
    struct.put("representedCountry", representedCountry(enterpriseResponse.getRepresentedCountry()));
    struct.put("traits", traits(enterpriseResponse.getTraits()));
    return struct;
  }

  static final Schema SCHEMA_ANONYMOUS_IP_RESPONSE;

  static {
    SCHEMA_ANONYMOUS_IP_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.AnonymousIpResponse")
        .doc("")
        .field("isAnonymous", SchemaBuilder.bool().doc("").optional().build())
        .field("isAnonymousVpn", SchemaBuilder.bool().doc("").optional().build())
        .field("isHostingProvider", SchemaBuilder.bool().doc("").optional().build())
        .field("isPublicProxy", SchemaBuilder.bool().doc("").optional().build())
        .field("isTorExitNode", SchemaBuilder.bool().doc("").optional().build())
        .build();
  }

  public static Struct anonymousIpResponse(AnonymousIpResponse anonymousIpResponse) {
    if (null == anonymousIpResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_ANONYMOUS_IP_RESPONSE);
    struct.put("isAnonymous", anonymousIpResponse.isAnonymous());
    struct.put("isAnonymousVpn", anonymousIpResponse.isAnonymousVpn());
    struct.put("isHostingProvider", anonymousIpResponse.isHostingProvider());
    struct.put("isPublicProxy", anonymousIpResponse.isPublicProxy());
    struct.put("isTorExitNode", anonymousIpResponse.isTorExitNode());
    return struct;
  }

  static final Schema SCHEMA_ISP_RESPONSE;

  static {
    SCHEMA_ISP_RESPONSE = SchemaBuilder.struct()
        .optional()
        .name("com.maxmind.geoip2.model.IspResponse")
        .doc("")
        .field("isp", SchemaBuilder.string().doc("").optional().build())
        .field("organization", SchemaBuilder.string().doc("").optional().build())
        .build();
  }

  public static Struct ispResponse(IspResponse ispResponse) {
    if (null == ispResponse) {
      return null;
    }

    Struct struct = new Struct(SCHEMA_ISP_RESPONSE);
    struct.put("isp", ispResponse.getIsp());
    struct.put("organization", ispResponse.getOrganization());
    return struct;
  }

  static final Schema SCHEMA_MAXMIND;

  static {
    SCHEMA_MAXMIND = SchemaBuilder.struct()
        .name("com.maxmind.geoip2.model.GeoIpData")
        .optional()
        .doc("This schema contains all of the data that can be retrieved from the MaxMind api.")
        .field("anonymousIpResponse", SCHEMA_ANONYMOUS_IP_RESPONSE)
        .field("asnResponse", SCHEMA_ASN_RESPONSE)
        .field("cityResponse", SCHEMA_CITY_RESPONSE)
        .field("connectionTypeResponse", SCHEMA_CONNECTION_TYPE_RESPONSE)
        .field("countryResponse", SCHEMA_COUNTRY_RESPONSE)
        .field("domainResponse", SCHEMA_DOMAIN_RESPONSE)
        .field("enterpriseResponse", SCHEMA_ENTERPRISE_RESPONSE)
        .field("insightsResponse", SCHEMA_INSIGHTS_RESPONSE)
        .field("ispResponse", SCHEMA_ISP_RESPONSE)
        .build();
  }

  static Struct struct(AnonymousIpResponse anonymousIpResponse, AsnResponse asnResponse, CityResponse cityResponse, ConnectionTypeResponse connectionTypeResponse, CountryResponse countryResponse, DomainResponse domainResponse, EnterpriseResponse enterpriseResponse, InsightsResponse insightsResponse, IspResponse ispResponse) {
    return new Struct(SCHEMA_MAXMIND)
        .put("anonymousIpResponse", anonymousIpResponse(anonymousIpResponse))
        .put("asnResponse", asnResponse(asnResponse))
        .put("cityResponse", cityResponse(cityResponse))
        .put("connectionTypeResponse", connectionTypeResponse(connectionTypeResponse))
        .put("countryResponse", countryResponse(countryResponse))
        .put("domainResponse", domainResponse(domainResponse))
        .put("enterpriseResponse", enterpriseResponse(enterpriseResponse))
        .put("insightsResponse", insightsResponse(insightsResponse))
        .put("ispResponse", ispResponse(ispResponse));
  }

  static final Schema[] SCHEMAS = new Schema[]{
      SCHEMA_MAXMIND,
      SCHEMA_REPRESENTED_COUNTRY,
      SCHEMA_CONTINENT,
      SCHEMA_LOCATION,
      SCHEMA_POSTAL,
      SCHEMA_CITY,
      SCHEMA_COUNTRY,
      SCHEMA_SUBDIVISION,
      SCHEMA_TRAITS,
      SCHEMA_COUNTRY_RESPONSE,
      SCHEMA_DOMAIN_RESPONSE,
      SCHEMA_CONNECTION_TYPE_RESPONSE,
      SCHEMA_CITY_RESPONSE,
      SCHEMA_ASN_RESPONSE,
      SCHEMA_INSIGHTS_RESPONSE,
      SCHEMA_ENTERPRISE_RESPONSE,
      SCHEMA_ANONYMOUS_IP_RESPONSE,
      SCHEMA_ISP_RESPONSE
  };

}
