package com.kaihei.esportingplus;

import com.kaihei.esportingplus.customer.center.CustomerCenterServiceApplication;
import java.io.InputStream;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
@SpringBootTest(classes = CustomerCenterServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class AbstractTest {

    /**
     * 获取test resources
     */
    protected InputStream getTestStream(String path) {
        String simpleName = this.getClass().getSimpleName();
        path = "/mock/" + simpleName.replaceAll("ControllerTest", "").toLowerCase() + "/" + path;
        path = path.replaceAll("/+", "/");
        return this.getClass().getResourceAsStream(path);
    }
}