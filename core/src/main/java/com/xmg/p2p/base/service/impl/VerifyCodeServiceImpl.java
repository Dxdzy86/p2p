package com.xmg.p2p.base.service.impl;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.DateUtil;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.base.vo.VerifyCodeVO;
/**
 * 验证码相关操作(验证码是否第一次发送、发送验证码、检查验证码和手机号码)
 * 验证码、手机号、发送验证码时间都封装进VO对象中，保存在session中，这样可以随时从会话中取出
 * 进行相应的操作
 * @author Dxd
 */
@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService{
	//注入sms配置文件中的属性值
	@Value("${sms.url}")
	private String url;
	@Value("${sms.username}")
	private String username;
	@Value("${sms.password}")
	private String password;
	@Value("${sms.apikey}")
	private String apikey;
	
	//发送短信验证码
	@Override
	public void sendVerifyCode(String phoneNumber) {
		//判断是否可以发送验证码
		if(this.checkVerifyCode()) {
			try {
				VerifyCodeVO vc = new VerifyCodeVO();
				String verifyCode = UUID.randomUUID().toString().substring(0, 4);//验证码
				
				//得到访问地址的url
				URL url = new URL(this.url);
				//得到HttpURLConnection访问对象
				HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
				//设置请求方法
				httpUrlConnection.setRequestMethod("POST");
				//设置请求参数,以流的形式进行传输
				httpUrlConnection.setDoOutput(true);
				StringBuilder sb = new StringBuilder().append("username=").append(username)
						.append("&password=").append(password)
						.append("&apikey=").append(apikey)
						.append("&mobile=").append(phoneNumber)
						.append("&content=").append("您的手机验证码为：").append(verifyCode)
						.append(",请在3分钟内尽快进行验证");
				//这里调用getOutputStream()会隐式进行connect()连接
				httpUrlConnection.getOutputStream().write(sb.toString().getBytes());
				//发送http请求,inputStream会发送http请求并封装了服务器的响应结果
				InputStream inputStream = httpUrlConnection.getInputStream();
				//从输入流中以utf-8的形式读取内容
				String responseCompont = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
				if(!responseCompont.startsWith("success")) {
					throw new RuntimeException("短信发送失败");
				}else {
					vc.setVerifyCode(verifyCode);
					vc.setPhoneNumber(phoneNumber);
					vc.setLastSendVerifyCodeTime(new Date());
					UserContext.setVerifyCodeVO(vc);//将验证码VO对象添加到session中
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}else {
			throw new RuntimeException("发送验证码失败，请重新发送");
		}
	}
	//是否第一次发送验证码以及是否重复发送
	@Override
	public boolean checkVerifyCode() {
		//如果是第一次发送验证码，先尝试从session中获取验证码VO对象
		VerifyCodeVO vc = UserContext.getVerifyCodeVo();
		Date now = new Date();//创建验证码时间
		if(vc == null) {//说明是第一次发送验证码
			return true;
		}else {
			//先判断当前发送验证码的时间和上一次成功发送验证码的时间间隔是否大于某个阈值(eg:60s)
			Date lastTime = vc.getLastSendVerifyCodeTime();
			if(DateUtil.timeInterval(now, lastTime)>60) {//DateUtil工具类负责判断时间间隔操作
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断当前的手机号码是不是接受验证码的手机号码
	 * @param phoneNumber
	 * @param verifyCode
	 * @return
	 */
	public boolean checkPhoneNumberAndVerifyCode(String phoneNumber,String verifyCode) {
		//先判断session里是否有VerifyCodeVO对象
		VerifyCodeVO vc = UserContext.getVerifyCodeVo();
		if(vc != null) {
			String currentPhoneNumber = vc.getPhoneNumber();
			String currentVerifyCode = vc.getVerifyCode();
			//如果获取到验证码后，一直不提交绑定手机的表单，那么验证码就一直有效。因此我们需要设置一个最大的等待客户输入验证码的时间
			if(phoneNumber.equals(currentPhoneNumber) && verifyCode.equals(currentVerifyCode)
					&& DateUtil.timeInterval(vc.getLastSendVerifyCodeTime(), new Date())< 1000*60*3) {
				return true;
			}
		}
		return false;
	}

}
