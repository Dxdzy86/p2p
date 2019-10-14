<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>
		<#include "common/links-tpl.ftl" />
		<link type="text/css" rel="stylesheet" href="/css/account.css" />
		<script type="text/javascript" src="/js/bootstrap-3.3.2-dist/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery-2.1.3.min.js"></script>
		<script type="text/javascript" src="/js/plugins/uploadifyfive/jquery.uploadifive.min.js"></script>
		<style type="text/css">
			.uploadifyfive{
				height: 20px;
				font-size:13px;
				line-height:20px;
				text-align:center;
				position: relative;
				margin-left:auto;
				margin-right:auto;
				cursor:pointer;
				background-color: #337ab7;
			    border-color: #2e6da4;
			    color: #fff;
			}
		</style>
		<script type="text/javascript">
			$(function(){
				//上传风控资料
				$("#btn_uploadUserFiles").uploadifive({
					uploadScript:'/userFileUpload.do',
					buttonText:"上传用户风控资料文件",
					buttonClass:"uploadifyfive",
					fileType:'image/*',
					fileObjName:'file',
					multi:true,
					onQueueComplete:function(){
						window.location.reload();
					}
				});
			})
		</script>
	</head>
	<body>
		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl"/>
		<#assign currentNav="personal" />
		<#include "common/navbar-tpl.ftl" />

		<div class="container">
			<div class="row">
				<!--导航菜单-->
				<div class="col-sm-3">
					<#assign currentMenu="userFile"/>
					<#include "common/leftmenu-tpl.ftl" />
				</div>
				<!-- 功能页面 -->
				<div class="col-sm-9">
					<div class="panel panel-default">
						<div class="panel-heading">
							用户认证文件信息
						</div>
					</div>
					<div class="row">
					  <#list userFiles as userFile>
					  <div class="col-sm-6 col-md-4">
					    <div class="thumbnail">
					      <img src="${userFile.file}" />
					      <div class="caption">
					        <h4>${userFile.fileType.title}</h4>
					        <p>得分：${userFile.score} &nbsp;&nbsp;状态：${userFile.stateDisplay}</p>
					      </div>
					    </div>
					  </div>
					  </#list>
					</div>
					<div class="row">
						<a href="javascript:;" id="btn_uploadUserFiles"></a>
					</div>
				</div>
			</div>
		</div>		
		<#include "common/footer-tpl.ftl" />		
	</body>
</html>