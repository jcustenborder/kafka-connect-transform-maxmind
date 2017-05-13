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

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.maxmind.geoip2.model.AbstractCityResponse;
import com.maxmind.geoip2.model.AbstractCountryResponse;
import com.maxmind.geoip2.model.AbstractResponse;
import com.maxmind.geoip2.record.AbstractNamedRecord;
import com.maxmind.geoip2.record.AbstractRecord;
import com.maxmind.geoip2.record.MaxMind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SchemaBuilderTest {
  private static final Logger log = LoggerFactory.getLogger(SchemaBuilderTest.class);

  Reflections reflections;

  static final String PACKAGE = "com.maxmind.geoip2.record";

  @BeforeEach
  public void before() throws MalformedURLException {
    log.info("before() - Configuring reflections to use package '{}'", PACKAGE);
    this.reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forJavaClassPath())
        .forPackages(PACKAGE, "com.maxmind.geoip2.model")
    );
  }

  static String schemaName(Class<?> cls) {
    return "SCHEMA_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, cls.getSimpleName());
  }

  @Test
  public void test() throws IOException {

    Set<Class<?>> allClasses = new LinkedHashSet<>();
    Set<Class<? extends AbstractRecord>> recordClasses = reflections.getSubTypesOf(AbstractRecord.class);
    Set<Class<? extends AbstractResponse>> allResponseClasses = reflections.getSubTypesOf(AbstractResponse.class);

    allClasses.addAll(recordClasses);
    allClasses.addAll(allResponseClasses);

    Map<Class<?>, String> classToSchema = new LinkedHashMap<>();

    StringWriter stringWriter = new StringWriter();
    BufferedWriter writer = new BufferedWriter(stringWriter);

    List<String> schemas = new ArrayList<>();
    schemas.add("SCHEMA_MAXMIND");

    for (Class<?> cls : allClasses) {
      if (Modifier.isAbstract(cls.getModifiers())) {
        continue;
      }

      if (cls.equals(MaxMind.class)) {
        continue;
      }

      final String schemaName = schemaName(cls);
      schemas.add(schemaName);
      classToSchema.put(cls, schemaName);

      writer.write(String.format("static final Schema %s;", schemaName));
      writer.newLine();

      writer.write("static {");
      writer.newLine();


      writer.write(String.format("  %s = SchemaBuilder.struct()", schemaName));
      writer.newLine();

      writer.write("    .optional()");
      writer.newLine();

      writer.write(String.format("    .name(\"%s\")", cls.getName()));
      writer.newLine();
      writer.write("    .doc(\"\")");
      writer.newLine();

      Map<Method, String> fieldMap = new LinkedHashMap<>();

      appendFields(writer, cls, fieldMap);

      List<Class<?>> propertyClasses = Arrays.asList(
          AbstractNamedRecord.class,
          AbstractCityResponse.class,
          AbstractCountryResponse.class
      );

      if (AbstractNamedRecord.class.isAssignableFrom(cls)) {
        writer.write(String.format("    .field(\"%s\", SchemaBuilder.string().doc(\"\").optional().build())", "name"));
        writer.newLine();
      }

      for (Class<?> propertyClass : propertyClasses) {
        if (propertyClass.isAssignableFrom(cls)) {
          appendFields(writer, propertyClass, fieldMap);
        }
      }

      writer.write("    .build();");
      writer.newLine();
      writer.write("}");
      writer.newLine();
      writer.newLine();

      final String variableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, cls.getSimpleName());

      writer.write(String.format(
          "public static Struct %s(%s %s) {",
          variableName,
          cls.getSimpleName(),
          variableName
      ));
      writer.newLine();
      writer.write(String.format("  if (null == %s) {", variableName));
      writer.newLine();
      writer.write("    return null;");
      writer.newLine();
      writer.write("  }");
      writer.newLine();
      writer.newLine();

      writer.write(String.format("  Struct struct = new Struct(%s);", schemaName));
      writer.newLine();

      int index = 1;
      for (Map.Entry<Method, String> kvp : fieldMap.entrySet()) {

        if (List.class.isAssignableFrom(kvp.getKey().getReturnType())) {
          String listVariable = kvp.getValue() + "List";
          writer.write(String.format("  List<Struct> %s = new ArrayList();", listVariable));
          writer.newLine();
          writer.write(String.format("  for(Subdivision s:%s.%s()) {", variableName, kvp.getKey().getName()));
          writer.newLine();
          writer.write("    Struct subdivisionStruct = subdivision(s);");
          writer.newLine();
          writer.write(String.format("    %s.add(subdivisionStruct);", listVariable));
          writer.newLine();
          writer.write("  }");
          writer.newLine();
          writer.write(
              String.format(
                  "  struct.put(\"%s\", %s);", kvp.getValue(), listVariable)
          );

        } else if (allClasses.contains(kvp.getKey().getReturnType())) {
          String method = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, kvp.getKey().getReturnType().getSimpleName());
          writer.write(
              String.format(
                  "  struct.put(\"%s\", %s(%s.%s()));", kvp.getValue(), method, variableName, kvp.getKey().getName())
          );
        } else {
          writer.write(
              String.format(
                  "  struct.put(\"%s\", %s.%s());", kvp.getValue(), variableName, kvp.getKey().getName())
          );
        }

        writer.newLine();
        index++;
      }

      writer.write("  return struct;");
      writer.newLine();
      writer.write("}");
      writer.newLine();
      writer.newLine();

