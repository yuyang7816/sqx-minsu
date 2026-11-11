package com.sqx.app.base.shopsRoom.controller;

import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsRoom.service.IShopsRoomService;
import com.sqx.app.base.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppShopsRoomController.class)
class AppShopsRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IShopsRoomService shopsRoomService;

    @Test
    void getShopRoomInfo_copyFromRoomId_returnsPrefillData() throws Exception {
        ShopsRoom copyRoom = new ShopsRoom();
        copyRoom.setCopySourceRoomName("801");
        copyRoom.setRoomTypeId(10L);
        copyRoom.setMoney(new java.math.BigDecimal("288"));

        when(shopsRoomService.getShopRoomInfoForCopy(1L)).thenReturn(copyRoom);

        mockMvc.perform(get("/app/shopsRoom/getShopRoomInfo")
                        .param("copyFromRoomId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.copySourceRoomName").value("801"));
    }

    @Test
    void saveShopRoom_batchCreate_returnsSuccess() throws Exception {
        when(shopsRoomService.saveShopRoom(any(ShopsRoom.class)))
                .thenReturn(Result.success("成功添加3间房间"));

        mockMvc.perform(post("/app/shopsRoom/saveShopRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"shopId\":1,\"roomName\":\"豪华大床房\",\"batchCount\":3,\"roomTypeId\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("成功添加3间房间"));
    }
}
