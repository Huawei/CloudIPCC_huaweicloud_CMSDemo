//language supported  
LANGUAGE_SUPPORT = {
	LANGUAGE_SUPPORT_CHINESE:"Chinese",
	LANGUAGE_SUPPORT_ENGLISH:"English"
}

//LanguagePageClass
function LanguagePageClass()
{
	// function
	// desc: the function return the language string define page for specified language
	// params：
	// [IN] language : string of language
	// return： void
	this.GetLanguagePage = function(language){
		if (language === LANGUAGE_SUPPORT.LANGUAGE_SUPPORT_CHINESE){
			return LanguageString.Chinese;
		}
		else if (language === LANGUAGE_SUPPORT.LANGUAGE_SUPPORT_ENGLISH){
			return LanguageString.English;
		}
		else{
			return LanguageString.English;
		}
	}
}

var LanguageString = {};

LanguageString.Chinese = 
	{
		I18N_DEMO_AGENT_ID:"工号: ",
	    I18N_DEMO_AGENT_PWD:"密码: ",
	    I18N_DEMO_AGENT_LOGIN:"登录",
	    I18N_DEMO_REQUEST_METHOD:"请求方法: ",
	    I18N_DEMO_SEND:"执行",
	    I18N_DEMO_REQUEST_BODY:"请求消息",
	    I18N_DEMO_RESPONSE_BODY:"响应结果",
	}

LanguageString.English = 
{
	I18N_DEMO_AGENT_ID:"AgentID: ",
    I18N_DEMO_AGENT_PWD:"AgentPwd: ",
    I18N_DEMO_AGENT_LOGIN:"Login",
    I18N_DEMO_REQUEST_METHOD:"Request Method: ",
    I18N_DEMO_SEND:"Send",
    I18N_DEMO_REQUEST_BODY:"Request Body",
    I18N_DEMO_RESPONSE_BODY:"Response Body",
}