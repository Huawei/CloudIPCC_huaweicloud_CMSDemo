<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="page/openjs/jquery-1.6.2.js"></script>
<script type="text/javascript" src="page/openjs/json.js"></script>
<script type="text/javascript" src="page/index.js"></script>
<script type="text/javascript" src="page/i18n/i18n.js"></script>
<script type="text/javascript" src="page/i18n/demo.lang.string.js"></script>
<script language="javascript">  
        function getLangObj()
        {
        	 var browerLanguage =(navigator.language || navigator.browserLanguage).toLowerCase();
             var LanguagePage = new LanguagePageClass();
             var global_language;
             if (browerLanguage.indexOf('zh') >= 0)
             {
                 global_language = LanguagePage.GetLanguagePage(LANGUAGE_SUPPORT.LANGUAGE_SUPPORT_CHINESE);
             }
             else
             {
                 global_language = LanguagePage.GetLanguagePage(LANGUAGE_SUPPORT.LANGUAGE_SUPPORT_ENGLISH);
             }
             var langObj = global_language;
        }
	
        window.onload=function()
        { 
            var browerLanguage =(navigator.language || navigator.browserLanguage).toLowerCase();
            var LanguagePage = new LanguagePageClass();
            var I18N = new I18NClass();
            if (browerLanguage.indexOf('zh') >= 0)
            {
                global_language = LanguagePage.GetLanguagePage(LANGUAGE_SUPPORT.LANGUAGE_SUPPORT_CHINESE);
                I18N.SwitchI18N(global_language);
            }
            else
            {
                global_language = LanguagePage.GetLanguagePage(LANGUAGE_SUPPORT.LANGUAGE_SUPPORT_ENGLISH);
                I18N.SwitchI18N(global_language);
            }
        }  
    </script>
<title>my page</title>
</head>
<body>
<div>
		<table  width="100%" border="0" cellpadding="0" cellspacing="3">
			<tr>
                <td>
                    <span self="I18N_DEMO_AGENT_ID">AgentID:</span>
                    <input type="text" id="workNo" maxlength="5"/>
                </td>
                <td>
                    <span self="I18N_DEMO_AGENT_PWD">AgentPwd:</span>
                    <input type="password" id="password" maxlength="10"/>
                </td>
                <td>
                    <input type="button" value="Login"  id="Login" self="I18N_DEMO_AGENT_LOGIN" onclick="LogIn()"/>
                </td>
            </tr>
		</table>
		
		<hr/>
		
		<table  width="100%" border="0" cellpadding="0" cellspacing="3">
            <tr>
            	<td>
            	    <span self="I18N_DEMO_REQUEST_METHOD">Request Method:</span>
            		<select id="Request_Method">
            			<option value="0">Get</option>
            			<option value="1">Post</option>
            			
            		</select>
            	</td>
            	<td>
            		/cmsgateway/resource/
            		<input type="text" id="URL" maxlength="80" style="width:400px; "/>
            	  </td>
            	 <td>
            		<input type="button" value="Send"  id="Send" self="I18N_DEMO_SEND" onclick="Send()"/>
            	 </td>
            	
            </tr>
		</table>
		
		<hr/>
		
	</div>
	
	<div>
        <p self="I18N_DEMO_REQUEST_BODY">Request Body</p>
        <textarea id="RequestInfo"  rows="20" style="width:100%" ></textarea>
    </div>
    <div>
        <p self="I18N_DEMO_RESPONSE_BODY">Response Body</p>
        <textarea id="ResponseInfo" readonly="readonly" rows="20" style="width:100%" ></textarea>
    </div>
    
</body>
</html>