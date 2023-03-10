package com.ehsanzhao.mvc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhaoyuan
 * @date 2023/1/13
 */
@Controller
public class FileUpAndDownController {

    @RequestMapping("/fileDown")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/img/flqd.jpeg");
        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组
        byte[] bytes = new byte[is.available()];
        //将流读到字节数组中
        is.read(bytes);
        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.jpg");
        //设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        //关闭输入流
        is.close();
        return responseEntity;
    }

    @RequestMapping("fileUpload")
    public String testUp(MultipartFile photo,HttpSession session) throws IOException {
        System.out.println(photo.getName());
        System.out.println(photo.getOriginalFilename());
        String photoPath = session.getServletContext().getRealPath("photo");
        File file = new File(photoPath);
        if(!file.exists()){
            file.mkdir();
        }
        String newFileName = photo.getOriginalFilename().substring(0,photo.getOriginalFilename().lastIndexOf("."))+"_"+System.currentTimeMillis()+"."+photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));
        photo.transferTo(new File( photoPath + File.separator + newFileName));
        return "success";
    }

}
