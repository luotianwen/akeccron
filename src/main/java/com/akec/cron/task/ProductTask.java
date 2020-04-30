package com.akec.cron.task;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.UUID;

public class ProductTask implements Runnable {
    Log log = Log.getLog(ProductTask.class);
    @Override
    public void run() {
        List<Record> ps= Db.use("s").find("select *   FROM v_product where CONVERT(VARCHAR(10),updatedate,120)=CONVERT(VARCHAR(10),GETDATE(),120)");
        for (Record r:ps
             ) {
            log.info("ProductTask"+r.getStr("code"));
            Record tp=  Db.use("t").findFirst("select id from t_product where code=?",r.getStr("code"));
            if(tp!=null){

            }
            else {
                try {
                    String id = UUID.randomUUID().toString().replaceAll("-", "");

                    Record tb = Db.use("t").findFirst("select id from t_basedata where param_name=?", r.getStr("type_name"));
                    Record sb = Db.use("t").findFirst("select id from t_basedata where param_name=?", r.getStr("series_name"));

                    Db.use("t").update("INSERT INTO [dbo].[t_product]" +
                                    "([id], [code], [material_code], [material_desc], [bar_code], [material_spe_desc], " +
                                    "[base_type_id], [type_name], [base_series_id], [series_name], " +
                                    "[batch_code], [stander_saleprice], [integral], [is_record_unit]," +
                                    " [is_verify_individualcode], [note], [status]" +
                                    ") VALUES " +
                                    "(?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?)",
                            id, r.getStr("code"), r.getStr("material_code"), r.getStr("material_desc"), r.getStr("bar_code"), r.getStr("material_spe_desc")
                            , tb.getStr("id"), r.getStr("type_name"), sb.getStr("id"), r.getStr("series_name")
                            , r.getStr("batch_code"), r.getStr("stander_saleprice"), r.getStr("integral"), r.getStr("is_record_unit")
                            , r.getStr("is_verify_individualcode"), r.getStr("note"), '1'
                    );
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("ProductTask"+r.getStr("code")+""+e.getMessage());
                }
            }
        }


    }
}
