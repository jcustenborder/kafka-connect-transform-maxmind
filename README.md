# Introduction

In order to build this locally you will need to download a copy of the [GeoLite2 City](http://dev.maxmind.com/geoip/geoip2/geolite2/#Downloads) from MaxMind.

# Configuration

## MaxMindTransformation

This transformation is used to lookup data from a MaxMind [database](http://dev.maxmind.com/geoip/geoip2/geolite2/#Downloads) and append the data to an existing struct.

```properties
transforms=maxmindtransformation
transforms.maxmindtransformation.type=com.github.jcustenborder.kafka.connect.transform.maxmind.MaxMindTransformation

# Set these required values
transforms.maxmindtransformation.field.input=
transforms.maxmindtransformation.maxmind.database.path=
transforms.maxmindtransformation.field.output=
```

| Name                          | Description                                                                                 | Type    | Default       | Valid Values                                              | Importance |
|-------------------------------|---------------------------------------------------------------------------------------------|---------|---------------|-----------------------------------------------------------|------------|
| field.input                   | The field in the value() of the connect record to read the ip address from.                 | string  |               |                                                           | high       |
| field.output                  | The field in the value() to write the GeoIp Data to. The schema will be appended.           | string  |               |                                                           | high       |
| maxmind.database.path         | The path of the database on the local file system.                                          | string  |               |                                                           | high       |
| maxmind.enable.anonymousip    | Flag to determine if the ip should be checked if it's used for anonymous internet access.   | boolean | false         |                                                           | medium     |
| maxmind.enable.asn            | Flag to determine if the ip should be checked if ASN information should be included.        | boolean | false         |                                                           | medium     |
| maxmind.enable.city           | Flag to determine if the ip should be checked if city information should be included.       | boolean | true          |                                                           | medium     |
| maxmind.enable.connectiontype | Flag to determine if the ip should be checked if connection information should be included. | boolean | false         |                                                           | medium     |
| maxmind.enable.country        | Flag to determine if the ip should be checked if country information should be included.    | boolean | false         |                                                           | medium     |
| maxmind.enable.domain         | Flag to determine if the ip should be checked if domain information should be included.     | boolean | false         |                                                           | medium     |
| maxmind.enable.enterprise     | Flag to determine if the ip should be checked if enterprise information should be included. | boolean | false         |                                                           | medium     |
| maxmind.enable.isp            | Flag to determine if the ip should be checked if ISP information should be included.        | boolean | false         |                                                           | medium     |
| maxmind.database.file.mode    | The mode in which to open the database.                                                     | string  | MEMORY_MAPPED | ValidEnum{enum=FileMode, allowed=[MEMORY_MAPPED, MEMORY]} | low        |


# Schemas

## com.maxmind.geoip2.model.GeoIpData

This schema contains all of the data that can be retrieved from the MaxMind api.

| Name                   | Optional | Schema                                                                                              | Default Value | Documentation |
|------------------------|----------|-----------------------------------------------------------------------------------------------------|---------------|---------------|
| anonymousIpResponse    | true     | [com.maxmind.geoip2.model.AnonymousIpResponse](#com.maxmind.geoip2.model.AnonymousIpResponse)       |               |               |
| asnResponse            | true     | [com.maxmind.geoip2.model.AsnResponse](#com.maxmind.geoip2.model.AsnResponse)                       |               |               |
| cityResponse           | true     | [com.maxmind.geoip2.model.CityResponse](#com.maxmind.geoip2.model.CityResponse)                     |               |               |
| connectionTypeResponse | true     | [com.maxmind.geoip2.model.ConnectionTypeResponse](#com.maxmind.geoip2.model.ConnectionTypeResponse) |               |               |
| countryResponse        | true     | [com.maxmind.geoip2.model.CountryResponse](#com.maxmind.geoip2.model.CountryResponse)               |               |               |
| domainResponse         | true     | [com.maxmind.geoip2.model.DomainResponse](#com.maxmind.geoip2.model.DomainResponse)                 |               |               |
| enterpriseResponse     | true     | [com.maxmind.geoip2.model.EnterpriseResponse](#com.maxmind.geoip2.model.EnterpriseResponse)         |               |               |
| insightsResponse       | true     | [com.maxmind.geoip2.model.InsightsResponse](#com.maxmind.geoip2.model.InsightsResponse)             |               |               |
| ispResponse            | true     | [com.maxmind.geoip2.model.IspResponse](#com.maxmind.geoip2.model.IspResponse)                       |               |               |

## com.maxmind.geoip2.record.RepresentedCountry

| Name      | Optional | Schema                                                                                                                                                                                                                | Default Value | Documentation |
|-----------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------|
| type      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| name      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| names     | true     | Map of <[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING), [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)> |               |               |
| geoNameId | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |

## com.maxmind.geoip2.record.Continent

| Name      | Optional | Schema                                                                                                                                                                                                                | Default Value | Documentation |
|-----------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------|
| code      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| name      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| names     | true     | Map of <[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING), [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)> |               |               |
| geoNameId | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |

## com.maxmind.geoip2.record.Location

| Name              | Optional | Schema                                                                                                  | Default Value | Documentation |
|-------------------|----------|---------------------------------------------------------------------------------------------------------|---------------|---------------|
| accuracyRadius    | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)     |               |               |
| averageIncome     | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)     |               |               |
| latitude          | true     | [Float64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#FLOAT64) |               |               |
| longitude         | true     | [Float64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#FLOAT64) |               |               |
| metroCode         | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)     |               |               |
| populationDensity | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)     |               |               |
| timeZone          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |

## com.maxmind.geoip2.record.Postal

| Name       | Optional | Schema                                                                                                | Default Value | Documentation |
|------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| code       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| confidence | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               |               |

## com.maxmind.geoip2.record.City

| Name       | Optional | Schema                                                                                                                                                                                                                | Default Value | Documentation |
|------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------|
| confidence | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |
| name       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| names      | true     | Map of <[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING), [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)> |               |               |
| geoNameId  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |

## com.maxmind.geoip2.record.Country

| Name       | Optional | Schema                                                                                                                                                                                                                | Default Value | Documentation |
|------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------|
| confidence | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |
| isoCode    | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| name       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| names      | true     | Map of <[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING), [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)> |               |               |
| geoNameId  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |

## com.maxmind.geoip2.record.Subdivision

| Name       | Optional | Schema                                                                                                                                                                                                                | Default Value | Documentation |
|------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------|
| confidence | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |
| isoCode    | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| name       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                 |               |               |
| names      | true     | Map of <[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING), [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)> |               |               |
| geoNameId  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                   |               |               |

## com.maxmind.geoip2.record.Traits

| Name                         | Optional | Schema                                                                                                  | Default Value | Documentation |
|------------------------------|----------|---------------------------------------------------------------------------------------------------------|---------------|---------------|
| autonomousSystemNumber       | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)     |               |               |
| autonomousSystemOrganization | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |
| connectionType               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |
| domain                       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |
| isAnonymousProxy             | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |
| isSatelliteProvider          | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |
| isp                          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |
| organization                 | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |
| userType                     | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)   |               |               |
| isLegitimateProxy            | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |

## com.maxmind.geoip2.model.CountryResponse

| Name               | Optional | Schema                                                                                        | Default Value | Documentation |
|--------------------|----------|-----------------------------------------------------------------------------------------------|---------------|---------------|
| continent          | true     | [com.maxmind.geoip2.record.Continent](#com.maxmind.geoip2.record.Continent)                   |               |               |
| country            | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| registeredCountry  | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| representedCountry | true     | [com.maxmind.geoip2.record.RepresentedCountry](#com.maxmind.geoip2.record.RepresentedCountry) |               |               |
| traits             | true     | [com.maxmind.geoip2.record.Traits](#com.maxmind.geoip2.record.Traits)                         |               |               |

## com.maxmind.geoip2.model.DomainResponse

| Name   | Optional | Schema                                                                                                | Default Value | Documentation |
|--------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| domain | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |

## com.maxmind.geoip2.model.ConnectionTypeResponse

| Name           | Optional | Schema                                                                                                | Default Value | Documentation |
|----------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| connectionType | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |

## com.maxmind.geoip2.model.CityResponse

| Name               | Optional | Schema                                                                                        | Default Value | Documentation |
|--------------------|----------|-----------------------------------------------------------------------------------------------|---------------|---------------|
| city               | true     | [com.maxmind.geoip2.record.City](#com.maxmind.geoip2.record.City)                             |               |               |
| location           | true     | [com.maxmind.geoip2.record.Location](#com.maxmind.geoip2.record.Location)                     |               |               |
| postal             | true     | [com.maxmind.geoip2.record.Postal](#com.maxmind.geoip2.record.Postal)                         |               |               |
| subdivisions       | true     | Array of [com.maxmind.geoip2.record.Subdivision](#com.maxmind.geoip2.record.Subdivision)      |               |               |
| continent          | true     | [com.maxmind.geoip2.record.Continent](#com.maxmind.geoip2.record.Continent)                   |               |               |
| country            | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| registeredCountry  | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| representedCountry | true     | [com.maxmind.geoip2.record.RepresentedCountry](#com.maxmind.geoip2.record.RepresentedCountry) |               |               |
| traits             | true     | [com.maxmind.geoip2.record.Traits](#com.maxmind.geoip2.record.Traits)                         |               |               |

## com.maxmind.geoip2.model.AsnResponse

| Name                         | Optional | Schema                                                                                                | Default Value | Documentation |
|------------------------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| autonomousSystemNumber       | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               |               |
| autonomousSystemOrganization | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |

## com.maxmind.geoip2.model.InsightsResponse

| Name               | Optional | Schema                                                                                        | Default Value | Documentation |
|--------------------|----------|-----------------------------------------------------------------------------------------------|---------------|---------------|
| city               | true     | [com.maxmind.geoip2.record.City](#com.maxmind.geoip2.record.City)                             |               |               |
| location           | true     | [com.maxmind.geoip2.record.Location](#com.maxmind.geoip2.record.Location)                     |               |               |
| postal             | true     | [com.maxmind.geoip2.record.Postal](#com.maxmind.geoip2.record.Postal)                         |               |               |
| subdivisions       | true     | Array of [com.maxmind.geoip2.record.Subdivision](#com.maxmind.geoip2.record.Subdivision)      |               |               |
| continent          | true     | [com.maxmind.geoip2.record.Continent](#com.maxmind.geoip2.record.Continent)                   |               |               |
| country            | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| registeredCountry  | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| representedCountry | true     | [com.maxmind.geoip2.record.RepresentedCountry](#com.maxmind.geoip2.record.RepresentedCountry) |               |               |
| traits             | true     | [com.maxmind.geoip2.record.Traits](#com.maxmind.geoip2.record.Traits)                         |               |               |

## com.maxmind.geoip2.model.EnterpriseResponse

| Name               | Optional | Schema                                                                                        | Default Value | Documentation |
|--------------------|----------|-----------------------------------------------------------------------------------------------|---------------|---------------|
| city               | true     | [com.maxmind.geoip2.record.City](#com.maxmind.geoip2.record.City)                             |               |               |
| location           | true     | [com.maxmind.geoip2.record.Location](#com.maxmind.geoip2.record.Location)                     |               |               |
| postal             | true     | [com.maxmind.geoip2.record.Postal](#com.maxmind.geoip2.record.Postal)                         |               |               |
| subdivisions       | true     | Array of [com.maxmind.geoip2.record.Subdivision](#com.maxmind.geoip2.record.Subdivision)      |               |               |
| continent          | true     | [com.maxmind.geoip2.record.Continent](#com.maxmind.geoip2.record.Continent)                   |               |               |
| country            | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| registeredCountry  | true     | [com.maxmind.geoip2.record.Country](#com.maxmind.geoip2.record.Country)                       |               |               |
| representedCountry | true     | [com.maxmind.geoip2.record.RepresentedCountry](#com.maxmind.geoip2.record.RepresentedCountry) |               |               |
| traits             | true     | [com.maxmind.geoip2.record.Traits](#com.maxmind.geoip2.record.Traits)                         |               |               |

## com.maxmind.geoip2.model.AnonymousIpResponse

| Name              | Optional | Schema                                                                                                  | Default Value | Documentation |
|-------------------|----------|---------------------------------------------------------------------------------------------------------|---------------|---------------|
| isAnonymous       | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |
| isAnonymousVpn    | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |
| isHostingProvider | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |
| isPublicProxy     | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |
| isTorExitNode     | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN) |               |               |

## com.maxmind.geoip2.model.IspResponse

| Name         | Optional | Schema                                                                                                | Default Value | Documentation |
|--------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| isp          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| organization | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |


