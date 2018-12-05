package com.kaihei.esportingplus;

import static org.junit.Assert.assertTrue;

import com.kaihei.esportingplus.resource.ResourceServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = ResourceServiceApplication.class)
@RunWith(SpringRunner.class)
public class AppTest {


    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void test() {
    }
}
