package org.examples.datasource;

import com.google.common.base.Preconditions;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class DatasourceBuilder {

    private String url;
    private String user;
    private String password;

    public static DatasourceBuilder as() {
        return new DatasourceBuilder();
    }

    public DatasourceBuilder url(String url){
        this.url = url;
        return this;
    }

    public DatasourceBuilder password(String password){
        this.password = password;
        return this;
    }


    public DatasourceBuilder user(String user){
        this.user = user;
        return this;
    }

    public DataSource build(){

        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(password);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }
}
