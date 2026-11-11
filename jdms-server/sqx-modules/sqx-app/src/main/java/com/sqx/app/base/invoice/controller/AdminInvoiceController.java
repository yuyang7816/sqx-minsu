package com.sqx.app.base.invoice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.invoice.domain.bo.InvoiceInfoBo;
import com.sqx.app.base.invoice.domain.bo.InvoicePageBo;
import com.sqx.app.base.invoice.domain.bo.InvoiceSaveBo;
import com.sqx.app.base.invoice.domain.bo.InvoiceUpdateBo;
import com.sqx.app.base.invoice.domain.vo.InvoiceVo;
import com.sqx.app.base.invoice.service.InvoiceService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 发票 前端控制器
 *
 * @author sqx
 * @since 2025-02-21 14:13:26
 */
@Tag(name = "管理端-发票", description = "管理端-发票")
@RestController
@RequestMapping("/admin/invoice")
public class AdminInvoiceController {

    @Resource
    private InvoiceService invoiceService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestAttribute("userId") Long userId, @RequestBody InvoiceSaveBo invoiceSaveBo) {
        invoiceSaveBo.setUserId(userId);
        return invoiceService.saveInfo(invoiceSaveBo);
    }

    /**
    * 列表
    */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = InvoiceVo.class)))
    @PostMapping("page")
    public Result<IPage<InvoiceVo>> page(@RequestBody InvoicePageBo invoicePageBo) {
        return invoiceService.pageInfo(invoicePageBo);
    }

    /**
    * 信息
    */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = InvoiceVo.class)))
    @PostMapping("info")
    public Result<InvoiceVo> info(@RequestBody InvoiceInfoBo invoiceInfoBo) {
        return invoiceService.info(invoiceInfoBo);
    }

    /**
    * 更新
    */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody InvoiceUpdateBo invoiceUpdateBo) {
        return invoiceService.updateInfo(invoiceUpdateBo);
    }

    /**
    * 删除
    */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody InvoiceInfoBo invoiceInfoBo) {
        return invoiceService.delete(invoiceInfoBo);
    }

    @Operation(summary = "开票")
    @PostMapping("updateStatus")
    public Result<Void> updateStatus(@RequestBody InvoiceInfoBo invoiceInfoBo) {
        return invoiceService.updateStatus(invoiceInfoBo);
    }
}
