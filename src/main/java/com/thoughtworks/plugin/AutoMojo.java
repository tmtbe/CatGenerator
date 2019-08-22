package com.thoughtworks.plugin;

import com.thoughtworks.plugin.doms.ReadProto;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author zhangjincheng
 * @goal auto
 * @phase pre-integration-test
 */
public class AutoMojo extends AbstractMojo {
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
    private ReadProto readProto;

    public void execute() throws MojoExecutionException {
        Build build = project.getBuild();
        //String autoGenpath = build.getSourceDirectory() + "/com/thoughtworks/autogen";
        //deleteDirectory(autoGenpath);
        Global.LocalPath = build.getSourceDirectory() + "/";
        String filepath = build.getSourceDirectory() + "/../resources/catgen/";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("banner.txt");
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        getLog().info(result);
        try {
            ready(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        run( "java");
        run( "fegin");
        run( "android");
        run( "oc");
        run( "js");
        run( "postman");
        run( "wx");
        run( "rabbit");
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public void deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            } //删除子目录
            else {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    private void ready(String filepath) throws Exception {
        readProto = new ReadProto();
        File file = new File(filepath);
        if (!file.isDirectory()) {  //通过isDirectory()判断当前路径是不是文件夹
            getLog().error("没有发现/resources/catgen/目录");
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "/" + filelist[i]);    //将输入路径及其子路径相连接
                if (!readfile.isDirectory()) {
                    if (filelist[i].endsWith(".xml")) {
                        readProto.addXml(readfile);
                    }
                }
            }
        }
        readProto.ready(getLog());
    }
    private void run(String environmentName) throws MojoExecutionException {
        try {
            readProto.run(getLog(), environmentName);
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("编译错误");
        }
    }
}