//      SchemaBuilder.struct().field()


//
//      StringBuilder builder = new StringBuilder();
//      builder.append("static final Schema ")
//
//
//
//      SchemaBuilder schemaBuilder = SchemaBuilder.struct();
//      schemaBuilder.name(cls.getName());


//      log.trace("{}", cls);
    }

    writer.write("static final Schema SCHEMA_MAXMIND;");
    writer.newLine();
    writer.newLine();
    writer.write("static {");
    writer.newLine();
    writer.write("  SCHEMA_MAXMIND = SchemaBuilder.struct()");
    writer.newLine();
    writer.write("    .name(\"com.maxmind.geoip2.model.GeoIpData\")");
    writer.newLine();
    writer.write("    .optional()");
    writer.newLine();
    writer.write("    .doc(\"This schema contains all of the data that can be retrieved from the MaxMind api.\")");
    writer.newLine();

    List<Class<?>> responseClasses = new ArrayList<>();
    for (Class<? extends AbstractResponse> responseClass : allResponseClasses) {
      if (!Modifier.isAbstract(responseClass.getModifiers())) {
        responseClasses.add(responseClass);
      }
    }
    responseClasses.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getSimpleName(), o2.getSimpleName()));

    for (Class<?> responseClass : responseClasses) {

      String schemaName = "SCHEMA_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, responseClass.getSimpleName());
      String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, responseClass.getSimpleName());
      writer.write(String.format("    .field(\"%s\", %s)", name, schemaName));
      writer.newLine();
    }

    writer.write("    .build();");
    writer.newLine();
    writer.write('}');
    writer.newLine();

    writer.write("static Struct struct(");

    int index = 1;
    for (Class<?> responseClass : responseClasses) {
      String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, responseClass.getSimpleName());
      writer.write(String.format("%s %s", responseClass.getSimpleName(), name));
      if (index < responseClasses.size()) {
        writer.write(", ");
      }
      index++;
    }

    writer.write(") {");
    writer.newLine();

    writer.write("  return new Struct(SCHEMA_MAXMIND)");
    writer.newLine();

    index = 1;
    for (Class<?> responseClass : responseClasses) {
      String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, responseClass.getSimpleName());

      writer.write(
          String.format(
              "    .put(\"%s\", %s(%s))", name, name, name)
      );

      if (index == responseClasses.size()) {
        writer.write(';');
      }

      writer.newLine();
      index++;
    }


    writer.write('}');
    writer.newLine();


    writer.newLine();
    writer.write("static final Schema[] SCHEMAS = new Schema[] {");
    writer.newLine();
    index = 1;
    for (String s : schemas) {
      writer.write("  " + s);
      if (index < schemas.size()) {
        writer.write(", ");
        writer.newLine();
      }
      index++;
    }
    writer.newLine();
    writer.write("};");
    writer.newLine();


    writer.flush();
    log.info("\n{}", stringWriter);
  }

  private void appendFields(BufferedWriter writer, Class<?> cls, Map<Method, String> fieldMap) throws IOException {

    Map<String, Method> methodMap = new LinkedHashMap<>();

    for (Method method : cls.getDeclaredMethods()) {
      methodMap.put(method.getName().replace("get", "").toLowerCase(), method);
    }

    if (AbstractNamedRecord.class.isAssignableFrom(cls)) {
      try {
        Method nameMethod = AbstractNamedRecord.class.getMethod("getName");
        fieldMap.put(nameMethod, "name");
      } catch (NoSuchMethodException e) {
        log.trace("no", e);
      }
    }


    for (Field field : cls.getDeclaredFields()) {
      if ("locales".equals(field.getName())) {
        continue;
      }
      if ("maxmind".equals(field.getName())) {
        continue;
      }
      if ("ipAddress".equals(field.getName())) {
        continue;
      }
      if (fieldMap.containsValue(field.getName())) {
        continue;
      }

      Method method = methodMap.get(field.getName().replace("get", "").toLowerCase());
      Preconditions.checkNotNull(method, "Could not find method for %s", field.getName());
      fieldMap.put(method, field.getName());

      StringBuilder builder = new StringBuilder();
      builder.append("SchemaBuilder.");

      String schema = null;

      boolean useBuilder = true;

      if (String.class.equals(field.getType())) {
        builder.append("string()");
      } else if (Integer.class.equals(field.getType())) {
        builder.append("int32()");
      } else if (Double.class.equals(field.getType())) {
        builder.append("float64()");
      } else if (boolean.class.equals(field.getType())) {
        builder.append("bool()");
      } else if (field.getType().isEnum()) {
        builder.append("string()");
      } else if (Map.class.equals(field.getType())) {
        builder.append("map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA)");
      } else if (List.class.equals(field.getType())) {
//        log.trace("List is of {}", ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
        builder.append("array(SCHEMA_SUBDIVISION)");
      } else {
        useBuilder = false;
        schema = schemaName(field.getType());
        log.trace("foo = {}", field.getType());
        Preconditions.checkState(PACKAGE.equals(field.getType().getPackage().getName()));
        builder.append("string");
      }

      builder.append(".doc(\"\").optional().build()");

      if (useBuilder) {
        writer.write(String.format("    .field(\"%s\", %s)", field.getName(), builder));
      } else {
        writer.write(String.format("    .field(\"%s\", %s)", field.getName(), schema));
      }

      writer.newLine();
    }
  }


}
