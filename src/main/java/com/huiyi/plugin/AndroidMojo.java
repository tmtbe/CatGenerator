package com.huiyi.plugin;

import com.huiyi.plugin.doms.ReadProto;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @goal android
 * @phase pre-integration-test
 * @author zhangjincheng
 */
public class AndroidMojo extends AbstractMojo {
    /**
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${buildinfo.prefix}"
     * default-value="---"
     */
    private String prefix;

    public void execute() throws MojoExecutionException {
        Build build = project.getBuild();
        ReadProto readProto = new ReadProto();
        Global.LocalPath = build.getSourceDirectory()+"/";
        String filepath = build.getSourceDirectory() + "/../resources/catgen/";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("banner.txt");
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        getLog().info(result);
        File file = new File(filepath);
        if (!file.isDirectory()) {  //通过isDirectory()判断当前路径是不是文件夹
            getLog().error("没有发现/resources/catgen/目录");
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "\\" + filelist[i]);    //将输入路径及其子路径相连接
                if (!readfile.isDirectory()) {
                    if (filelist[i].endsWith(".xml")) {
                        readProto.addXml(readfile);
                    }
                }
            }
        }
        try {
            readProto.run(getLog(),"android");
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("编译错误");
        }
    }
}
