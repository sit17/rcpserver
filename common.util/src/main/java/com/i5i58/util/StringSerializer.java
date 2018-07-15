package com.i5i58.util;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class StringSerializer implements ZkSerializer {

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes);
    }

    @Override
    public byte[] serialize(Object serializable) throws ZkMarshallingError {
    	if (serializable instanceof String){
    		return ((String) serializable).getBytes();
    	}else{
    		throw new ZkMarshallingError();
    	}
    }

}