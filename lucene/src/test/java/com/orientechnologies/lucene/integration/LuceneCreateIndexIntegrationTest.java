package com.orientechnologies.lucene.integration;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.server.OServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class LuceneCreateIndexIntegrationTest {

  private OServer  server0;
  private OrientDB remote;

  @Before
  public void before() throws Exception {
    server0 = OServer.startFromClasspathConfig("com/orientechnologies/lucene/integration/orientdb-simple-server-config.xml");
    remote = new OrientDB("remote:localhost", "root", "test", OrientDBConfig.defaultConfig());
    remote.create("LuceneCreateIndexIntegrationTest", ODatabaseType.PLOCAL);
    ODatabaseSession session = remote.open("LuceneCreateIndexIntegrationTest", "admin", "admin");

    session.command("create class Person");
    session.command("create property Person.name STRING");
    session.command("create property Person.surname STRING");

    OElement doc = session.newElement("Person");
    doc.setProperty("name", "Jon");
    doc.setProperty("surname", "Snow");
    session.save(doc);
    session.close();
  }

  @Test
  public void testCreateIndexJavaAPI() throws IOException {

    ODatabaseSession session = remote.open("LuceneCreateIndexIntegrationTest", "admin", "admin");

    OClass person = session.getMetadata().getSchema().getClass("Person");

    person.createIndex("Person.firstName_lastName", "FULLTEXT", null, null, "LUCENE", new String[] { "name", "surname" });

    Assert.assertTrue(session.getMetadata().getSchema().getClass("Person").areIndexed("name", "surname"));
  }

  @After
  public void after() {

    remote.drop("LuceneCreateIndexIntegrationTest");

    server0.shutdown();
  }

}