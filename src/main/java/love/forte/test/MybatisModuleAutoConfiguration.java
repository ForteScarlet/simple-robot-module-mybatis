package love.forte.test;

import cn.hutool.core.io.resource.ResourceUtil;
import com.forte.qqrobot.BaseConfiguration;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.depend.DependCenter;
import com.forte.qqrobot.scanner.FileScanner;
import com.forte.qqrobot.utils.AnnotationUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

/**
 *
 * @author ForteScarlet <ForteScarlet@163.com> 
 */
@Beans
public class MybatisModuleAutoConfiguration {

    @Depend
    private DependCenter dependCenter;

    @Depend
    private BaseConfiguration configuration;

    /**
     * 得到一个 {@link Configuration} 实例
     * @return {@link Configuration}
     */
    @Beans
    public Configuration getMybatisConfiguration(){
        Configuration conf = new Configuration();
        String driver="com.mysql.cj.jdbc.Driver";
        String url="jdbc:mysql://cdb-5cmcanfz.gz.tencentcdb.com:10048/oh-pixiv-yeah?serverTimezone=UTC&useUnicode=true&amp&characterEncoding=UTF-8&useSSL=false";
        String name="root";
        String password="shisenwen20000627";
        Properties variables = new Properties();
        variables.setProperty("driver", driver);
        variables.setProperty("url", url);
        variables.setProperty("name", name);
        variables.setProperty("password", password);
        conf.setVariables(variables);

       //Properties

        Environment development = new Environment.Builder("development").transactionFactory(new JdbcTransactionFactory())
                .dataSource(new PooledDataSource(driver, url, name, password)).build();

        conf.setEnvironment(development);

        // scan

        String propertyScan = configuration.getConfigProperties().getProperty("simbot.mapper.scanPackage");
        if(propertyScan != null){
            for (String proPackage : propertyScan.split(",")) {
                conf.addMappers(proPackage);
            }
        }

        return conf;
    }

    /**
     * create sql session factory
     */
    @Beans
    public SqlSessionFactory getSqlSessionFactory(Configuration conf) throws IOException {

//        Configuration conf = new Configuration();
//
//
//        conf.add
        // todo



//        URL confXml = ResourceUtil.getResource("conf.xml");
        //获得sqlSession工厂对象
//        return new SqlSessionFactoryBuilder().build(confXml.openStream());
        return new SqlSessionFactoryBuilder().build(conf);
    }


    /**
     * 扫描所有存在{@link Mapper}的类
     */
    @Beans(init = true)
    public SqlSession getSqlSessionAndRegisterMapper(SqlSessionFactory sqlSessionFactory){
//        Properties variables = sqlSessionFactory.getConfiguration().getVariables();
//        System.err.println("getVariables()");
//        variables.forEach((k, v) -> System.err.println(k + "=" + v));


        Predicate<Class<?>> classFilter = c -> AnnotationUtils.getAnnotation(c, Mapper.class) != null;
        Set<String> packages = configuration.getScannerPackage();
        FileScanner scanner = new FileScanner();
        for (String p : packages) {
            scanner.find(p, classFilter);
        }
        String propertyScan = configuration.getConfigProperties().getProperty("simbot.mapper.scanPackage");
        if(propertyScan != null){
            for (String proPackage : propertyScan.split(",")) {
                scanner.find(proPackage, classFilter);
            }
        }
        classFilter = null;

        SqlSession session = sqlSessionFactory.openSession();


        Set<Class<?>> mappers = scanner.get();
        for (Class<?> mapper : mappers) {
            dependCenter.load(session.getMapper(mapper));
        }

        return session;
    }





}
