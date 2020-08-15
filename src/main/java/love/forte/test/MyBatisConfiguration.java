package love.forte.test;

import com.forte.config.Conf;

/**
 *
 * 配置文件中的信息
 *
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/7/29
 */
@Conf("simbot")
public class MyBatisConfiguration {

    /**
     * mybatis的配置文件的资源路径。
     * 如果没有则使用以下的简化配置，如果有则直接优先使用此配置。
     */
    @Conf(value = "mybatis.config", comment = "mybatis的配置文件的资源路径。")
    private String config;

    /**
     * 连接信息-driver
     */
    @Conf(value = "db.driver", comment = "连接信息-driver")
    private String driver;
    /**
     * 连接路径
     */
    @Conf(value = "db.url", comment = "连接路径")
    private String url;
    /**
     * 连接用户名
     */
    @Conf(value = "db.name", comment = "连接用户名")
    private String name;
    /**
     * 连接用密码
     */
    @Conf(value = "db.password", comment = "连接用密码")
    private String password;

    /**
     * mapper的扫描路径
     */
    @Conf(value = "mapper.scanPackage", comment = "mapper的扫描路径")
    private String[] mapperScan = new String[0];

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getMapperScan() {
        return mapperScan;
    }

    public void setMapperScan(String[] mapperScan) {
        this.mapperScan = mapperScan;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
