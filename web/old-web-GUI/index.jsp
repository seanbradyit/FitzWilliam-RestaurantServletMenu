<%-- 
    Document   : index
    Created on : 23-Jul-2022, 01:02:59
    Author     : seanb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"  
xmlns:h="http://xmlns.jcp.org/jsf/html"  
xmlns:f="http://xmlns.jcp.org/jsf/core">  
        <h:head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <title>JSP Page</title>
        </h:head>
        <h:body>

            <div id="page">

                <div id="header">
                    <ui:insert name="header" >
                      <ui:include src="/template/common/commonHeader.xhtml" />
                    </ui:insert>
                </div>

                <div id="content">
                    <ui:insert name="content" >
                      <ui:include src="/template/common/commonContent.xhtml" />
                    </ui:insert>
                </div>

                <div id="footer">
                    <ui:insert name="footer" >
                      <ui:include src="/template/common/commonFooter.xhtml" />
                    </ui:insert>
                </div>

            </div>

        </h:body>
    </html>
