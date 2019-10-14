$(function(){
		//点击立即绑定，弹出绑定手机模式框
		$("#showBindPhoneModal").click(function(){
			$("#bindPhoneForm")[0].reset();
			$("#bindPhoneModal").modal("show");
		})
		//点击发送验证码
		$("#sendVerifyCode").click(function(){
			var phoneNumber = $("#phoneNumber").val();
			var _but = $(this);
			//如果手机号码存在
			if(phoneNumber){
				_but.attr("disabled","disabled");//将发送验证码按钮变成不可用
				$.ajax({
					url:"/sendVerifyCode.do",
					type:"post",
					dataType:"json",
					data:{phoneNumber:phoneNumber},
					success:function(){
						var sec = 60;
						var timer = window.setInterval(function(){
							if(sec>0){
								_but.text(sec+"秒后重新发送验证码");
								sec--; 
							}else{
								_but.removeAttr("disabled");
								_but.text("重新发送验证码");
								window.clearTimeout(timer);
							}
						},1000);
					}
				})
			}
		});
	//绑定手机
	$("#bindPhone").click(function(){
		$("#bindPhoneForm").ajaxSubmit({
			type:"post",
			success:function(data){
				if(data.success){
					$.messager.confirm("提示","绑定手机成功",function(){
						window.location.reload();
					})
				}else{
					$.messager.popup(data.msg);
				}
			}
		})
	})
	//绑定邮箱
	if($("#showBindEmailModal").size()>0){
		$("#showBindEmailModal").click(function(){
			$("#bindEmailModal").modal("show");
		})
	}
	
	$("#bindEmail").click(function(){
		var email = $("#email").val();
		if(email){
			$("#bindEmailForm").ajaxSubmit({
				type:"post",
				success:function(data){
					if(data.success){
						$.messager.confirm("提示","邮箱绑定成功",function(){
							window.location.reload();
						})
					}else{
						$.messager.alert(data.msg);
					}
				}
			})
		}
	})
	
	
});

//校验手机号码格式、是否已经绑定
/*$("#bindPhoneForm").validate({
	rules:{
		"phoneNumber":{
			required:true,
			digits:true,
			rangelength:[5,5]
		}
	},
	messages:{
		"phoneNumber":{
			required:"请输入手机号码",
			digits:"手机号码必须为数字",
			rangelength:"手机号码的长度必须为{0}位"
		}
	}
})*/