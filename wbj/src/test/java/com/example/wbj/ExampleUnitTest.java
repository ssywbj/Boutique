package com.example.wbj;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ying.jie.entity.protobuf.ChildMsg;
import ying.jie.entity.protobuf.ObtainUserInfoProtocol;
import ying.jie.entity.protobuf.PersonEntity;
import ying.jie.entity.protobuf.PersonMsg;
import ying.jie.util.VoiceToText;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testBankNumber() throws Exception {
        String voiceToText = VoiceToText.voiceToText("124");
        assertEquals("一百二十四元", voiceToText);
        /*System.out.println("voiceToText = " + voiceToText);
        voiceToText = VoiceToText.voiceToText("545767425");
        System.out.println("voiceToText = " + voiceToText);*/
        //Log.i("dddd", "testBankNumber: "+voiceToText);
        /*assertEquals(4, VoiceToText.voiceToText("124"));
        assertEquals(4, 2 + 2);*/
        //assertEquals(4, 2 + 2);
    }

    @Test
    public void testPersonEntity() throws Exception {
        String name = "韦帮杰";
        System.out.println("汉字GBK编码占两个字节");
        for (byte b : name.getBytes("GBK")) {
            System.out.print(b + "\t");
        }
        System.out.println("\n" + "汉字UTF-8编码占三个字节");
        for (byte b : name.getBytes("UTF-8")) {
            System.out.print(b + "\t");
        }
        System.out.println();

        PersonEntity.Person.Builder builder = PersonEntity.Person.newBuilder();
        builder.setId(13);
        builder.setName(name);
        builder.setEmail("bonja.way@qq.com");
        PersonEntity.Person person = builder.build();
        System.out.println("序列化，对象变数据，byte array-->" + person.toByteArray());
        System.out.println("序列化，对象变数据，byte string-->" + person.toByteString());
        for (byte b : person.toByteArray()) {
            System.out.print(b + "\t");//从打印结果可以看出它用的"UTF-8"编码
        }
        System.out.println();
        System.out.println(new String(person.toByteArray(), 0, person.toByteArray().length));
        System.out.println(new String(person.toByteArray(), 4, 3));
        /*byte y = 119;//字符与字节之间可以相互转换
        char ch = (char) y;
        byte b = 'y';
        System.out.println("person.toByteArray()[4] = " + ch + ", b = " + b);*/
        PersonEntity.Person person1 = PersonEntity.Person.parseFrom(person.toByteArray());
        System.out.println("反序列化，ByteArray数据变对象-->" + person1 + "name-->" + person1.getName());
        PersonEntity.Person person2 = PersonEntity.Person.parseFrom(person.toByteString());
        System.out.println("反序列化，ByteString数据变对象-->" + person2 + "name--> " + person2.getName());

        //protoc -I=D:\Test\protobuf --java_out=D:\Test\protobuf D:\Test\protobuf\ObtainUserInfoProtocol.proto
        PersonMsg.Person.Builder builderInfo = PersonMsg.Person.newBuilder();
        builderInfo.setId(168);
        builderInfo.setName("Ssywbj");
        PersonMsg.Person personInfo = builderInfo.build();
        System.out.println("获取默认值-->" + PersonMsg.Person.parseFrom(personInfo.toByteArray()).getEmail());

        System.out.println("-------------------------------------------");

        ObtainUserInfoProtocol.Request.Builder builderRequest = ObtainUserInfoProtocol.Request.newBuilder();
        builderRequest.setId(34).setName("Ssywbj").setEmail("1271829012@qq.com");
        builderRequest.setInputType(ObtainUserInfoProtocol.InputType.EMAIL);
        builderRequest.setPerson(PersonMsg.Person.newBuilder().setId(2001).setName("yehuimei").setEmail("yehuimei@qq.com").build());
        builderRequest.setChild(ChildMsg.Child.newBuilder().setId(3001).setName("xiaogongqu"));
        builderRequest.addFriend("Jack").addFriend("Jessy").addFriend("Mark");//添加repeated类型的普通数据

        ObtainUserInfoProtocol.User.Builder builderUser = ObtainUserInfoProtocol.User.newBuilder();//先生成对象的Builder对象
        builderRequest.addUser(builderUser.setId(1001).setName("Wbj").setAge(13));//添加repeated类型的message数据方法1
        builderRequest.addUser(builderUser.setId(1002).setName("Ssy").setAge(14).build());//添加repeated类型的message数据方法2
        List<ObtainUserInfoProtocol.User> userList = new ArrayList<>();
        userList.add(builderUser.setId(1003).setName("a").setAge(15).build());
        userList.add(builderUser.setId(1004).setName("b").setAge(16).build());
        userList.add(builderUser.setId(1005).setName("c").setAge(17).build());
        userList.add(builderUser.setId(1006).setName("d").setAge(18).build());
        builderRequest.addAllUser(userList);//添加repeated类型的message数据方法3

        ObtainUserInfoProtocol.Request requestSend = builderRequest.build();//再由Builder对象生成对象

        ObtainUserInfoProtocol.Request requestReceive = ObtainUserInfoProtocol.Request.parseFrom(requestSend.toByteArray());
        System.out.println("id: " + requestReceive.getId() + "-->name: " + requestReceive.getName() +
                "-->email: " + requestReceive.getEmail() + "-->input type: " + requestReceive.getInputType() +
                "-->friend(0): " + requestReceive.getFriend(0) + "-->user(0): " + requestReceive.getUser(0));
        List<ObtainUserInfoProtocol.User> userListReceive = requestReceive.getUserList();
        for (ObtainUserInfoProtocol.User user : userListReceive) {
            System.out.println("user value-->" + user);
        }

    }
}