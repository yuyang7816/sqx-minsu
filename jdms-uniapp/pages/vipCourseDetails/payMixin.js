export default {
  methods: {
    pay() {
      if (this.openWay == 0) {
        this.$queue.showToast('请选择支付方式!')
        return;
      }
      if (this.openWay == 1) {
        console.log('微信')
        // #ifdef H5
        // 微信H5支付 根据订单id获取支付信息
        this.$Request.postJson("/app/wxPayRider/wxPayMpRiderCertification", this.form).then(res => {
          console.log(JSON.stringify(res), '微信支付信息')
          this.callPay(res.data);
        });
        // #endif
        // #ifdef APP-PLUS
        // 微信APP支付 根据订单id获取支付信息
        this.$Request.postJson("/app/wxPayRider/appRiderCertification", this.form).then(res => {
          console.log(JSON.stringify(res), '微信支付信息')
          this.isCheckPay(res.code, 'wxpay', JSON.stringify(res.data));
        });
        // #endif
        // #ifdef MP-WEIXIN
        let that = this
        that.$Request.postJson("/app/wxPayRider/wxPayJsApiRiderCertification", that.form).then(res => {
          if (res.code == 0) {
            uni.requestPayment({
              provider: 'wxpay',
              timeStamp: res.data.timestamp,
              nonceStr: res.data.noncestr,
              package: res.data.package,
              signType: res.data.signType,
              paySign: res.data.sign,
              success: function (res) {
                console.log(res)
                uni.hideLoading();
                uni.showToast({
                  title: '实名认证提交成功',
                  icon: 'none'
                });
                setTimeout(function () {
                  uni.navigateBack(1)
                }, 1000)
              },
              fail: function (err) {
                console.log('fail:' + JSON.stringify(err));
                uni.hideLoading();
                that.$queue.showToast('支付失败');
              }
            });
          } else {
            uni.showToast({
              title: res.msg,
              icon: 'none'
            })
          }
        });
        // #endif
      } else {
        // APP支付宝支付
        // #ifdef H5
        this.form.payType = 4
        this.$Request.postJson('/app/aliPay/aliPayRiderCertification', this.form).then(
          res => {
            if (res.code == 0) {
              const div = document.createElement('div')
              div.innerHTML = res.data //此处form就是后台返回接收到的数据
              document.body.appendChild(div)
              document.forms[0].submit()
            } else {
              uni.showToast({
                icon: 'none',
                title: '支付失败!'
              });
            }
          });
        // #endif

        // #ifdef APP
        this.form.payType = 5
        this.$Request.postJson("/app/aliPay/aliPayRiderCertification", this.form).then(res => {
          console.log(JSON.stringify(res), '支付宝支付信息')
          this.isCheckPay(res.code, 'alipay', res.data)
        });
        // #endif
      }
    },
    isCheckPay(status, name, order) {
      if (status == 0) {
        this.setPayment(name, order);
      } else {
        uni.hideLoading();
        uni.showToast({
          title: '支付信息有误',
          icon: 'none'
        });
      }
    },
    setPayment(name, order) {
      console.log(name, '*-*-*', order)
      uni.requestPayment({
        provider: name,
        orderInfo: order, //微信、支付宝订单数据
        success: function (res) {
          console.log(res)
          uni.hideLoading();
          uni.showLoading({
            title: '支付成功'
          });
          setTimeout(function () {
            uni.navigateBack()
          }, 1000);
        },
        fail: function (err) {
          console.log(err)
          uni.hideLoading();
        },
        complete() {
          uni.hideLoading();
        }
      });
    },
    callPay: function (response) {
      if (typeof WeixinJSBridge === "undefined") {
        if (document.addEventListener) {
          document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
        } else if (document.attachEvent) {
          document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
          document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
        }
      } else {
        this.onBridgeReady(response);
      }
    },
    onBridgeReady: function (response) {
      let that = this;
      // if (!response.package) {
      // 	return;
      // }
      console.log(response, 111111)
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
        "appId": response.appid, //公众号名称，由商户传入
        "timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
        "nonceStr": response.noncestr, //随机串
        "package": response.package,
        "signType": response.signType, //微信签名方式：
        "paySign": response.sign //微信签名
      },
        function (res) {
          console.log(res, '/*-/*-/*-')
          if (res.err_msg === "get_brand_wcpay_request:ok") {
            // 使用以上方式判断前端返回,微信团队郑重提示：
            //res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
            uni.showLoading({
              title: '支付成功'
            });
            setTimeout(function () {
              uni.hideLoading();
              uni.navigateBack()
            }, 1000);
          } else {
            uni.hideLoading();
          }
          WeixinJSBridge.log(response.err_msg);
        }
      );
    }
  }
}