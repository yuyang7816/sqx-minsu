package com.sqx.common.pay.constants;

import java.util.List;
import java.util.Map;

/**
 * @author chengming
 * @since 2026/2/11
 */
public interface PayConstant {

    // 微信
    interface Wx {
        String MCH_ID = "sys_wx_mch_id";
        String MCH_KEY = "sys_wx_mch_key";
        String KEY_PATH = "sys_wx_key_path";

        // 用户端
        interface User {
            // app appid
            String APP_ID = "sys_user_wx_app_id";

            // 公众号appid
            String MP_APP_ID = "sys_user_mp_app_id";

            // 小程序appid
            String MA_APP_ID = "sys_user_ma_app_id";
        }

        // 其他端
    }
}
