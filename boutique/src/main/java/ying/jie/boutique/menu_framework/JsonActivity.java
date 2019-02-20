package ying.jie.boutique.menu_framework;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ying.jie.boutique.App;
import ying.jie.boutique.BasicActivity;
import ying.jie.boutique.R;
import ying.jie.entity.GsonExpose;
import ying.jie.entity.GsonGeneric;
import ying.jie.entity.GsonTypeAdapter;
import ying.jie.entity.GsonUser;
import ying.jie.util.LogUtil;

public class JsonActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    @Override
    public int getLayoutId() {
        return R.layout.gson_aty;
    }

    GsonGeneric<List<GsonUser>> gsonGenericUserList;

    @Override
    public void initData() {
        //生成基本数据类型
        Gson gson = new Gson();
        int i = gson.fromJson("100", int.class);
        double d = gson.fromJson("99.99", double.class);
        boolean b = gson.fromJson("true", boolean.class);
        String str = gson.fromJson("Ssywbj", String.class);
        LogUtil.d(mLogTag, "i = " + i + ", d = " + d + ", b = " + b + ", str = " + str);

        //生成Json：toJson
        GsonUser gsonUser = new GsonUser(26, "Wbj", "1271829012@qq.com");
        LogUtil.d(mLogTag, "object to json-->" + gson.toJson(gsonUser));

        /*
         *解析Json：formJson
         * 字串符“name”、“age”、“EmailAddress”的要与GsonUser类中的属性字段：name、int、EmailAddress对应（包括大小写）；如果不对应，将赋值不成功。
         */
        String jsonString = "{\"name\":\"帅哥Wbj\",\"age\":24, \"EmailAddress\":\"1271829012@qq.com\"}";//赋值成功
        gsonUser = gson.fromJson(jsonString, GsonUser.class);
        LogUtil.d(mLogTag, "success, json to object-->" + gsonUser.toString());
        //赋值失败，因为“Name”和GsonUser类中的name属性不对应，“emailAddress”和GsonUser类中的EmailAddress属性不对应，所以这个属性赋值不成功，但不影响属性age成功赋值
        jsonString = "{\"Name\":\"帅哥Wbj\",\"age\":24, \"emailAddress\":\"1271829012@qq.com\"}";
        gsonUser = gson.fromJson(jsonString, GsonUser.class);
        LogUtil.d(mLogTag, "fail, json to object-->" + gsonUser.toString());

        jsonString = "{\"name\":\"帅哥Wbj\",\"age\":24, \"EmailAddress\":\"1271829012@qq.com\", \"count_time\":\"111111\"}";
        gsonUser = gson.fromJson(jsonString, GsonUser.class);
        LogUtil.d(mLogTag, "111111, json to object-->" + gsonUser.toString() + ", -->" + gson.toJson(gsonUser));
        jsonString = "{\"name\":\"帅哥Wbj\",\"age\":24, \"EmailAddress\":\"1271829012@qq.com\", \"countTime\":\"222222\"}";
        gsonUser = gson.fromJson(jsonString, GsonUser.class);
        LogUtil.d(mLogTag, "222222, json to object-->" + gsonUser.toString() + ", -->" + gson.toJson(gsonUser));
        jsonString = "{\"name\":\"帅哥Wbj\",\"age\":24, \"EmailAddress\":\"1271829012@qq.com\", \"time_count\":\"333333\"}";
        gsonUser = gson.fromJson(jsonString, GsonUser.class);
        LogUtil.d(mLogTag, "333333, json to object-->" + gsonUser.toString() + ", -->" + gson.toJson(gsonUser));
        jsonString = "{\"name\":\"帅哥Wbj\",\"age\":24, \"EmailAddress\":\"1271829012@qq.com\", \"count_time\":\"111111\", \"countTime\":\"222222\", \"time_count\":\"LastNumber\"}";
        gsonUser = gson.fromJson(jsonString, GsonUser.class);
        LogUtil.d(mLogTag, "44444, json to object-->" + gsonUser.toString() + ", -->" + gson.toJson(gsonUser));

        List<GsonUser> gsonUserListTmp = new ArrayList<>();
        gsonUserListTmp.add(gsonUser);
        gsonUserListTmp.add(new GsonUser(25, "Wbj", "1271829012@qq.com", "444444"));
        gsonUserListTmp.add(new GsonUser(26, "Wbj", "1271829012@qq.com", "555555"));
        String jsonArray = gson.toJson(gsonUserListTmp);
        LogUtil.d(mLogTag, "json array-->" + jsonArray);
        GsonUser[] gsonUsers = gson.fromJson(jsonArray, GsonUser[].class);//Json数组转换成对象数组
        for (GsonUser gsonUserTmp : gsonUsers) {
            LogUtil.d(mLogTag, "gsonUserTmp.toString()-->" + gsonUserTmp.toString());
        }
        /*
        对于List将上面的代码中的 GsonUser[].class 直接改为 List<GsonUser>.class 是行不通的。因为不管对于Java来说List<String> 和List<GsonUser>
        ，还是其它List<Object>，它们的字节码文件只一个那就是List.class，这是Java泛型使用时要注意的问题：泛型擦除。

        注：TypeToken的构造方法是protected修饰的,才会写成new TypeToken<List<String>>() {}.getType() 而不是  new TypeToken<List<String>>().getType()
         */
        List<GsonUser> gsonUserList = gson.fromJson(jsonArray, new TypeToken<List<GsonUser>>() {
        }.getType());//Json数组转换成对象数组
        for (GsonUser gsonUserTmp : gsonUserList) {
            LogUtil.d(mLogTag, "----------->" + gsonUserTmp.toString());
        }

        //Gson框架泛型的使用：start
        GsonGeneric<GsonUser> gsonGenericUser = new GsonGeneric<>(0, "success", gsonUser);
        String genericUserJson = gson.toJson(gsonGenericUser);//要解析的数据
        LogUtil.d(mLogTag, "gsonGenericUser Json-->" + genericUserJson);
        GsonGeneric<GsonUser> gsonUserObj = gson.fromJson(genericUserJson, new TypeToken<GsonGeneric<GsonUser>>() {
        }.getType());//泛型是JavaBean
        LogUtil.d(mLogTag, "gsonGenericUser Object-->" + gsonUserObj.toString() + ", gsonUserObj.data-->" + gsonUserObj.data);

        gsonGenericUserList = new GsonGeneric<>(1, "fail", gsonUserList);
        String genericListJson = gson.toJson(gsonGenericUserList);//要解析的数据
        LogUtil.d(mLogTag, "gsonGenericUser List Json-->" + genericListJson);
        GsonGeneric<List<GsonUser>> gsonUserListObj = gson.fromJson(genericListJson, new TypeToken<GsonGeneric<List<GsonUser>>>() {
        }.getType());//泛型是List<T>
        for (GsonUser userInfo : gsonUserListObj.data) {
            LogUtil.d(mLogTag, "-->userInfo.toString()-->" + userInfo.toString());
        }

        GsonGeneric<?> gsonGeneric = new GsonGeneric<>(0, "text", null);
        String gsonGenericJson = gson.toJson(gsonGeneric);//要解析的数据
        LogUtil.d(mLogTag, "gsonGenericUser ? Json-->" + gsonGenericJson);
        GsonGeneric<?> gsonGenericObj = gson.fromJson(gsonGenericJson, new TypeToken<GsonGeneric<?>>() {
        }.getType());//如果要解析的Json数据中没有“data”字段，则泛型可以为“?”
        LogUtil.d(mLogTag, "gsonGenericUser ? Obj-->" + gsonGenericObj.toString());
        //Gson框架泛型的使用：end

        StringBuilder stringBuilder = new StringBuilder();
        gson.toJson(gsonGenericUser, stringBuilder);//直接把解析后的Json串赋值到某个Appendable接口的实现类的对象中
        LogUtil.d(mLogTag, "stringBuilder.toString()-->" + stringBuilder.toString());

        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory(),
                        "SaveJson.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintStream printStream = new PrintStream(file);
                printStream.println("Auth:Wbj");
                gson.toJson(gsonGenericUser, printStream);//直接把解析后的Json串写到文件中：自动方式
                printStream.println();
                printStream.flush();
                printStream.close();

                //直接把解析后的Json串写到文件中：手动方式，写入{"name":"怪盗kidou","age":24,"email":null}
                JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file, true)));//以true表示以追加的方式写入文件
                jsonWriter.beginObject() // throws IOException
                        .name("name").value("Ssy")
                        .name("age").value(18)
                        .name("email").nullValue() //演示null
                        .endObject(); // throws IOException
                jsonWriter.flush();
                jsonWriter.close();
            }
        } catch (Exception e) {
            LogUtil.e(mLogTag, toString());
        }

        this.gsonBuilderTest();
    }

    /**
     * 使用GsonBuilder导出null值、格式化输出、日期时间
     */
    private void gsonBuilderTest() {
        Gson gson = new Gson();
        GsonUser gsonUser = new GsonUser(25, "Wbj");
        LogUtil.d(mLogTag, "Gson在默认情况下不自动导出值null的键-->" + gson.toJson(gsonUser));

        /**
         * serializeNulls()：导出空值的健；setPrettyPrinting()：格式化输出
         */
        Gson gsonCreate = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        //LogUtil.d(mLogTag, gsonCreate.toJson(gsonGenericUserList));

        String jsonExpose = "{\"dateBirth\": \"2017-10-03\", \"dateGraduate\": \"2037-07-01\",\"score\": 100,\"school\": \"哈佛大学\"}";
        Gson gsonExpose = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        GsonExpose gsonExposeObj = new GsonExpose(100, "1989-10-03", "2014-07-01", "Peking");
        LogUtil.d(mLogTag, "gsonExpose, Obj to Json-->" + gsonExpose.toJson(gsonExposeObj) + ",  Json to Obj-->" + gsonExpose.fromJson(jsonExpose, GsonExpose.class));


        /*
        如果单单是想序列化时（对象转json）设置日期(java.util.Date)的输出格式为："yyyy/MM/dd HH:mm:ss"，
        那么可以直接设置setDateFormat("yyyy/MM/dd HH:mm:ss")，而不用registerTypeAdapter。
         */
        Gson gsonTypeAdapter = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
        String toTypeAdapterJson = gsonTypeAdapter.toJson(new GsonTypeAdapter(new Date(), 21, 100));
        String tempDateTime = "{\"inSchool\":\"1472404303746\",\"numInt\":21,\"numDouble\":100.0}";
        LogUtil.d(mLogTag, "------------------------, setDateFormat, Obj to Json-->" + toTypeAdapterJson);
        LogUtil.d(mLogTag, "========================, " + gsonTypeAdapter.fromJson(tempDateTime, GsonTypeAdapter.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private final class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            //序死化时（对象转json），设置日期(java.util.Date)对象的输出格式为："yyyy/MM/dd HH:mm:ss"
            return new JsonPrimitive(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(src));
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            //反序死化时（json转对象），把日期"inSchool"键的值转化为日期(java.util.Date)对象
            long dateTimeLong = json.getAsLong();//"inSchool"键的值
            Log.d(mLogTag, "---->json.getAsLong()---->" + dateTimeLong);
            try {
                Date date = new Date(dateTimeLong);//long to Date
                LogUtil.d(mLogTag, "---->deserialize, date.getTime()---->" + new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(date));
                return date;
            } catch (Exception e) {
                LogUtil.e(mLogTag, e.toString());
            }

            return null;
        }
    }

}

