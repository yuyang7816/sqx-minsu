package com.sqx.app.base.cancelAccount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.cancelAccount.db.service.ICancelAccountService;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountInfoBo;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountPageBo;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountSaveBo;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountUpdateBo;
import com.sqx.app.base.cancelAccount.domain.entity.CancelAccount;
import com.sqx.app.base.cancelAccount.domain.vo.CancelAccountVo;
import com.sqx.app.base.cancelAccount.service.CancelAccountService;
import com.sqx.app.base.user.dao.UserDao;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户注销申请 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-07 09:31:24
 */
@Service
public class CancelAccountServiceImpl implements CancelAccountService {

    @Resource
    private ICancelAccountService iCancelAccountService;
    @Resource
    private UserDao userDao;

    @Override
    public void saveInfo(CancelAccountSaveBo cancelAccountSaveBo) {
        CancelAccount cancelAccount = new CancelAccount();
        BeanUtils.copyProperties(cancelAccountSaveBo, cancelAccount);
        cancelAccount.setStatus(1);

        UserEntity userEntity = userDao.selectById(cancelAccountSaveBo.getUserId());
        cancelAccount.setUserName(userEntity.getUserName());

        iCancelAccountService.save(cancelAccount);
    }

    @Override
    public IPage<CancelAccountVo> pageInfo(CancelAccountPageBo cancelAccountPageBo) {
        Page<CancelAccount> initPage = PageUtil.initPage(cancelAccountPageBo.getPage(), cancelAccountPageBo.getLimit());

        QueryWrapper<CancelAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(cancelAccountPageBo.getStatus()), CancelAccount::getStatus, cancelAccountPageBo.getStatus())
                .orderByDesc(CancelAccount::getCreateTime);

        IPage<CancelAccount> iPage = iCancelAccountService.page(initPage, queryWrapper);
        List<CancelAccountVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (CancelAccount record : iPage.getRecords()) {
                CancelAccountVo vo = new CancelAccountVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<CancelAccountVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public CancelAccountVo info(CancelAccountInfoBo cancelAccountInfoBo) {
        CancelAccount cancelAccount = iCancelAccountService.getById(cancelAccountInfoBo.getId());
        if (Objects.isNull(cancelAccount)) {
            throw new BizException("信息不存在");
        }

        CancelAccountVo vo = new CancelAccountVo();
        BeanUtils.copyProperties(cancelAccount, vo);
        return vo;
    }

    @Override
    public void updateInfo(CancelAccountUpdateBo cancelAccountUpdateBo) {
        CancelAccount cancelAccount = new CancelAccount();
        BeanUtils.copyProperties(cancelAccountUpdateBo, cancelAccount);

        iCancelAccountService.updateById(cancelAccount);
    }

    @Override
    public void delete(CancelAccountInfoBo cancelAccountInfoBo) {
        iCancelAccountService.removeById(cancelAccountInfoBo.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(Long id) {
        CancelAccount cancelAccount = iCancelAccountService.getById(id);
        if (cancelAccount == null) {
            throw new BizException("用户申请不存在");
        }

        if (1 != cancelAccount.getStatus()) {
            throw new BizException("不能注销该用户");
        }
        cancelAccount.setStatus(2);
        iCancelAccountService.updateById(cancelAccount);
        userDao.deleteById(cancelAccount.getUserId());
    }

    @Override
    public void revoke(Long userId) {
        QueryWrapper<CancelAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CancelAccount::getUserId, userId);
        CancelAccount cancelAccount = iCancelAccountService.getOne(queryWrapper);
        if (cancelAccount == null) {
            throw new BizException("注销申请不存在");
        }

        if (1 != cancelAccount.getStatus()) {
            throw new BizException("非法操作");
        }

        cancelAccount.setStatus(3);
        iCancelAccountService.updateById(cancelAccount);
    }

    @Override
    public void adminCancel(Long userId) {
        UserEntity userEntity = userDao.selectById(userId);
        if (Objects.isNull(userEntity)) {
            throw new BizException("用户不存在");
        }

        CancelAccount cancelAccount = new CancelAccount();
        cancelAccount.setTitle("系统注销");
        cancelAccount.setContent("系统注销");
        cancelAccount.setUserId(userId);
        cancelAccount.setUserName(userEntity.getUserName());
        cancelAccount.setStatus(2);
        iCancelAccountService.save(cancelAccount);

        userDao.deleteById(cancelAccount.getUserId());
    }
}
