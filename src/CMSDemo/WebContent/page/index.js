function LogIn()
{
	var workNo = $('#workNo').val();
	var password = $('#password').val();
	if (workNo >= 1 && workNo <= 59999)
	{
		$.ajax(
		{
			url : '/CMSDemo/LogInServlet',
			type : "POST",
			data :
			{
				workNo : workNo,
				password : password
			},
			async : true,
			dataType : 'text',
			cache : false,
			success : function(list)
			{

				$("#ResponseInfo").html(list);

				if (list == null || list == "" || list == undefined)
				{
					alert("密码错误");
				} else
				{
					alert("登录成功");
				}
			},
			error : function(args)
			{
				alert("登录失败");
			}
		});
	}

	else
	{
		alert("工号非法");
	}
}

function Send()
{
	var urls = $('#URL').val();
	var reqbody = document.getElementById("RequestInfo").value;
	var reqmethod = document.getElementById("Request_Method").value;
	if (reqmethod == 0 && reqbody != "")
	{
		alert("GET方法不需要输入请求消息体");
	} else if (reqmethod == 1
			&& (reqbody == null || reqbody == "" || reqbody == undefined))
	{
		alert("POST方法需要输入请求消息体");
	} else
	{
		$.ajax(
		{
			url : '/CMSDemo/SendServlet',
			type : "POST",
			data :
			{
				urls : urls,
				reqbody : reqbody,
				reqmethod : reqmethod
			},
			async : true,
			dataType : 'text',
			cache : false,
			success : function(list1)
			{
				$("#ResponseInfo").html(list1);
			},
			error : function(args)
			{

			}
		});
	}

}