package com.example.oda.rest;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

public class LocalRest {
    public static void main(String[] args) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8888";
            String response = restTemplate.getForObject(url, String.class);
        //} catch (ConnectException e) {
        //    System.out.println("Connect Exception");
        } catch (ResourceAccessException e) {
            System.out.println("Resource Access Exception");
            e.printStackTrace();
        }

    }


    /*
      总结： 在Pod全挂但DNS正常的EKS生产故障中，您将稳定捕获到包装了ConnectException的ResourceAccessException。

      HttpClientErrorException 属于HTTP协议层的异常，它必须满足以下所有条件才能抛出：
        DNS解析成功（域名 → IP）
        TCP连接成功建立（三次握手完成）
        TLS/SSL握手成功（如果是HTTPS）
        HTTP请求成功发送到服务器
        服务器成功接收并处理请求
        服务器返回4xx或5xx状态码

        RestTemplate GET请求
            ↓
        DNS解析 (Route 53) → 成功
            ↓
        TCP连接 (Pod端口) → 失败 ❌ (Connection refused)
            ↓
        【抛出 ResourceAccessException】
            ↓
            (永远不会到达这里)
            ↓
        HTTP请求发送 → (无法执行)
            ↓
        服务器响应4xx/5xx → (无法执行)
            ↓
        【永远不会抛出 HttpClientErrorException】
     */
}
