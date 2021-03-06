package com.hibegin.http.server.test;

import com.hibegin.http.server.WebServerBuilder;
import com.hibegin.http.server.config.ServerConfig;
import com.hibegin.http.server.web.Controller;

/**
 * 加载 resource 内的静态资源，http://localhost:6058/static/index.html
 */
public class StaticMapperController extends Controller {

    public static void main(String[] args) {
        ServerConfig serverConfig = new ServerConfig();
        //serverConfig.getRouter().addMapper("/", StaticMapperController.class);
        serverConfig.addStaticResourceMapper("/static", "/static");
        new WebServerBuilder.Builder().serverConfig(serverConfig).build().startWithThread();
    }
}
