package com.akec.cron.task;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.UUID;

public class SellproductTask implements Runnable {
    Log log = Log.getLog(SellproductTask.class);

    @Override
    public void run() {
        List<Record> ps= Db.use("s").find("select * from v_sellproduct where CONVERT(VARCHAR(10),updatedate,120)=CONVERT(VARCHAR(10),GETDATE(),120)");
        for (Record r:ps
        ) {
            log.info("SellproductTask"+r.getStr("individualcode"));
            Record tp=  Db.use("t").findFirst("select individualcode from t_sellproduct where individualcode=?",r.getStr("individualcode"));
            if(tp!=null){

            }
            else{
                try{
                String id = UUID.randomUUID().toString().replaceAll("-", "");

                Db.use("t").update("INSERT INTO [dbo].[t_sellproduct]([material_desc], [bar_code], [material_specification_desc], [type_code], [series], " +
                                "[batch_code], [dealer_code], [dealer_name], [business_dealer_code], [business_dealer_name], " +
                                "[individualcode], [proudct_code], [material_code], [comments], [sale_type]) VALUES " +
                                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?', ?)",
                        r.getStr("material_desc"),r.getStr("bar_code"),r.getStr("material_specification_desc"),r.getStr("type_code"),r.getStr("seriesseries")
                        ,r.getStr("batch_code")  ,r.getStr("dealer_code")  ,r.getStr("dealer_name"),r.getStr("business_dealer_code"),r.getStr("business_dealer_name")
                        ,r.getStr("individualcode") ,r.getStr("proudct_code") ,r.getStr("material_code") ,r.getStr("comments") ,r.getStr("sale_type")

                );
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("SellproductTask"+r.getStr("individualcode")+""+e.getMessage());
                }
            }
        }
    }
}
