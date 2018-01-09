package com.example.administrator.pospatrol.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class NetUtils {
    /**
     *
     * @param url请求的url
     * @return 服务器端响应的数据
     */
    public static byte[] get(String url) throws IOException {
        if (url == null) {
            return null;
        }

        url = url.trim();
        if (!url.toLowerCase().startsWith("http")) {
            throw new IllegalArgumentException("不是一个HTTP请求!");
        }

        URL destUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) destUrl.openConnection();
        conn.setRequestMethod("GET"); // 请求方法
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"); // 添加请求头
        conn.setReadTimeout(10000); // 超时10秒
        conn.connect(); // 发送请求

        // 读响应
        int code = conn.getResponseCode(); // 获取响应码
        if (code != 200) { // 响应不正确
            throw new IllegalStateException("HTTP响应出错，响应码为:" + code);
        }

        InputStream in = conn.getInputStream(); // 用于读取响应数据
        return readStream(in);
    }

    public static void download(String url,File f) throws IOException {
        if (url == null) {
            return;
        }

        url = url.trim();
        if (!url.toLowerCase().startsWith("http")) {
            throw new IllegalArgumentException("不是一个HTTP请求!");
        }

        URL destUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) destUrl.openConnection();
        conn.setRequestMethod("GET"); // 请求方法
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"); // 添加请求头
        conn.setReadTimeout(10000); // 超时10秒
        conn.connect(); // 发送请求

        // 读响应
        int code = conn.getResponseCode(); // 获取响应码
        if (code != 200) { // 响应不正确
            throw new IllegalStateException("HTTP响应出错，响应码为:" + code);
        }

        InputStream in = conn.getInputStream(); // 用于读取响应数据
        OutputStream out=new FileOutputStream(f);
        int len=-1;
        byte[] bytes=new byte[1024*100];
        while((len=in.read(bytes))!=-1){
            out.write(bytes, 0, len);
        }

        out.flush();

        in.close();
        out.close();

    }

    /**
     * 从流中读取数据
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            byte[] data = out.toByteArray();
            out.close();
            return data;
        } finally {
            if (in != null)
                in.close();
        }
    }

    /**
     * 读取一个url的内容，自动判断字符集
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getContent(String url) throws IOException {
        if (url == null) {
            return null;
        }

        url = url.trim();
        if (!url.toLowerCase().startsWith("http")) {
            throw new IllegalArgumentException("不是一个HTTP请求!");
        }

        URL destUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) destUrl.openConnection();
        conn.setRequestMethod("GET"); // 请求方法
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"); // 添加请求头
        conn.setReadTimeout(10000); // 超时10秒
        conn.connect(); // 发送请求

        // 读响应
        int code = conn.getResponseCode(); // 获取响应码
        if (code != 200) { // 响应不正确
            throw new IllegalStateException("HTTP响应出错，响应码为:" + code);
        }

        // 判断响应的字符集
        String contentType = conn.getContentType(); // 获取Content-Type响应头的值

        String charset = "utf-8";

        if (contentType != null
                && contentType.toLowerCase().contains("charset")) {
            charset = contentType.substring(contentType.lastIndexOf("=") + 1);
        }

        // String headValue = conn.getHeaderField("content-length"); //获取指定响应头的值
        InputStream in = conn.getInputStream(); // 用于读取响应数据
        byte[] data = readStream(in);

        return new String(data, charset);
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param params
     *            已编码的请求参数 username=%AD%3C%00%61&password=1234
     * @return
     */
    public static String post(String url, String params) throws IOException {
        if (url == null) {
            return null;
        }

        url = url.trim();
        if (!url.toLowerCase().startsWith("http")) {
            throw new IllegalArgumentException("不是一个HTTP请求!");
        }

        URL destUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) destUrl.openConnection();
        conn.setRequestMethod("POST"); // 设置请求方法,必须大写
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"); // 添加请求头
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
                params == null ? "0" : String.valueOf(params.length()));
        conn.setReadTimeout(10000); // 超时10秒
        conn.setDoOutput(true); // POST请求必须允许输出
        // 把请求参数发给服务器
        OutputStream out = conn.getOutputStream();
        out.write(params.getBytes());
        out.close();
        // 读响应
        int code = conn.getResponseCode(); // 获取响应码
        if (code != 200) { // 响应不正确
            throw new IllegalStateException("HTTP响应出错，响应码为:" + code);
        }

        // 判断响应的字符集
        String contentType = conn.getContentType(); // 获取Content-Type响应头的值

        String charset = "utf-8";

        if (contentType != null
                && contentType.toLowerCase().contains("charset")) {
            charset = contentType.substring(contentType.lastIndexOf("=") + 1);
        }

        InputStream in = conn.getInputStream(); // 用于读取响应数据
        byte[] data = readStream(in);

        return new String(data, charset);
    }

    /**
     * 使用HttpClient发送get请求
     *
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String httpGet(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get); // 发送请求
        // 获取响应的状态码
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) { // 响应不正确
            throw new IllegalStateException("HTTP响应出错，响应码为:" + code);
        }

        String contentType = response.getFirstHeader("Content-Type").getValue();
        // System.out.println(contentType);
        // System.out.println(">> " + response.getEntity().getContentType());
        String charset = "utf-8";
        if (contentType != null
                && contentType.toLowerCase().contains("charset")) {
            charset = contentType.substring(contentType.lastIndexOf("=") + 1);
        }

        InputStream in = response.getEntity().getContent();
        byte[] data = readStream(in);

        return new String(data, charset);
    }

    /**
     * 使用HttpClient发送get请求
     *
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String httpPost(String url, HashMap<String, String> params,
                                  String encoding) throws IOException {
        HttpPost post = new HttpPost(url);
        HttpClient client = new DefaultHttpClient();
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        // NameValuePair param = new BasicNameValuePair("username", "张三");
        // parameters.add(param);
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> ents = params.entrySet();
            for (Map.Entry<String, String> ent : ents) {
                NameValuePair param = new BasicNameValuePair(ent.getKey(),
                        ent.getValue());
                parameters.add(param);
            }
        }

        HttpEntity entity = new UrlEncodedFormEntity(parameters, encoding); // 请求附属体
        post.setEntity(entity);
        HttpResponse response = client.execute(post); // 发送请求

        // 获取响应的状态码
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) { // 响应不正确
            throw new IllegalStateException("HTTP响应出错，响应码为:" + code);
        }

        String contentType = response.getFirstHeader("Content-Type").getValue();
        // System.out.println(contentType);
        // System.out.println(">> " + response.getEntity().getContentType());
        String charset = "utf-8";
        if (contentType != null
                && contentType.toLowerCase().contains("charset")) {
            charset = contentType.substring(contentType.lastIndexOf("=") + 1);
        }

        InputStream in = response.getEntity().getContent();
        byte[] data = readStream(in);

        return new String(data, charset);
    }

    /**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     * <FORM METHOD=POST ACTION="http://192.168.1.156:8080/jspDemo/upload" enctype="multipart/form-data">
     *   <INPUT TYPE="text" NAME="name">
     *   <INPUT TYPE="text" NAME="id">
     *   <input type="file" name="photo"/>
     *   <input type="file" name="zip"/>
     * </FORM>
     *
     * @param path
     *            上传路径
     * @param params
     *            请求参数 key为参数名,value为参数值
     * @param file
     *            上传文件
     */
    public static boolean uploadFile(String path, Map<String, String> params,
                                     FormFile[] files) throws Exception {
        final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志

        int fileDataLength = 0;
        for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\""
                    + uploadFile.getParameterName() + "\";filename=\""
                    + uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: " + uploadFile.getContentType()
                    + "\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if (uploadFile.getInStream() != null) {
                fileDataLength += uploadFile.getFile().length();
            } else {
                fileDataLength += uploadFile.getData().length;
            }
        }
        StringBuilder textEntity = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
        }
        // 计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length
                + fileDataLength + endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        // 下面完成HTTP请求头的发送
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="
                + BOUNDARY + "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: " + dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());
        // 写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        // 把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        // 把所有文件类型的实体数据发送出来
        for (FormFile uploadFile : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\""
                    + uploadFile.getParameterName() + "\";filename=\""
                    + uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: " + uploadFile.getContentType()
                    + "\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if (uploadFile.getInStream() != null) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                uploadFile.getInStream().close();
            } else {
                outStream.write(uploadFile.getData(), 0,
                        uploadFile.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        // 下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        if (reader.readLine().indexOf("200") == -1) {// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return false;
        }
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return true;
    }

    /**
     * 提交数据到服务器
     *
     * @param path
     *            上传路径
     * @param params
     *            请求参数 key为参数名,value为参数值
     * @param file
     *            上传文件
     */
    public static boolean uploadFile(String path, Map<String, String> params,
                                     FormFile file) throws Exception {
        return uploadFile(path, params, new FormFile[] { file });
    }
}
