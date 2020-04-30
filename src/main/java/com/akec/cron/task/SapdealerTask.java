package com.akec.cron.task;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.UUID;

public class SapdealerTask implements Runnable {
    Log log = Log.getLog(SapdealerTask.class);

    @Override
    public void run() {
        List<Record> ps= Db.use("s").find("select * from v_sapdealer where CONVERT(VARCHAR(10),updatedate,120)=CONVERT(VARCHAR(10),GETDATE(),120)");
        for (Record r:ps
        ) {
            log.info("SapdealerTask"+r.getStr("original_code"));
            Record tp=  Db.use("t").findFirst("select id from t_sapdealer where original_code=?",r.getStr("original_code"));
            if(tp!=null){

            }
            else {
                try {
                    String id = UUID.randomUUID().toString().replaceAll("-", "");

                    Db.use("t").update("INSERT INTO [dbo].[t_sapdealer]([original_code], [business_region], [business_province], [market], [finance_region], [finance_province]," +
                                    " [name], [code], [quality_validtiy], [status], [business_statics_code], [business_statics_name]) VALUES " +
                                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?',?)",
                            r.getStr("original_code"), r.getStr("business_region"), r.getStr("business_province"), r.getStr("market"), r.getStr("finance_province")
                            , r.getStr("name"), r.getStr("code")
                            , r.getStr("quality_validtiy"), r.getStr("status"), r.getStr("business_statics_code"), r.getStr("business_statics_name")

                    );
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("SapdealerTask"+r.getStr("original_code")+""+e.getMessage());
                }
            }

        }
    }
}
