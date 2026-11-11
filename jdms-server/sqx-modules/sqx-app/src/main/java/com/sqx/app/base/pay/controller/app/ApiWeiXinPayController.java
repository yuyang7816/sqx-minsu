package com.sqx.app.base.pay.controller.app;

import com.sqx.app.base.pay.service.WxService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author fang
 * @date 2020/2/26
 */
@RestController
@Tag(name = "微信支付---用户端")
@RequestMapping("/app/wxPay")
@Slf4j
public class ApiWeiXinPayController {

    @Autowired
    private WxService wxService;


    @Login
    @Operation(summary = "微信支付订单")
    @PostMapping("/wxPayOrder")
    public Result wxPayOrder(@RequestParam(required = false) @Parameter(description = "订单id") Long orderId,
                             @RequestParam(required = false) @Parameter(description = "分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝）")Integer classify,
                             HttpServletRequest request) throws Exception {
        return wxService.payOrder(orderId,classify, request);
    }
    @Login
    @Operation(summary = "微信购买会员")
    @PostMapping("/wxPayVip")
    public Result wxPayVip(@RequestAttribute("userId") Long userId,
                           @RequestParam(required = false) @Parameter(description = "会员id") Long vipId,
                           @RequestParam(required = false) @Parameter(description = "分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝）")Integer classify) throws Exception {
        return wxService.wxPayVip(userId,vipId,classify);
    }

    @PostMapping("/notify")
    @Operation(summary = "微信回调")
    public String wxPayNotify(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml, 1);
            log.info("成功");
            log.info(result);

            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @PostMapping("/notifyJsApi")
    @Operation(summary = "微信回调")
    public String notifyJsApi(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml, 3);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @PostMapping("/notifyMp")
    @Operation(summary = "微信回调")
    public String notifyMp(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml, 2);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

}
