package com.medici.app.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import com.medici.app.entity.VisionMessage;

public class ByteUtils {

	public static Image getByteString(VisionMessage message) throws IOException {
		byte[] data = IOUtils.toByteArray(new URL(message.getImageUrl()));
		ByteString imgBytes = ByteString.copyFrom(data);
		return Image.newBuilder().setContent(imgBytes).build();
	}

}
