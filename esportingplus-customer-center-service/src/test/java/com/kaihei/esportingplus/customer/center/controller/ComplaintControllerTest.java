package com.kaihei.esportingplus.customer.center.controller;

import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.AbstractTest;
import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.gamingteam.api.feign.RPGTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.vo.OrderVO;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * 使用 @Autowired{@link Autowired} @MockBean{@link MockBean}注解Mock Spring容器内的bean
 *
 * 尽量把对象交给Spring容器管理 通过上述两个注解实现Mock是最佳Mockito的最佳实践
 *
 * Mockito Mock对象的原理是实现他的子类、因此无法实现静态方法及final类对象的Mock
 *
 * 需要对静态方法及final类进行Mock的情况下、应该在使用 {@link PrepareForTest}注解，声明需要改变字节码的类
 *
 * PowerMock会修改其相应的字节码，从而实现对静态方法及final类对象的Mock
 *
 * @author 谢思勇
 */
@PrepareForTest({PageHelper.class})
public class ComplaintControllerTest extends AbstractTest {

    /**
     * 模拟Http请求访问接口
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private RPGOrdersServiceClient RPGOrdersServiceClient;
    @MockBean
    @Autowired
    private RPGTeamServiceClient RPGTeamServiceClient;



    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ComplaintController complaintController;

    {
        MockitoAnnotations.initMocks(this);
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    /**
     * 测试订单列表接口
     */
    @Test
    public void listComplaintTest() throws Exception {
        mockMvc.perform(post("/complaints/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                        "{\"page\":1,\"page_size\":20,\"search_type\":1,\"type\":2,\"start_date\":\"2018-01-31\",\"end_date\":\"2018-09-12\",\"status\":0}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list").isNotEmpty());
    }

    /**
     * 获取订单详情测试
     */
    @Test
    public void getComplaintDetailTest() throws Exception {
        //Mock根据Id查询到的订单
//        int oid = 1;
//        //打桩：订单
//        PowerMockito.doReturn(objectMapper
//                .readValue(this.getTestStream("complaintDetailVo.json"),
//                        ComplaintDetailVo.class)).when(compaintRepository)
//                .selectCompaintById(oid);
//
//        //打桩：订单详情
//        PowerMockito.doReturn(objectMapper
//                .readValue(this.getTestStream("compaintItem.json"), CompaintItem.class))
//                .when(compaintItemRepository).selectByCompaintId(oid);
//
//        //打桩：订单图片
//        PowerMockito.doReturn(objectMapper
//                .readValue(this.getTestStream("compaintItemPictures.json"),
//                        new TypeReference<List<CompaintItemPicture>>() {
//                        })).when(compaintItemPictureRepository)
//                .selectCompaintItemPicturesByCompaintId(oid);
        ComplaintQueryParam complaintQueryParam = objectMapper.readValue(
                "{\"page\":1,\"pageSize\":20,\"searchType\":1,\"type\":2,\"startDate\":\"2018-01-31\",\"endDate\":\"2018-09-12\",\"status\":0}",
                ComplaintQueryParam.class);

        ResponsePacket<PagingResponse<ComplaintListVo>> result = complaintController
                .listComplaint(complaintQueryParam);
        int id = result.getData().getList().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/complaints/compaint/{oid}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.oid").value(1));
    }

    @Before
    public void before() throws IOException {
        //打桩、返回默认订单
        PowerMockito.doReturn(objectMapper
                .readValue(this.getTestStream("orderVOResponsePacket.json"),
                        new TypeReference<ResponsePacket<OrderVO>>() {
                        })).when(RPGOrdersServiceClient)
                .getBySequenceId(anyString());

        //打桩：游戏结果
        PowerMockito.doReturn(objectMapper
                .readValue(this.getTestStream("gamingTeamGameResult.json"),
                        new TypeReference<ResponsePacket<TeamGameResultVO>>() {
                        })).when(RPGTeamServiceClient).getGamingTeamGameResult(anyString());
    }
}
