package love.forte.test;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.forte.config.ConfigurationHelper;
import com.forte.config.InjectableConfig;
import com.forte.qqrobot.BaseConfiguration;
import com.forte.qqrobot.ConfigProperties;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.depend.DependCenter;
import com.forte.qqrobot.scanner.FileScanner;
import com.forte.qqrobot.utils.AnnotationUtils;
import com.forte.qqrobot.utils.ResourcesUtils;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 用于向core中自动装配
 * @author ForteScarlet <ForteScarlet@163.com> 
 */
@SuppressWarnings("unused")
@Beans
public class MybatisModuleAutoConfiguration {

    @Depend
    private DependCenter dependCenter;

    @Depend
    private BaseConfiguration configuration;

    @Depend
    private ConfigProperties configProperties;

    private static InjectableConfig<MyBatisConfiguration> myBatisConfigurationInjectableConfig = ConfigurationHelper.toInjectable(MyBatisConfiguration.class);

    /**
     * 获取配置文件中的信息
     * @return
     */
    @Beans
    public MyBatisConfiguration getModuleMyBatisConfiguration(){
        final MyBatisConfiguration conf = new MyBatisConfiguration();
        myBatisConfigurationInjectableConfig.inject(conf, configProperties);
        return conf;
    }


    /**
     * 得到一个 {@link Configuration} 实例
     * @return {@link Configuration}
     */
    @Beans
    public Configuration getMybatisConfiguration(MyBatisConfiguration myBatisConfiguration){
        Configuration conf = new Configuration();
        if(myBatisConfiguration.getConfig() != null){
            return conf;
        }
        String driver = myBatisConfiguration.getDriver();
        String url = myBatisConfiguration.getUrl();
        String name = myBatisConfiguration.getName();
        String password = myBatisConfiguration.getPassword();
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
        final String[] mapperScan = myBatisConfiguration.getMapperScan();
        for (String scan : mapperScan) {
            conf.addMappers(scan);
        }

        return conf;
    }

    /**
     * 得到一个{@link SqlSessionFactoryBuilder}实例
     * @return {@link SqlSessionFactoryBuilder}实例
     */
    @Beans
    public SqlSessionFactoryBuilder getSqlSessionFactoryBuilder(){
        return new SqlSessionFactoryBuilder();
    }

    /**
     * create sql session factory
     */
    @Beans
    public SqlSessionFactory getSqlSessionFactory(SqlSessionFactoryBuilder builder, Configuration config, MyBatisConfiguration myBatisConfiguration) throws IOException {
        final String configXmlPath = myBatisConfiguration.getConfig();
        if(configXmlPath != null){
            final Resource resource = ResourcesUtils.getResource(configXmlPath, null);
            return builder.build(resource.getReader(StandardCharsets.UTF_8));
        }else{
            return builder.build(config);
        }
    }


    /**
     * 获取{@link SqlSession}实例，
     * 且扫描所有存在{@link Mapper}的类并将这些mapper注入到依赖里。
     */
    @Beans(init = true)
    public SqlSession getSqlSessionAndRegisterMapper(SqlSessionFactory sqlSessionFactory){
        SqlSession session = sqlSessionFactory.openSession();
        final Collection<Class<?>> mappers = sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers();
        for (Class<?> mapper : mappers) {
            dependCenter.load(session.getMapper(mapper));
        }

        return session;
    }





}
