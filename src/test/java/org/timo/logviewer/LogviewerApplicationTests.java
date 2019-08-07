package org.timo.logviewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.timo.logviewer.controller.LogFile;
import org.timo.logviewer.controller.LogFileRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogviewerApplicationTests {

  @Autowired LogFileRepository repo;

  @BeforeClass
  public static void setupClass() {
    System.setProperty(
        "logviewer.path",
        LogviewerApplicationTests.class.getClassLoader().getResource("test-logs").getPath());
  }

  @Test
  public void contextLoads() {
    List<LogFile> list = repo.findAll();
    assertEquals(12, list.size());
    repo.findAll().forEach(f -> assertTrue(repo.getById(f.getId()).isPresent()));
  }
}
