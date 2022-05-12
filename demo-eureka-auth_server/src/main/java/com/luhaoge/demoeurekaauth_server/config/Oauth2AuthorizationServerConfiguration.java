package com.luhaoge.demoeurekaauth_server.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Configuration
@Component
public class Oauth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    //用于身份验证的接口
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DruidDataSource druidDataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //super.configure(security);
        /**
         * access token认证 只有ROLE_TRUSTED_CLIENT权限的客户端才可以通过认证
         * 所以要将表oauth_client_details中客户端记录的authorities字段设置为ROLE_TRUSTED_CLIENT
         * 才能是客户端满足条件
         */
        System.out.println("security");
        //安全认证
        security.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //super.configure(clients);
        /**
         * 数据库管理客户端应用，从DataSource中配置的数据源中获取客户端数据
         * 客户端数据源都保存在oauth_client_details中
         */
        System.out.println("clients");
        clients.withClientDetails(new JdbcClientDetailsService(druidDataSource));
    }

    //配置认证服务器的安全性，一切通过数据库管理

    @Override//生成token
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //用户信息查询服务
        endpoints.userDetailsService(userDetailsService);
        //数据库管理access token和refresh token
        TokenStore tokenStore=new JdbcTokenStore(druidDataSource);
        DefaultTokenServices tokenServices=new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(new JdbcClientDetailsService(druidDataSource));
        tokenServices.setAccessTokenValiditySeconds(38000);
        endpoints.tokenServices(tokenServices);
        //数据库管理授权码
        endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(druidDataSource));
        //数据库管理授权信息
        ApprovalStore approvalStore=new JdbcApprovalStore(druidDataSource);
        endpoints.approvalStore(approvalStore);
    }
}
