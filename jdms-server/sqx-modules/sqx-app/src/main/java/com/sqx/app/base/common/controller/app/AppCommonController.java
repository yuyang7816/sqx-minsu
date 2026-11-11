package com.sqx.app.base.common.controller.app;

import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@Tag(name = "通用配置管理---用户端")
@RequestMapping(value = "/app/common")
public class AppCommonController {
    @Autowired
    private CommonInfoService commonService;

    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @Operation(summary = "用户端根据type获取对象")
    @ResponseBody
    public Result getCommonList(@Parameter(description = "类型") @PathVariable Integer type) {
        return commonService.findByType(type);
    }

    @GetMapping("/proxyImage")
    @Operation(summary = "代理读取远程图片，用于H5 canvas跨域绘制")
    public void proxyImage(@RequestParam String url, HttpServletResponse response) {
        if (StringUtils.isBlank(url) || (!url.startsWith("http://") && !url.startsWith("https://"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        HttpURLConnection conn = null;
        try {
            URL imageUrl = new URL(url);
            String host = imageUrl.getHost();
            if (StringUtils.isBlank(host) || isBlockedHost(host)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                response.setStatus(statusCode);
                return;
            }
            String contentType = conn.getContentType();
            if (StringUtils.isBlank(contentType) || !contentType.toLowerCase().startsWith("image/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            response.setContentType(contentType.split(";")[0]);
            response.setHeader("Access-Control-Allow-Origin", "*");
            try (InputStream inputStream = conn.getInputStream();
                 OutputStream outputStream = response.getOutputStream()) {
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private boolean isBlockedHost(String host) {
        String lowerHost = host.toLowerCase();
        if ("localhost".equals(lowerHost) || "127.0.0.1".equals(lowerHost)) {
            return true;
        }
        if (lowerHost.startsWith("192.168.") || lowerHost.startsWith("10.")) {
            return true;
        }
        if (lowerHost.startsWith("172.")) {
            String[] parts = lowerHost.split("\\.");
            if (parts.length >= 2) {
                try {
                    int second = Integer.parseInt(parts[1]);
                    return second >= 16 && second <= 31;
                } catch (NumberFormatException ignored) {
                    return false;
                }
            }
        }
        return false;
    }



}