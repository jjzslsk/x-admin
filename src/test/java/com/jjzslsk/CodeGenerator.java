package com.jjzslsk;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {

        String url = "jdbc:mysql://jjzslsk.gicp.net:17948/lsk";
//        String url = "jdbc:mysql://127.0.0.1:3306/lsk";

        String username = "root";
        String password = "123456";
        String moduleName = "sys";
        String mapperLocation = "D:\\lsk\\x-admin\\src\\main\\resources\\mapper\\" + moduleName;
        String tables = "x_user,x_role,x_menu,x_user_role,x_role_menu";
        String tablePrefix = "x_";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("")
//                            .enableSwagger()
//                            .fileOverride()
                            .outputDir("D:\\lsk\\x-admin\\src\\main\\java");
                })
//                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
//                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
//                    if (typeCode == Types.SMALLINT) {

//                        return DbColumnType.INTEGER;
//                    }
//                    return typeRegistry.getColumnType(metaInfo);
//
//                }))
                .packageConfig(builder -> {
                    builder.parent("com.jjzslsk")
                            .moduleName(moduleName)
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix(tablePrefix);
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
