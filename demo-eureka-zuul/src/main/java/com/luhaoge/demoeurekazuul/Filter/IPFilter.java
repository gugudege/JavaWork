package com.luhaoge.demoeurekazuul.Filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class IPFilter extends ZuulFilter {
    private String[] whitelist;
    @Value("${lhgfilter.ip.whitelist}")
    private String strIPWhitelist;
    @Value("${lhgfilter.ip.whitelistenabled}")
    private String WhitelistEnabled;

    /**
     * 获取IP地址
     * @return
     * @throws ZuulException
     */
    public String getIPAddr(HttpServletRequest httpServletRequest){
        String ip=httpServletRequest.getHeader("X-Forwarded-For");
        if (ip==null || ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (ip==null || ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip==null || ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (ip==null || ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=httpServletRequest.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println(strIPWhitelist);
        whitelist=strIPWhitelist.split("\\,");

        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest=requestContext.getRequest();
        String ipAddr=this.getIPAddr(httpServletRequest);
        System.out.println("请求IP地址为:["+ipAddr+"]");//配置本地IP白民丹
        List<String> ips=new ArrayList<>();
        for (int i=0;i<whitelist.length;i++){
            System.out.println(whitelist[i]);//输出abc
            ips.add(whitelist[i]);
        }
        System.out.println("whitelist:"+ips.toString());//配置IP白名单
        if (!ips.contains(ipAddr)){
            System.out.println("未通过ip地址教研.["+ipAddr+"]");
            requestContext.setResponseStatusCode(401);
            requestContext.setSendZuulResponse(false);
            requestContext.getResponse().setContentType("application/json;charset=UTF-8");
            requestContext.setResponseBody("{\"errrocode\":\"00001\",\"errmsg\":\"IpAddr is forbidden!["+ipAddr+"]\"}");
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        if ("true".equalsIgnoreCase(WhitelistEnabled))
        return true;
        else {
            return false;
        }
    }


}
