<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>"/>
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		function login(){
			var loginAct=$.trim($("#loginAct").val())
			var loginPwd=$.trim($("#loginPwd").val())
			if(loginAct=="" || loginPwd==""){
				$("#msg").html("账户密码信息不得为空")
				return false
			}
			$.ajax({
				url:"settings/user/login.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"get",
				dataType:"json",
				success:function (data) {
					if(data.success){
						window.location.href="workbench/index.jsp"
					}else {
						$("#msg").html(data.msg)
					}
				}

			})
		}

		//初始化光标
		$(function () {

			$("#loginAct").val("")

			$("#loginAct").focus()
		})

		//当前窗口绑定回车
		//event取得我们敲的是哪个键
		$(window).keydown(function (event) {
			//如果event.keyCode==13表示敲了回车
			if(event.keyCode==13){
				$("#submitBtn").click()
			}
		})
	</script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" id="loginAct">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" id="loginPwd">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">

                    <span id="msg"></span><%--显示登录错误的信息--%>

                </div>
				<!--
						注意：按钮写在form表单中，默认的行为就是提交表单
							一定要将按钮的类型设置为button
							按钮所触发的行为应该是由我们自己手动写js代码来决定
					-->
                <button type="button" id="submitBtn" onclick="login()" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>