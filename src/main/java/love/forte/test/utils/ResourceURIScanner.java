/*
 * Copyright (c) 2020. ForteScarlet All rights reserved.
 * Project  simple-robot-core
 * File     FileScanner.java
 *
 * You can contact the author through the following channels:
 * github https://github.com/ForteScarlet
 * gitee  https://gitee.com/ForteScarlet
 * email  ForteScarlet@163.com
 * QQ     1149159218
 *
 */

package love.forte.test.utils;


import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarFile;


/**
 * 扫描包下所有资源文件
 *
 * @author ForteScarlet
 */
public class ResourceURIScanner {

    /**
     * 储存结果的Set集合
     */
    private Set<URI> eleStrategySet = new HashSet<>();

    /**
     * 默认使用的类加载器
     */
    private ClassLoader classLoader;

    /**
     * 构造，指定默认的类加载器为当前线程的类加载器
     */
    public ResourceURIScanner() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * 使用指定的类加载器
     */
    public ResourceURIScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    /**
     * 根据过滤规则查询
     *
     * @param classFilter class过滤规则
     */
    public ResourceURIScanner find(String path, Predicate<URI> classFilter) {
        eleStrategySet.addAll(addFile(path, classFilter));
        return this;
    }

    /**
     * 根据过滤规则查询, 查询全部
     */
    public ResourceURIScanner find(String path) {
        if (path == null || path.length() == 0) {
            path = "." + File.separator;
        }
        eleStrategySet.addAll(addFile(path, c -> true));
        return this;
    }

    /**
     * 获取包下所有
     *
     * @param filter 过滤器
     */
    private Set<URI> addFile(String path, Predicate<URI> filter) {
        URL url = classLoader.getResource(path);
        //如果路径为null，抛出异常
        if (url == null) {
            throw new RuntimeException("资源路径不存在: " + path);
        }


        //路径字符串
        String protocol = url.getProtocol();
        try {
            //如果是文件类型，使用文件扫描
            if ("file".equals(protocol)) {
                // 本地自己可见的代码
                return findLocal(path, filter);
                //如果是jar包类型，使用jar包扫描
            } else if ("jar".equals(protocol)) {
                // 引用jar包的代码
                return findJar(path, filter);
            }
        }catch (Exception e){
            throw new RuntimeException("无法扫描路径: " + path, e);
        }
        return Collections.emptySet();
    }

    /**
     * 本地查找
     */
    private Set<URI> findLocal(final String path, final Predicate<URI> filter) {
        Set<URI> set = new HashSet<>();
        URI uri;
        try {
            uri = classLoader.getResource(path).toURI();
        } catch (NullPointerException | URISyntaxException e) {
            throw new RuntimeException("未找到File资源: " + path, e);
        }
        File file = new File(uri);
        if (file.isDirectory()) {
            file.listFiles(chiFile -> {
                if (chiFile.isDirectory()) {
                    //如果是文件夹，递归扫描
                    if (path.length() == 0) {
                        set.addAll(findLocal(chiFile.getName(), filter));
                    } else {
                        set.addAll(findLocal(path + File.separator + chiFile.getName(), filter));
                    }
                } else {
                    final URI childUri = chiFile.toURI();
                    if (filter.test(childUri)) {
                        set.add(childUri);
                    }
                }
                return true;
            });
        }
        return set;
    }

    /**
     * jar包查找
     *
     * @param path
     */
    private Set<URI> findJar(final String path, final Predicate<URI> filter) {
        Set<URI> set = new HashSet<>();
        JarFile jarFile;
        try {
            URL url = classLoader.getResource(path);
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException("未找到Jar资源: " + path, e);
        }

        // 遍历
        jarFile.stream().forEach(jarEntry -> {
            String jarEntryName = jarEntry.getName();

            if (jarEntryName.contains(path) && !jarEntryName.equals(path + File.separator)) {
                //递归遍历子目录
                if (jarEntry.isDirectory()) {
                    String jarFileName = jarEntry.getName();
                    int endIndex = jarFileName.lastIndexOf(File.separator);
                    String prefix;
                    if (endIndex > 0) {
                        prefix = jarFileName.substring(0, endIndex);
                        set.addAll(findJar(prefix, filter));
                    }
                }else{
                    final URI uri = URI.create(jarEntry.getName());
                    //判断，如果符合，添加
                    if (filter.test(uri)) {
                        set.add(uri);
                    }
                }
            }
        });

        return set;
    }


    public Set<URI> get() {
        return this.eleStrategySet;
    }

}