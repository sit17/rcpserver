package com.i5i58.primary.dao.channel;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelPushDevice;

public interface ChannelPushDevicePriDao extends PagingAndSortingRepository<ChannelPushDevice, Long> {

	ChannelPushDevice findByAccIdAndDeviceAndSerialNumAndModel(String accId, int device, String serialNum,
			String model);

	ChannelPushDevice findByAccIdAndDevice(String defaultPush, int device);
}
