package com.smack.administrator.smackstudyapplication.dao;

import org.greenrobot.greendao.converter.PropertyConverter;

//定义消息发送状态
public enum MsgStatusEnum {
    draft(-1),//草稿
    sending(0),//发送
    success(1),//成功
    fail(2),//失败
    read(3),//已读
    unread(4);//未读

    private int value;

    private MsgStatusEnum(int var3) {
        this.value = var3;
    }


    public final int getValue() {
        return this.value;
    }

    public static class StatusConverter implements PropertyConverter<MsgStatusEnum,Integer>{

        @Override
        public MsgStatusEnum convertToEntityProperty(Integer databaseValue) {
            if(databaseValue == null){
                return fail;
            }

            for (MsgStatusEnum statu:MsgStatusEnum.values()){
                if(statu.value == databaseValue){
                    return statu;
                }
            }
            return fail;
        }

        @Override
        public Integer convertToDatabaseValue(MsgStatusEnum entityProperty) {
            return entityProperty == null?null:entityProperty.value;
        }
    }
}